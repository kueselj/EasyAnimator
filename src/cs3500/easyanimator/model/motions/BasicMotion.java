package cs3500.easyanimator.model.motions;

import java.util.Objects;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * Class representing a basic motion, has a start time, end time,
 * start size, end size, start position, end position, start color, and end color.
 * The class is immutable, once initialized it cannot be changed.
 */
public final class BasicMotion implements IMotion {

  private final int startTime;
  private final int endTime;

  private final WidthHeight startSize;
  private final WidthHeight endSize;

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
                     WidthHeight startSize, WidthHeight endSize,
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
    return this.startPosition;
  }

  @Override
  public Point getEndPosition() {
    return this.endPosition;
  }

  @Override
  public WidthHeight getStartSize() {
    return this.startSize;
  }

  @Override
  public WidthHeight getEndSize() {
    return this.endSize;
  }

  @Override
  public Color getStartColor() {
    return this.startColor;
  }

  @Override
  public Color getEndColor() {
    return this.endColor;
  }

  @Override
  public String toString() {
    //TODO compile a list of ints and use deliminator instead of this monstrosity. or visitor
    return this.startTime + " "
            + this.startPosition.getX() + " "
            + this.startPosition.getY() + " "
            + this.startSize.getWidth() + " "
            + this.startSize.getHeight() + " "
            + this.startColor.getRed() + " "
            + this.startColor.getGreen() + " "
            + this.startColor.getBlue() + " "
            + this.endTime + " "
            + this.endPosition.getX() + " "
            + this.endPosition.getY() + " "
            + this.endSize.getWidth() + " "
            + this.endSize.getHeight() + " "
            + this.endColor.getRed() + " "
            + this.endColor.getGreen() + " "
            + this.endColor.getBlue();
  }

  @Override
  public <T> T accept(IMotionVisitor<T> motion) {
    return motion.applyToBasicMotion(this);
  }

  // To give the position at a certain time tick, we use linear interpolation.

  @Override
  public Point getPosition(int tick) {
    return new Point(IMotion.tween(startPosition.getX(), startTime,
            endPosition.getX(), endTime, tick),
            IMotion.tween(startPosition.getY(), startTime,
                    endPosition.getY(), endTime, tick));
  }

  @Override
  public WidthHeight getSize(int tick) {
    return new WidthHeight(IMotion.tween(startSize.getWidth(), startTime,
            endSize.getWidth(), endTime, tick),
            IMotion.tween(startSize.getHeight(), startTime,
                    endSize.getHeight(), endTime, tick));
  }

  @Override
  public Color getColor(int tick) {
    // Renaming just to make the next a little smaller.
    return new Color(IMotion.tween(startColor.getRed(), startTime,
            endColor.getRed(), endTime, tick),
            IMotion.tween(startColor.getGreen(), startTime,
                    endColor.getGreen(), endTime, tick),
            IMotion.tween(startColor.getBlue(), startTime,
                    endColor.getBlue(), endTime, tick));
  }

  /**
   * We override the existing equals behavior so that it is equals if the fields match.
   * @param o   The object to compare to.
   * @return    True if the objects are equal otherwise false.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof BasicMotion)) {
      return false;
    } else {
      BasicMotion otherMotion = (BasicMotion) o;
      return otherMotion.startTime == this.startTime &&
              otherMotion.endTime == this.endTime &&
              otherMotion.startSize == this.startSize &&
              otherMotion.endSize == this.endSize &&
              otherMotion.startPosition == this.startPosition &&
              otherMotion.endPosition == this.endPosition &&
              otherMotion.startColor == this.startColor &&
              otherMotion.endColor == this.endColor;
    }
  }

  /**
   * Overrides the default hashcode implementation to be compatible with equal, field comparison.
   * @return  A integer hashcode.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.startTime, this.endTime,
            this.startSize,this.endSize,
            this.startPosition, this.endPosition,
            this.startColor, this.endColor);
  }
}
