package cs3500.easyanimator.provider.model;

import cs3500.easyanimator.model.Point;

import java.util.Objects;

public class Pos2D {
  private int xCoordinate;
  private int yCoordinate;

  /**
   * Basic constructor for a Point that takes in an x and y coordinate.
   *
   * @param x the x coordinate.
   * @param y the y coordinate.
   */
  public Pos2D(int x, int y) {
    this.xCoordinate = x;
    this.yCoordinate = y;
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

  /**
   * Shifts the Position by the given position
   * @param p the shift position.
   */
  public void add(Pos2D p) {
    this.xCoordinate += p.xCoordinate;
    this.yCoordinate += p.yCoordinate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true; // Fast pointer comparison.
    }

    if (!(o instanceof Pos2D)) {
      return false; // You know the drill.
    }

    Pos2D otherPoint = (Pos2D) o;
    return otherPoint.xCoordinate == this.xCoordinate &&
            otherPoint.yCoordinate == this.yCoordinate;
  }

  @Override
  public int hashCode() {
    return Objects.hash(xCoordinate, yCoordinate);
  }
}

