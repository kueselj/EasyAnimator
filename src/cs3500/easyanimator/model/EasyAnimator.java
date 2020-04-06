package cs3500.easyanimator.model;

import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* An EasyAnimator is an implementation of the animator model that works via maps.
*/
public class EasyAnimator implements IAnimatorModel {

  // These fields were made private following assignment 5 feedback.
  private HashMap<String, IShape> shapes;
  // The list of motions we associate with will have the invariant that the end state of the
  // previous motion will match the start state of the next motion.
  // Additionally none of the motions will clash in tick range.
  // We have another invariant here that the keysets for the shapes map and the motions map should
  // match.
  private HashMap<String, List<IMotion>> motions;

  // These are part of the new update.
  private Point canvasPosition;
  private WidthHeight canvasSize;

  /**
   * Basic constructor for an EasyAnimator, just gives sets both fields to empty HashMaps.
   */
  public EasyAnimator() {
    this.shapes = new HashMap<>();
    this.motions = new HashMap<>();
  }

  @Override
  public void setCanvas(Point topLeftCorner, WidthHeight widthHeight) {
    if (topLeftCorner == null || widthHeight == null) {
      throw new IllegalArgumentException("Attempted to set canvas with uninitialized parameters.");
    }
    this.canvasPosition = topLeftCorner;
    this.canvasSize = widthHeight;
  }

  @Override
  public void addShape(String id, IShape shape) {
    if (id == null || shape == null) {
      throw new IllegalArgumentException("Attempted to addShape with uninitialized parameters.");
    }

    // We add the shape. And watch to see if we replaced anything.
    IShape previous = this.shapes.put(id, shape.copy());
    if (previous == null) {
      // We will need to add a blank list to motions.
      this.motions.put(id, new ArrayList<>());
    }
  }

  @Override
  public void removeShape(String id) {
    if (id == null) {
      throw new IllegalArgumentException("Attempted to remove a shape with an uninitialized id.");
    }

    if (this.shapes.containsKey(id)) {
      // If there was a matching key.
      this.shapes.remove(id);
      // We also need to remove any motions, this should not throw an exception.
      this.motions.remove(id);
    } else {
      // If there was no matching key.
      throw new IllegalArgumentException("Attempted to removeShape with an id that doesn't match.");
    }
  }

  @Override
  public WidthHeight getCanvasSize() {
    if (this.canvasSize == null) {
      throw new IllegalStateException("The canvas has yet to be set.");
    }
    return this.canvasSize; // I don't copy because these are technically immutable.
    // This was a mistake of the past.
  }

  @Override
  public Point getCanvasPosition() {
    if (this.canvasPosition == null) {
      throw new IllegalStateException("The canvas has yet to be set.");
    }
    return this.canvasPosition;
  }

  @Override
  public Map<String, IShape> getShapes() {
    Map<String, IShape> newMap = new HashMap<>();
    for (Map.Entry<String, IShape> entry: shapes.entrySet()) {
      newMap.put(entry.getKey(), entry.getValue().copy());
    }
    return newMap;
  }

  @Override
  public long getShapeMaxTick(String id) {
    if (!getShapes().keySet().contains(id)) {
      throw new IllegalArgumentException("Unable to fetch max tick for nonexistent shape.");
    }
    long maxTick = 0;
    for (IMotion motion: getMotions().get(id)) {
      long motionEndTime = motion.getEndTime();
      if (motionEndTime > maxTick) {
        maxTick = motionEndTime;
      }
    }
    return maxTick;
  }

