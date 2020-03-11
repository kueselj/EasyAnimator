package cs3500.easyanimator.model;

import java.util.Objects;

/**
 * Represents a point that has an x and a y coordinate.
 */
public final class Point {
  private final int xCoordinate;
  private final int yCoordinate;

  /**
   * Basic constructor for a Point that takes in an x and y coordinate.
   *
   * @param x the x coordinate.
   * @param y the y coordinate.
   */
  public Point(int x, int y) {
    this.xCoordinate = x;
    this.yCoordinate = y;
  }

  public Point(Point point) {
    this.xCoordinate = point.getX();
    this.yCoordinate = point.getY();
  }

  /**
   * Gets the x coordinate of the Point.
   *
   * @return the x coordinate of the Point as an int.
   */
  public int getX() {
    return this.xCoordinate;
  }

  /**
   * Gets the y coordinate of the Point.
   *
   * @return the y coordinate of the Point as an int.
   */
  public int getY() {
    return this.yCoordinate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return false; // Fast pointer comparison.
    }

    if (!(o instanceof Point)) {
      return false; // You know the drill.
    }

    Point otherPoint = (Point) o;
    return otherPoint.xCoordinate == this.xCoordinate &&
            otherPoint.yCoordinate == this.yCoordinate;
  }

  @Override
  public int hashCode() {
    return Objects.hash(xCoordinate, yCoordinate);
  }
}
