package cs3500.easyanimator.model;

import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.motions.IMotionVisitor;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;

import java.util.ArrayList;
import java.util.Collections;
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
    this.shapes = new HashMap<String, IShape>();
    this.motions = new HashMap<String, List<IMotion>>();
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
    } else {
      // Do nothing. We preserve the motions.
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

  /**
   * A private helper that checks if the given motion when added would cause any conflicts of time.
   * @param id      The id of the shape being animated.
   * @param motion  The motion that would be added.
   * @return  A boolean indicating if there is a time conflict (true) or not (false).
   */
  private boolean conflictingTime(String id, IMotion motion) {
    return this.motions.get(id)
            .stream()
            .anyMatch(otherMotion ->
                    (motion.getStartTime() >= otherMotion.getStartTime()
                            && otherMotion.getEndTime() > motion.getStartTime())
                            || (motion.getStartTime() <= otherMotion.getStartTime()
                            && otherMotion.getStartTime() <= motion.getEndTime()));
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

  /**
   * Tests if the end state of the first motion matches the start state of the second motion.
   * @param first   The first motion.
   * @param second  The second motion.
   * @return  True if the end state and start state match, false otherwise.
   */
  private boolean endStartStateMatch(IMotion first, IMotion second) {
    return (first.getEndSize().equals(second.getStartSize())
            && first.getEndPosition().equals(second.getStartPosition())
            && first.getEndColor().equals(second.getStartColor()));
  }

  @Override
  public void addMotion(String id, IMotion motion) {
    if (id == null || motion == null) {
      throw new IllegalArgumentException("Attempted to addMotion with uninitialized parameters.");
    }

    if (this.motions.containsKey(id)) {
      // We then need to check that we now don't violate our two invariants with this motion.

      // The first is easy, do we conflict with any other motions in range of time.
      boolean conflict = conflictingTime(id, motion);
      if (conflict) {
        throw new IllegalArgumentException("Unable to add motion, conflicts in time ranges.");
      }

      // The second is harder,
      // do we conflict with any other motions by not having our states line up?
      Optional<IMotion> previous = seekBackward(id, motion);
      Optional<IMotion> next = seekForward(id, motion);
      if (previous.isPresent()) {
        if (!endStartStateMatch(previous.get(), motion)) {
          throw new IllegalArgumentException("Attempted to add a motion that creates a state"
                  + " mismatch.");
        }
      }
      if (next.isPresent()) {
        if (!endStartStateMatch(motion, next.get())) {
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
            !endStartStateMatch(previous.get(), next.get())) {
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
   * Outputs a String representing a text rendering output of the model.
   *
   * @return a text rendering output of the model as a String.
   */
  @Override
  public String textOutput() {

    //output string builder to build.
    StringBuilder output = new StringBuilder();


    //iterate through each shape or entry in
    for (Map.Entry<String, List<IMotion>> entry: this.motions.entrySet()) {

      String shapeType = this.shapes.get(entry.getKey()).accept(new ShapeTypeVisitor());
      String shapeString = "shape " + entry.getKey() + " " + shapeType;

      //add the shape to the output.
      output.append(shapeString + "\n");

      Collections.sort(entry.getValue(), Comparator.comparingInt(IMotion::getStartTime));

      //now iterate through the list of motions for the given entry.
      for (IMotion motion: entry.getValue()) {
        String motionString = "motion " + entry.getKey() + " "
                + motion.accept(new MotionToTextVisitor());
        output.append(motionString + "\n");
      }

      output.append("\n");
    }

    return output.toString().trim();
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

    // Try and see if the shape exists, if so grab its motions, sort them, and grab the shape.
    try {
      shapeMotions = this.motions.get(shape);
      //Collections.sort(shapeMotions, Comparator.comparingInt(IMotion::getStartTime));
      shapeType = this.shapes.get(shape);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("That shape does not exist.");
    }

    // Try and see if there is a motion for the desired tick.
    try {
      //System.out.println(shapeMotions);
      desiredMotion = this.getFromMotionTick(tick, shapeMotions);
      //System.out.println(desiredMotion);
      //System.out.println(desiredMotion.getStartTime());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Not a valid tick");
    }

    //if all is valid, return a new shape with the correct state at a desired tick.
    return shapeType.accept(new GetShapeAtMotionTick(desiredMotion, tick,
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
  private class GetShapeAtMotionTick implements IShapeVisitor<IShape> {

    private IMotion motion;
    private int tick;
    private int sT;
    private int eT;
    private int width;
    private int height;
    private int r;
    private int g;
    private int b;
    private int x;
    private int y;


    /**
     * Basic constructor for the visitor, takes in a motion and a tick to use.
     * @param motion the motion to tween for the desired tick.
     * @param tick the tick to find the shape's shape at.
     */
    private GetShapeAtMotionTick(IMotion motion, int tick, int canvasX, int canvasY) {
      this.motion = motion;
      this.tick = tick;
      this.sT = this.motion.getStartTime();

      this.eT = this.motion.getEndTime();


      this.width = this.tween(this.motion.getStartSize().getWidth(),
              sT, this.motion.getEndSize().getWidth(), eT, tick);
      this.height = this.tween(this.motion.getStartSize().getHeight(),
              sT, this.motion.getEndSize().getHeight(), eT, tick);
      this.r = this.tween(this.motion.getStartColor().getRed(), sT,
              this.motion.getEndColor().getRed(), eT, tick);
      this.g = this.tween(this.motion.getStartColor().getGreen(), sT,
              this.motion.getEndColor().getGreen(), eT, tick);
      this.b = this.tween(this.motion.getStartColor().getBlue(), sT,
              this.motion.getEndColor().getBlue(), eT, tick);
      this.x = this.tween(this.motion.getStartPosition().getX(), sT,
              this.motion.getEndPosition().getX(), eT, tick) - canvasX;
      this.y = this.tween(this.motion.getStartPosition().getY(), sT,
              this.motion.getEndPosition().getY(), eT, tick) - canvasY;
    }

    @Override
    public IShape applyToRectangle(Rectangle rect) {
      return new Rectangle(new WidthHeight(width, height), new Point(x, y), new Color(r, g, b));
    }

    @Override
    public IShape applyToOval(Oval oval) {
      return new Oval(new WidthHeight(width, height), new Point(x, y), new Color(r, g, b));
    }

    /**
     * Finds the tweened value between the two values at the desired tick.
     * @param val1 the first int.
     * @param val1T the tick time for a.
     * @param val2 the second int.
     * @param val2T the tick time for b.
     * @param tick the desired tick time.
     * @return the value at the desired tick.
     */
    private int tween(int val1, int val1T, int val2, int val2T, int tick) {

      double a = (double) val1;
      double aT = (double) val1T;
      double b = (double) val2;
      double bT = (double) val2T;
      double t = (double) tick;

      double f1 = (bT - t) / (bT - aT);
      double f2 = a * f1;
      //System.out.println(f2);

      double g1 = (t - aT) / (bT - aT);
      double g2 = b * g1;
      return (int) (f2 + g2);
    }
  }

  /**
   * Private visitor class to return the type of shape as a string.
   */
  private class ShapeTypeVisitor implements IShapeVisitor<String> {

    @Override
    public String applyToRectangle(Rectangle r) {
      return "rectangle";
    }

    @Override
    public String applyToOval(Oval o) {
      return "oval";
    }
  }

  /**
   * A private visitor class to return an IMotion as represented by a string.
   */
  private class MotionToTextVisitor implements IMotionVisitor<String> {

    @Override
    public String applyToBasicMotion(BasicMotion b) {
      return b.getStartTime() + " "
              + b.getStartPosition().getX() + " "
              + b.getStartPosition().getY() + " "
              + b.getStartSize().getWidth() + " "
              + b.getStartSize().getHeight() + " "
              + b.getStartColor().getRed() + " "
              + b.getStartColor().getGreen() + " "
              + b.getStartColor().getBlue() + "    "
              + b.getEndTime() + " "
              + b.getEndPosition().getX() + " "
              + b.getEndPosition().getY() + " "
              + b.getEndSize().getWidth() + " "
              + b.getEndSize().getHeight() + " "
              + b.getEndColor().getRed() + " "
              + b.getEndColor().getGreen() + " "
              + b.getEndColor().getBlue();
    }
  }
}
