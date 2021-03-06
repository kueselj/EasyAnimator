package cs3500.easyanimator.model.shapes;

import java.util.Objects;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;

/**
 * A class representing an Oval Shape.
 */
public class Oval extends AbstractShape {

  /**
   * Abstract constructor for a Shape.
   *  @param size    the size of the Shape.
   * @param position the position of the Shape.
   * @param color    the color of the Shape.
   */
  public Oval(WidthHeight size, Point position, Color color) {
    super(size, position, color);
  }

  @Override
  public <T> T accept(IShapeVisitor<T> visitor) {
    return visitor.applyToOval(this);
  }

  @Override
  public IShape copy() {
    return new Oval(this.size, this.position, this.color);
  }

  /**
   * Changed equals behavior. Returns equals if the ovals have the same properties.
   * @return
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Oval)) {
      return false;
    } else {
      Oval otherOval = (Oval) o;
      return this.size.equals(otherOval.size) && this.position.equals(otherOval.position)
              && this.color.equals(otherOval.color);
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
