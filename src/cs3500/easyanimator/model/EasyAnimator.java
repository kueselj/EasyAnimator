package cs3500.easyanimator.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * An EasyAnimator is an implementation of the animator model that works via maps.
 */
public class EasyAnimator implements IAnimatorModel {

  // CANVAS METHODS

  private Point canvasCorner;
  private WidthHeight canvasSize;

  @Override
  public void setCanvas(Point topLeftCorner, WidthHeight size) {
    if (topLeftCorner == null || size == null) {
      throw new IllegalArgumentException("Unable to set canvas with uninitialized parameters");
    }
    this.canvasCorner = topLeftCorner;
    this.canvasSize = size;
  }

  @Override
  public WidthHeight getCanvasSize() {
    if (canvasSize == null) {
      throw new IllegalStateException("Unable to request canvas size without setting it.");
    }
    return canvasSize;
  }

  @Override
  public Point getCanvasPosition() {
    if (canvasCorner == null) {
      throw new IllegalStateException("Unable to request canvas size without setting it.");
    }
    return canvasCorner;
  }

  // ANIMATION DATA SPECIFIC

  // We want a predictable iteration order to respect insertion order.
  // This is to clarify z-index later.
  // We track the type of shape here, and don't use the other fields of the shape.
  // This is a design compromise to avoid having a shape type class without fields.
  private final LinkedHashMap<String, IShape> namedShapes;

  // These two maps are tightly linked. Their keysets should match.

  // We track the motions of shapes here.
  // IShapes are used to track the state of a shape at a given tick.
  // They represent keyframes. We use a NavigableMap for utility.
  private final LinkedHashMap<String, NavigableMap<Integer, IShape>> shapeKeyframes;

  /**
   * Create a new basic model implementation and initialize internal data structures.
   */
  public EasyAnimator() {
    this.namedShapes = new LinkedHashMap<>();
    this.shapeKeyframes = new LinkedHashMap<>();
  }

  @Override
  public void addShape(String id, IShape shape) {
    if (id == null || shape == null) {
      throw new IllegalArgumentException("Unable to add shape with uninitialized parameters");
    }
    // We add the shape (basically type).
    namedShapes.put(id, shape.copy());
    // We add an entry for motions if necessary. Remember, they could be overriding the shape.
    if (!shapeKeyframes.containsKey(id)) {
      shapeKeyframes.put(id, new TreeMap<>());
      // Natural ordering luckily already makes sense for Integers.
    }
  }

  @Override
  public void removeShape(String id) {
    if (id == null) {
      throw new IllegalArgumentException("Unable to remove a shape with a null id");
    } else if (!namedShapes.containsKey(id)) {
      throw new IllegalArgumentException("Unable to remove a shape that does not exist.");
    }
    // The shape is valid. We will delete the shape and it's motions.
    namedShapes.remove(id);
    // These motions are guaranteed to exist by the invariant of us keeping our keysets the same.
    shapeKeyframes.remove(id);
  }

  @Override
  public Map<String, IShape> getShapes() {
    // We want to give all the shapes. We copy so we can't get mutated by a client by accident.
    // Even through the client doesn't know it's a LinkedHashMap,
    // they will be pleased by the iterator order.
    LinkedHashMap<String, IShape> map = new LinkedHashMap<>();
    for (Map.Entry<String, IShape> shapeEntry: namedShapes.entrySet()) {
      map.put(shapeEntry.getKey(), shapeEntry.getValue().copy());
    }
    return map;
  }

  // OPERATIONS WITH SOME LOGIC

  @Override
  public void addKeyframe(String id, IShape state, int tick) {
    // First we verify that the parameters are correct.
    if (id == null || state == null) {
      throw new IllegalArgumentException("Unable to add a keyframe with uninitialized parameters");
    } else if (tick < 0) {
      throw new IllegalArgumentException("Unable to add a keyframe with that illegal time (<0).");
    } else if (!namedShapes.containsKey(id)) {
      throw new IllegalArgumentException("Unable to add a state for a shape that doesn't exist");
    }

    shapeKeyframes.get(id).put(tick, state);
  }

