package cs3500.easyanimator.model.shapes;

import cs3500.easyanimator.model.shapes.AbstractShape;

import java.awt.*;

/**
 * A class representing an Oval Shape.
 */
public class Oval extends AbstractShape {

  /**
   * Abstract constructor for a Shape.
   *
   * @param width    the width of the Shape.
   * @param height   the height of the Shape.
   * @param color    the color of the Shape.
   * @param position the position of the Shape.
   */
  public Oval(int width, int height, Color color, Point position) {
    super(width, height, color, position);
  }
}
