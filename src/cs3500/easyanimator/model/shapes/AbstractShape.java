package cs3500.easyanimator.model.shapes;

import java.awt.*;

/**
 * Abstract base class for implementation of {@link IShape}
 */
public abstract class AbstractShape implements IShape {

  private int width;
  private int height;
  private Color color;
  private Point position;

  /**
   * Abstract constructor for a Shape.
   *
   * @param width the width of the Shape.
   * @param height the height of the Shape.
   * @param color the color of the Shape.
   * @param position the position of the Shape.
   */
  AbstractShape(int width, int height, Color color, Point position) {
    this.width = width;
    this.height = height;
    this.color = color;
    this.position = position;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public Point getPosition() {
    return this.position;
  }

  @Override
  public void setSize(int width, int height) {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("Width and Height of a shape cannot be zero or below.");
    }
    this.width = width;
    this.height = height;
  }

  @Override
  public void setColor(Color color) {
    this.color = color;

  }

  @Override
  public void setPosition(Point position) {
    this.position = position;
  }

  @Override
  public void setPosition(int x, int y) {
    this.position = new Point(x, y);
  }
}