  @Override
  public void removeKeyframe(String id, int tick) {
    // First we verify that the parameters are correct.
    if (id == null) {
      throw new IllegalArgumentException("Unable to remove keyframe for null shape name.");
    } else if (!namedShapes.containsKey(id)) {
      throw new IllegalArgumentException("Unable to remove keyframe for nonexistent shape.");
    } else if (!shapeKeyframes.get(id).containsKey(tick)) {
      throw new IllegalArgumentException("Unable to remove keyframe for nonexistent keyframe.");
    }
    shapeKeyframes.get(id).remove(tick);
  }

  @Override
  public SortedMap<Integer, IShape> getKeyframes(String id) {
    if (!namedShapes.containsKey(id)) {
      throw new IllegalArgumentException("Unable to get keyframes for nonexistent shape.");
    }
    // We build out a copy to avoid outside mutation.
    return new TreeMap<>(shapeKeyframes.get(id));
  }

  @Override
  public IShape getShapeAtTick(String id, int tick) {
    // Unfortunately I can't use the tweening available down in BasicMotions anymore.
    if (!namedShapes.containsKey(id)) {
      throw new IllegalArgumentException("Unable to get nonexistent shape at tick.");
    }
    IShape shape = namedShapes.get(id);
    NavigableMap<Integer, IShape> keyframes = shapeKeyframes.get(id);
    // If there are no keyframes then we return null.
    Map.Entry<Integer, IShape> leftKeyframe = keyframes.floorEntry(tick);
    Map.Entry<Integer, IShape> rightKeyframe = keyframes.higherEntry(tick);
    if (leftKeyframe != null && rightKeyframe != null) {
      // We are between two keyframes.
      return shape.accept(new Tweener(
              canvasCorner,
              leftKeyframe.getValue(),
              leftKeyframe.getKey(),
              rightKeyframe.getValue(),
              rightKeyframe.getKey(),
              tick));
    } else {
      // We are outside motions and so we return null.
      return null;
    }
  }

  @Override
  public List<IShape> getShapesAtTick(int tick) {
    // I can re-use the method I already defined.
    ArrayList<IShape> shapes = new ArrayList<>();
    // Note this is sorted by insertion order so our list will be in insertion order.
    for (String shapeName: namedShapes.keySet()) {
      IShape shape = getShapeAtTick(shapeName, tick);
      if (shape != null) {
        shapes.add(shape);
      }
    }
    return shapes;
  }

  @Override
  public int getShapeMaxTick(String id) {
    if (!namedShapes.containsKey(id)) {
      throw new IllegalArgumentException("Unable to get max tick for nonexistent shape.");
    }
    if (shapeKeyframes.get(id).size() == 0) {
      return 0; // N.A
    }
    return shapeKeyframes.get(id).lastKey();
  }

