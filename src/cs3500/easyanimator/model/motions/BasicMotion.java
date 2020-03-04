package cs3500.easyanimator.model.motions;

import java.awt.Point;
import java.awt.Color;

import cs3500.easyanimator.model.shapes.widthHeight;

/**
 * Class representing a basic motion, has a start time, end time,
 * start size, end size, start position, end position, start color, and end color.
 */
public class BasicMotion implements IMotion {

  private final int startTime;
  private final int endTime;

  private final widthHeight startSize;
  private final widthHeight endSize;

  private final Point startPosition;
  private final Point endPosition;

  private final Color startColor;
  private final Color endColor;

  /**
   * Basic Constructor for a BasicMotion.
   *
   * @param startTime the start time of the Motion.
   * @param endTime the end time of the Motion.
   * @param startSize the start width of the Motion.
   * @param endSize the end width of the Motion.
   * @param startPosition the start position of the Motion.
   * @param endPosition the end position of the Motion.
   * @param startColor the start color of the Motion.
   * @param endColor the end color of the Motion.
   */
  public BasicMotion(int startTime, int endTime,
                     widthHeight startSize, widthHeight endSize,
                     Point startPosition, Point endPosition,
                     Color startColor, Color endColor) {

    this.startTime = startTime;
    this.endTime = endTime;
    this.startSize = startSize;
    this.endSize = endSize;
    this.startPosition = startPosition;
    this.endPosition = endPosition;
    this.startColor = startColor;
    this.endColor = endColor;
  }

  @Override
  public int getStartTime() {
    return this.startTime;
  }

  @Override
  public int getEndTime() {
    return this.endTime;
  }

  @Override
  public Point getStartPosition() {
    return new Point(this.startPosition);
  }

  @Override
  public Point getEndPosition() {
    return new Point(this.endPosition);
  }

  @Override
  public widthHeight getStartSize() {
    return new widthHeight(this.startSize);
  }

  @Override
  public widthHeight getEndSize() {
    return new widthHeight(this.endSize);
  }

  @Override
  public Color getStartColor() {
    return this.startColor;
  }

  @Override
  public Color getEndColor() {
    return this.endColor;
  }
}
