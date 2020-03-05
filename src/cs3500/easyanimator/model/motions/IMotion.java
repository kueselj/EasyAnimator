package cs3500.easyanimator.model.motions;

import cs3500.easyanimator.model.shapes.WidthHeight;

import java.awt.Color;
import java.awt.Point;

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
   *
   * @return the start position of the Motion represented by a Point.
   */
  Point getStartPosition();

  /**
   * Gets the end position of the Motion
   *
   * @return the end position of the Motion represented by a Point.
   */
  Point getEndPosition();

  /**
   * Gets the start size of the Motion.
   *
   * @return the start size of the Motion as an widthHeight.
   */
  WidthHeight getStartSize();

  /**
   * Gets the end size of the Motion.
   *
   * @return the end size of the Motion as a widthHeight.
   */
  WidthHeight getEndSize();

  /**
   * Gets the start color of the Motion.
   *
   * @return the start color of the Motion represented by a Color.
   */
  Color getStartColor();

  /**
   * Gets the end color of the Motion.
   *
   * @return the end color of the Motion represented by a Color.
   */
  Color getEndColor();
}