  @Override
  public void addMotion(String id, IMotion motion) {
    // A motion is basically two keyframes.
    // But we want to make sure we still follow the old invariants.
    if (id == null || motion == null) {
      throw new IllegalArgumentException("Unable to add motion with uninitialized parameters.");
    } else if (!namedShapes.containsKey(id)) {
      throw new IllegalArgumentException("Unable to add motion with nonexistent shape.");
    }
    // First conflicting time. Is there a keyframe in the middle of the motion?
    IShape shape = namedShapes.get(id);
    NavigableMap<Integer, IShape> keyframes = shapeKeyframes.get(id);
    NavigableSet<Integer> ticks = keyframes.navigableKeySet();
    List<Integer> intersection = new ArrayList<>(ticks.headSet(motion.getStartTime(), true));
    intersection.retainAll(ticks.tailSet(motion.getEndTime(), false));
    if (intersection.size() > 0) {
      throw new IllegalArgumentException("Unable to add the motion, " +
              "there exists keyframes in between the suggested start and end times.");
    }
    // Next does the state conflict?
    IShape frameLeft = null;
    if (keyframes.floorEntry(motion.getStartTime()) != null) {
      frameLeft = keyframes.floorEntry(motion.getStartTime()).getValue();
    }
    IShape frameRight = null;
    if (keyframes.ceilingEntry(motion.getEndTime()) != null) {
      frameRight = keyframes.ceilingEntry(motion.getEndTime()).getValue();
    }
    if (frameLeft != null &&
            !(frameLeft.getSize().equals(motion.getStartSize()) &&
                    frameLeft.getPosition().equals(motion.getStartPosition()) &&
                    frameLeft.getColor().equals(motion.getStartColor()))) {
      System.out.println(frameLeft.getColor());
      System.out.println(motion.getStartColor());

      throw new IllegalArgumentException("Unable to add the given motion, " +
              "state mismatch on the left.");

    } else if (frameRight != null &&
            !(frameRight.getSize().equals(motion.getEndSize()) &&
                    frameRight.getPosition().equals(motion.getEndPosition()) &&
                    frameRight.getColor().equals(motion.getEndColor()))) {
      throw new IllegalArgumentException("Unable to add the given motion, " +
              "state mismatch on the right.");
    }
    // We passed our tests, lets add some keyframes.
    keyframes.put(motion.getStartTime(), shape.accept(new ShapeF(
            motion.getStartSize(),
            motion.getStartPosition(),
            motion.getStartColor())));
    keyframes.put(motion.getEndTime(), shape.accept(new ShapeF(
            motion.getEndSize(),
            motion.getEndPosition(),
            motion.getEndColor())));
  }

  @Override
  public void removeMotion(String id, IMotion motion) {
    // Check our parameters.
    if (id == null || motion == null) {
      throw new IllegalArgumentException("Unable to remove motion with uninitialized parameters.");
    } else if (!namedShapes.containsKey(id)) {
      throw new IllegalArgumentException("Unable to remove motion for nonexistent shape.");
    }
    // Now we want to search for the motion.
    NavigableMap<Integer, IShape> keyframes = shapeKeyframes.get(id);
    // Do the keyframes exist?
    if (!keyframes.containsKey(motion.getStartTime()) ||
      !keyframes.containsKey(motion.getEndTime())) {
      throw new IllegalArgumentException("Unable to remove this motion, it does not exist.");
    }
    // Finally, do we cause a state clash by removing this motion?
    Map.Entry<Integer, IShape> lower = keyframes.lowerEntry(motion.getStartTime());
    Map.Entry<Integer, IShape> higher = keyframes.higherEntry(motion.getEndTime());
    if (lower != null && higher != null) {
      IShape lowerValue = lower.getValue();
      IShape higherValue = higher.getValue();
      if (!(lowerValue.getSize().equals(higherValue.getSize()) &&
              lowerValue.getPosition().equals(higherValue.getPosition()) &&
              lowerValue.getColor().equals(higherValue.getColor()))) {
        throw new IllegalArgumentException("Removing this motion would cause a state mismatch.");
      }
    }

    // We passed all the tests, let's do removal.
    keyframes.remove(motion.getStartTime());
    keyframes.remove(motion.getEndTime());
  }

