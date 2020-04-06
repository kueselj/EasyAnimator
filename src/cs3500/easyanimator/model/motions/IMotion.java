package cs3500.easyanimator.model.motions;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * Interface for a Motion, defines only getter methods for the fields of an implementation.
 */
public interface IMotion {

  /**
   * Gets the start time of the Motion.
   *
   * @return the start time of the Motion as an int.
   */
  int getStartTime();

  /**
   * Gets the end time of the Motion.
   *
   * @return the end time of the Motion as an int.
   */
  int getEndTime();

  /**
   * Gets the start position of the Motion.
   * @return the start position of the Motion represented by a Point.
   */
  Point getStartPosition();

  /**
   * Gets the end position of the Motion.
   * @return the end position of the Motion represented by a Point.
   */
  Point getEndPosition();

  /**
   * Gets the start size of the Motion.
   * @return the start size of the Motion as an widthHeight.
   */
  WidthHeight getStartSize();

  /**
   * Gets the end size of the Motion.
   * @return the end size of the Motion as a widthHeight.
   */
  WidthHeight getEndSize();

  /**
   * Gets the start color of the Motion.
   * @return the start color of the Motion represented by a Color.
   */
  Color getStartColor();

  /**
   * Gets the end color of the Motion.
   * @return the end color of the Motion represented by a Color.
   */
  Color getEndColor();

  /**
   * Returns a string representation of the motion, which includes all of its fields.
   * @return a string representing the motion.
   */
  String toString();

  /**
   * Accepts an IMotionVisitor and applies it.
   * @param motion the visitor to use.
   * @param <T> the type to return
   * @return of type T.
   */
  <T> T accept(IMotionVisitor<T> motion);

  /**
   * Gives the position at the given tick. This was abstracted away from model code.
   * @param tick  The tick to fetch the position frame of.
   * @return      The position as a Point tweened correctly between the start and endTime.
   *              The behavior outside those bounds is undefined.
   */
  Point getPosition(int tick);

  /**
   * Gives the size at the given tick. This was abstracted away from model code.
   * @param tick  The tick to fetch the size frame of.
   * @return      The size as a Point tweened correctly between the start and endTime.
   *              The behavior outside those bounds is undefined.
   */
  WidthHeight getSize(int tick);

  /**
   * Gives the color at the given tick. This was abstracted away from model code.
   * @param tick  The tick to fetch the size frame of.
   * @return      The color as a Point tweened correctly between the start and endTime.
   *              The behavior outside those bounds is undefined.
   */
  Color getColor(int tick);

  /**
   * Returns if there is a time conflict between two motions.
   * @param motion      The first motion to compare.
   * @param otherMotion The second motion to compare.
   * @return
   */
  static boolean timeConflict(IMotion motion, IMotion otherMotion) {
    return (motion.getStartTime() >= otherMotion.getStartTime() &&
            // It is conflicting if the end time is past our start time.
            otherMotion.getEndTime() > motion.getStartTime()) ||
            // The second situation [ newmotion [ oldmotion )
            // We need to verify the newmotion ends before the old motion starts.
            (motion.getStartTime() < otherMotion.getStartTime() &&
                    // The newmotion is conflicting if it ends after the other start time.
                    motion.getEndTime() > otherMotion.getStartTime());
  }

  /**
   * Tests if the end state of the first motion matches the start state of the second motion.
   * @param first   The first motion.
   * @param second  The second motion.
   * @return  True if the end state and start state match, false otherwise.
   */
  static boolean endStartStateMatch(IMotion first, IMotion second) {
    return (first.getEndSize().equals(second.getStartSize())
            && first.getEndPosition().equals(second.getStartPosition())
            && first.getEndColor().equals(second.getStartColor()));
  }
}
