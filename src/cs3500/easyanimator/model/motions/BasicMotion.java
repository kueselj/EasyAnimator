package cs3500.easyanimator.model.motions;



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
    return new Point(this.startPosition);
  }

  @Override
  public Point getEndPosition() {
    return new Point(this.endPosition);
  }

  @Override
  public WidthHeight getStartSize() {
    return new WidthHeight(this.startSize);
  }

  @Override
  public WidthHeight getEndSize() {
    return new WidthHeight(this.endSize);
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
    //TODO compile a list of ints and use deliminator instead of this monstrosity.
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
}