  @Override
  public Map<String, SortedSet<IMotion>> getSortedMotions() {
    // We simulate motions.
    LinkedHashMap<String, SortedSet<IMotion>> out = new LinkedHashMap<>();
    for (Map.Entry<String, NavigableMap<Integer, IShape>> shapeAndKeyframes:
            shapeKeyframes.entrySet()) {
      SortedSet<IMotion> motions = new TreeSet<>(Comparator.comparingInt(IMotion::getEndTime));
      Map.Entry<Integer, IShape> previousKeyframe = null;
      for (Map.Entry<Integer, IShape> keyframe:
               shapeAndKeyframes.getValue().entrySet()) {
        if (previousKeyframe == null) {
          previousKeyframe = keyframe;
          continue; // Next loop.
        } else {
          // We only add a motion if it changes the value.
          WidthHeight prevSize = previousKeyframe.getValue().getSize();
          WidthHeight curSize = keyframe.getValue().getSize();
          Point prevPos = previousKeyframe.getValue().getPosition();
          Point curPos = keyframe.getValue().getPosition();
          Color prevColor = previousKeyframe.getValue().getColor();
          Color curColor = keyframe.getValue().getColor();
          if (!(prevSize.equals(curSize) &&
                  prevPos.equals(curPos) &&
                  prevColor.equals(curColor))) {
            motions.add(new BasicMotion(previousKeyframe.getKey(),
                    keyframe.getKey(),
                    prevSize, curSize,
                    prevPos, curPos,
                    prevColor, curColor));
          }
          previousKeyframe = keyframe;
        }
      }
      if (shapeAndKeyframes.getValue().size() == 1) {
        // If there is only one keyframe then the above loop wouldn't create a motion.
        Map.Entry<Integer, IShape> soleKeyframe = shapeAndKeyframes.getValue().firstEntry();
        motions.add(new BasicMotion(soleKeyframe.getKey(), soleKeyframe.getKey(),
                soleKeyframe.getValue().getSize(), soleKeyframe.getValue().getSize(),
                soleKeyframe.getValue().getPosition(), soleKeyframe.getValue().getPosition(),
                soleKeyframe.getValue().getColor(), soleKeyframe.getValue().getColor()));
      }
      out.put(shapeAndKeyframes.getKey(), motions);
    }
    return out;
  }

  /**
   * A shape factory produces a shape instance for the given IShape parameters.
   */
  private static class ShapeF implements IShapeVisitor<IShape> {
    private WidthHeight size;
    private Point position;
    private Color color;

    /**
     * Create a new shape.
     * @param size      The size to use.
     * @param position  The position as a point to use.
     * @param color     The color to construct with.
     */
    ShapeF(WidthHeight size, Point position, Color color) {
      this.size = size;
      this.position = position;
      this.color = color;
    }

    @Override
    public IShape applyToRectangle(Rectangle r) {
      return new Rectangle(size, position, color);
    }

    @Override
    public IShape applyToOval(Oval o) {
      return new Rectangle(size, position, color);
    }
  }

  /**
   * A Tweener is a small visitor to return a shape constructed between the two given shapes.
   */
  private static class Tweener extends ShapeF {
    /**
     * Construct a new Tweener with state before, after, and the tick to pick.
     *
     * @param before  The state before the tick.
     * @param beforeT The tick at which state before appears.
     * @param after   The state after the tick.
     * @param afterT  The state at which state after appears.
     * @param tick    The tick we wish to get the shape at.
     */
    Tweener(Point canvasCorner,
            IShape before, int beforeT, IShape after, int afterT, int tick) {
      super(new WidthHeight(IMotion.tween(before.getSize().getWidth(), beforeT,
              after.getSize().getWidth(), afterT,
              tick),
              IMotion.tween(before.getSize().getHeight(), beforeT,
                      after.getSize().getHeight(), afterT,
                      tick)),

              new Point(IMotion.tween(before.getPosition().getX() - canvasCorner.getX(),
                      beforeT,
              after.getPosition().getX(), afterT,
              tick),
              IMotion.tween(before.getPosition().getY() - canvasCorner.getY(),
                      beforeT,
                      after.getPosition().getY(), afterT,
                      tick)),


              new Color(IMotion.tween(before.getColor().getRed(), beforeT,
                      after.getColor().getRed(), afterT,
                      tick),
                      IMotion.tween(before.getColor().getGreen(), beforeT,
                        after.getColor().getGreen(), afterT,
                        tick),
                      IMotion.tween(before.getColor().getBlue(), beforeT,
                        after.getColor().getBlue(), afterT,
                        tick)));
    }
  }
}
