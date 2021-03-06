package cs3500.easyanimator.model.shapes;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;

/**
 * A BasicShapeFactory allows us to name the basic shapes from version 0. Ovals and Rectangles.
 */
public class BasicShapeFactory implements IShapeFactory {

  @Override
  public IShape getShape(String name, WidthHeight size, Point position, Color color) {
    if (name == null || size == null || color == null || position == null) {
      throw new IllegalArgumentException("Unable to create the given shape with uninitialized" +
              " values.");
    }

    switch (name) {
      case "ellipse": // We tailor it to their descriptions.
      case "oval":
        return new Oval(size, position, color);
      case "rectangle":
        return new Rectangle(size, position, color);
      default :
        throw new IllegalArgumentException(String.format("The specified named shape %s does " +
                "not exist.", name));
    }
  }
}
