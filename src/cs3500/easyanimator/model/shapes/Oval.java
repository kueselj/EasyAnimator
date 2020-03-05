package cs3500.easyanimator.model.shapes;

import java.awt.*;

/**
 * A class representing an Oval Shape.
 */
public class Oval extends AbstractShape {

  /**
   * Abstract constructor for a Shape.
   *
   * @param size    the size of the Shape.
   * @param color    the color of the Shape.
   * @param position the position of the Shape.
   */
  public Oval(WidthHeight size, Color color, Point position) {
    super(size, color, position);
  }
}
