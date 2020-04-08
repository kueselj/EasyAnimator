package cs3500.easyanimator.model.shapes;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * A shape factory produces a shape instance for the given IShape parameters.
 */
public class ShapeF implements IShapeVisitor<IShape> {
  private WidthHeight size;
  private Point position;
  private Color color;

  /**
   * Create a new shape.
   * @param size      The size to use.
   * @param position  The position as a point to use.
   * @param color     The color to construct with.
   */
  public ShapeF(WidthHeight size, Point position, Color color) {
    this.size = size;
    this.position = position;
    this.color = color;
  }

  @Override
  public IShape applyToRectangle(Rectangle r) {
    return new Rectangle(size, position, color);
  }

  @Override
  public IShape applyToOval(Oval o) {
    return new Oval(size, position, color);
  }
}
