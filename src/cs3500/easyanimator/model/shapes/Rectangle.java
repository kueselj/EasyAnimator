package cs3500.easyanimator.model.shapes;

import java.util.Objects;

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

  /**
   * Changed equals behavior. Returns equals if the rectangles have the same properties.
   * @return
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Rectangle)) {
      return false;
    } else {
      Rectangle otherRectangle = (Rectangle) o;
      return this.size.equals(otherRectangle.size) && this.position.equals(otherRectangle.position)
              && this.color.equals(otherRectangle.color);
    }
  }

  /**
   * Changed hashCode behavior to match equals behavior. Hashcode is based on fields.
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.size, this.position, this.color);
  }
}
