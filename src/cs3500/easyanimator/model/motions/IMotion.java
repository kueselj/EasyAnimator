package cs3500.easyanimator.model.motions;

import java.awt.Color;
import java.awt.Point;

/**
 * Interface for a Motion.
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
   * Gets the start width of the Motion.
   *
   * @return the start width of the Motion as an int.
   */
  int getStartWidth();

  /**
   * Gets the end width of the Motion.
   *
   * @return the end width of the Motion as an int.
   */
  int getEndWidth();

  /**
   * Gets the start height of the Motion.
   *
   * @return the start height of the Motion as an int.
   */
  int getStartHeight();

  /**
   * Gets the end height of the Motion.
   *
   * @return the end height of the Motion as an int.
   */
  int getEndHeight();

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
