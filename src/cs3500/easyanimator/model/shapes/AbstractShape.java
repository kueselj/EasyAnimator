package cs3500.easyanimator.model.shapes;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;

/**
 * Abstract base class for implementation of {@link IShape}. It includes three properties size,
 * color, and position.
 */
public abstract class AbstractShape implements IShape {

  protected WidthHeight size;
  protected Color color;
  protected Point position;

  /**
   * Abstract constructor for a Shape.
   *  @param size the size of the Shape.
   * @param position the position of the Shape.
   * @param color the color of the Shape.
   */
  AbstractShape(WidthHeight size, Point position, Color color) {
    this.size = size;
    this.color = color;
    this.position = position;
  }

  @Override
  public WidthHeight getSize() {
    return this.size;
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
  public void setSize(WidthHeight size) {
    if (size.getWidth() <= 0 || size.getHeight() <= 0) {
      throw new IllegalArgumentException("Width and Height of a shape cannot be zero or below.");
    }
    this.size = size;
  }

  @Override
  public void setColor(Color color) {
    this.color = color;

  }

  @Override
  public void setPosition(Point position) {
    this.position = position;
  }
}
