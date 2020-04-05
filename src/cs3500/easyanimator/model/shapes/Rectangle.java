package cs3500.easyanimator.model.shapes;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;

/**
 * A class representing a Rectangle Shape.
 */
public class Rectangle extends AbstractShape {

  /**
   * Abstract constructor for a Shape.
   *  @param size    the size of the Shape.
   * @param position the position of the Shape.
   * @param color    the color of the Shape.
   */
  public Rectangle(WidthHeight size, Point position, Color color) {
    super(size, position, color);
  }

  @Override
  public <T> T accept(IShapeVisitor<T> visitor) {
    return visitor.applyToRectangle(this);
  }

  @Override
  public IShape copy() {
    return new Rectangle(this.size, this.position, this.color);
  }
}
