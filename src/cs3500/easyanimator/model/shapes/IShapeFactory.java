package cs3500.easyanimator.model.shapes;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;

/**
 * An IShapeFactory is a class like the View factory used to make a shape.
 */
public interface IShapeFactory {
  /**
   * Give an instance of an IShape with the given name.
   * @param name      The name of the shape to fetch. Implementations should support oval, rectangle.
   * @param size      The size of the shape to create.
   * @param color     The color of the shape to create.
   * @param position  The position of the shape.
   * @throws IllegalArgumentException If the given name is not a shape.
   *                                  Or if any of the parameters are uninitialized.
   * @return
   */
  IShape getShape(String name, WidthHeight size, Color color, Point position);
}
