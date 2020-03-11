package cs3500.easyanimator.model.shapes;


import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;

/**
 * Abstract base class for implementation of {@link IShape}
 */
public abstract class AbstractShape implements IShape {

  private WidthHeight size;
  private Color color;
  private Point position;

  /**
   * Abstract constructor for a Shape.
   *
   * @param size the size of the Shape.
   * @param color the color of the Shape.
   * @param position the position of the Shape.
   */
  AbstractShape(WidthHeight size, Color color, Point position) {
    this.size = size;
    this.color = color;
    this.position = position;
  }

  @Override
  public WidthHeight getSize() {
    return new WidthHeight(this.size);
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
    this.size = new WidthHeight(width, height);
  }

  @Override
  public void setSize(WidthHeight size) {
    if (size.getWidth() <= 0 || size.getHeight() <= 0) {
      throw new IllegalArgumentException("Width and Height of a shape cannot be zero or below.");
    }
    this.size = new WidthHeight(size);
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
