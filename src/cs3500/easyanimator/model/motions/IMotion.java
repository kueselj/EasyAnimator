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
}