  /**
   * A private helper that checks if the given motion when added would cause any conflicts of time.
   * @param id      The id of the shape being animated.
   * @param motion  The motion that would be added.
   * @return  A boolean indicating if there is a time conflict (true) or not (false).
   */
  private boolean conflictingTime(String id, IMotion motion) {
    for (IMotion otherMotion: motions.get(id)) {
      if (IMotion.timeConflict(motion, otherMotion)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Seeks backward through the list of motions for the given id for the next previous motion to
   * the provided one (doesn't have to be in the list).
   * @param id      The id of the shape to seek backwards in the list of.
   * @param motion  The motion to seek from.
   * @return        The IMotion "behind" this motion (in terms of end time) or empty.
   */
  private Optional<IMotion> seekBackward(String id, IMotion motion) {
    return this.motions.get(id).stream()
            // We only want the ones behind it.
            .filter(otherMotion -> otherMotion.getEndTime() < motion.getEndTime())
            // And we want the nearest one.
            .max(Comparator.comparingInt(IMotion::getEndTime));
  }

  /**
   * Seeks forward through the list of motions for the given id for the next motion to
   * the provided one (doesn't have to be in the list).
   * @param id      The id of the shape to seek forwards in the list of.
   * @param motion  The motion to seek from.
   * @return        The IMotion "in front of" this motion (in terms of end time) or empty.
   */
  private Optional<IMotion> seekForward(String id, IMotion motion) {
    return this.motions.get(id).stream()
            // We only want the ones behind it.
            .filter(otherMotion -> otherMotion.getStartTime() >= motion.getEndTime())
            // And we want the nearest one.
            .max(Comparator.comparingInt(IMotion::getStartTime));
  }

  @Override
  public void addMotion(String id, IMotion motion) {
    if (id == null || motion == null) {
      throw new IllegalArgumentException("Attempted to addMotion with uninitialized parameters.");
    }

    if (this.motions.containsKey(id)) {
      // We then need to check that we now don't violate our two invariants with this motion.

      // The first is easy, do we conflict with any other motions in range of time?
      boolean conflict = conflictingTime(id, motion);
      if (conflict) {
        throw new IllegalArgumentException("Unable to add motion, conflicts in time ranges.");
      }

      // The second is harder,
      // do we conflict with any other motions by not having our states line up?
      Optional<IMotion> previous = seekBackward(id, motion);
      Optional<IMotion> next = seekForward(id, motion);
      if (previous.isPresent()) {
        if (!IMotion.endStartStateMatch(previous.get(), motion)) {
          throw new IllegalArgumentException("Attempted to add a motion that creates a state"
                  + " mismatch.");
        }
      }
      if (next.isPresent()) {
        if (!IMotion.endStartStateMatch(motion, next.get())) {
          throw new IllegalArgumentException("Attempted to add a motion that creates a state"
                  + " mismatch.");
        }
      }

      this.motions.get(id).add(motion);
    } else {
      // Motions does not have a key for id.
      throw new IllegalArgumentException("Attempted to addMotion with an invalid unmatched id.");
    }
  }

  @Override
  public void removeMotion(String id, IMotion motion) {
    if (id == null || motion == null) {
      throw new IllegalArgumentException("Attempted to removeMotion with uninitialized " +
              "parameters.");
    }

    if (!motions.containsKey(id)) {
      throw new IllegalArgumentException("Attempted to removeMotion with an id that doesn't match" +
              " to a shape.");
    }

    if (!motions.get("C").contains(motion)) {
      throw new IllegalArgumentException("Attempted to removeMotion with a motion that wasn't " +
              "added to the shape.");
    }

    // We then need to check if removing this motion breaks our invariants.
    Optional<IMotion> previous = seekBackward(id, motion);
    Optional<IMotion> next = seekForward(id, motion);

    if (previous.isPresent() && next.isPresent() &&
            !IMotion.endStartStateMatch(previous.get(), next.get())) {
      throw new IllegalArgumentException("Attempted to removeMotion that would lead to invalid " +
              "state.");
    }

    motions.get("C").remove(motion);
  }

  @Override
  public Map<String, List<IMotion>> getMotions() {
    return this.motions;
  }

  /**
   * Gets the motion in the list of motions given that contains the given tick.
   *
   * @param tick the tick to find a corresponding motion for.
   * @param motions the list of motions to find.
   * @return the motion that contains the desired tick.
   */
  private IMotion getFromMotionTick(int tick, List<IMotion> motions) {
    for (IMotion mt: motions) {
      if (tick >= mt.getStartTime() && tick <= mt.getEndTime()) {
        return mt;
      }
    }
    throw new IllegalArgumentException("No motion has that tick!");
  }

  @Override
  public IShape getShapeAtTick(int tick, String shape) {
    //try to get the shapes list of motions

    IShape shapeType;
    List<IMotion> shapeMotions;
    IMotion desiredMotion;

    if (!this.motions.containsKey(shape) || !this.shapes.containsKey(shape)) {
      throw new IllegalArgumentException("That shape does not exist.");
    }

    // Grab its motions, sort them, and grab the shape.
    shapeMotions = this.motions.get(shape);
    // Collections.sort(shapeMotions, Comparator.comparingInt(IMotion::getStartTime));
    shapeType = this.shapes.get(shape);

    // Try and see if there is a motion for the desired tick.
    try {
      desiredMotion = this.getFromMotionTick(tick, shapeMotions);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Not a valid tick.");
    }

    // if all is valid, return a new shape with the correct state at a desired tick.
    return shapeType.accept(new GetShapeAtTick(desiredMotion, tick,
            this.getCanvasPosition().getX(), this.getCanvasPosition().getY()));
  }

  @Override
  public List<IShape> getShapesAtTick(int tick) {
    List<IShape> shapesAtTick = new ArrayList<IShape>();

    //iterate through all the entries in the map.
    for (Map.Entry<String, List<IMotion>> entry: this.motions.entrySet()) {
      try {
        //try and see if the entry has a state at the given tick and save it
        IShape shapeToAdd = this.getShapeAtTick(tick, entry.getKey());
        shapesAtTick.add(shapeToAdd);
      } catch (IllegalArgumentException e) {
        //Do nothing. Not a valid shape at the given tick so just dont add it.
      }
    }
    return shapesAtTick;
  }

  /**
   * Custom visitor for getting a shape's state at a given tick.
   */
  private class GetShapeAtTick implements IShapeVisitor<IShape> {
    private WidthHeight size;
    private Point position;
    private Color color;

    /**
     * Basic constructor for the visitor, takes in a motion and a tick to use.
     * @param motion the motion to tween for the desired tick.
     * @param tick the tick to find the shape's shape at.
     */
    private GetShapeAtTick(IMotion motion, int tick, int canvasX, int canvasY) {
      this.size = motion.getSize(tick);
      this.color = motion.getColor(tick);

      // We need to reposition.
      if (canvasX != 0 || canvasY != 0) {
        Point preTransPosition = motion.getPosition(tick);
        this.position = new Point(preTransPosition.getX() - canvasX,
                preTransPosition.getY() - canvasY);
      } else {
        this.position = motion.getPosition(tick);
      }
    }

    @Override
    public IShape applyToRectangle(Rectangle rect) {
      return new Rectangle(this.size, this.position, this.color);
    }

    @Override
    public IShape applyToOval(Oval oval) {
      return new Oval(this.size, this.position, this.color);
    }
  }
}
