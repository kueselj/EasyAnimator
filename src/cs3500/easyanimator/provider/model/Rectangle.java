package cs3500.easyanimator.provider.model;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A class which represents a rectangle. A child class of an AShape.
 */
public class Rectangle extends AShape {

  /**
   * Constructor for a rectangle object.
   *
   * @param name  - The name of the rectangle (ID).
   * @param pos   - The position of the rectangle.
   * @param color - The color of this rectangle.
   * @param xSize - The width of this rectangle.
   * @param ySize - The height of this rectangle.
   */
  public Rectangle(String name, Pos2D pos, Color color, float xSize, float ySize) {
    super(name, pos, color, xSize, ySize);
  }

  @Override
  public void render(Graphics2D g2d, int xOffset, int yOffset) {
    g2d.setColor(this.color);
    g2d.fillRect((int) this.pos.getX() + xOffset, (int) this.pos.getY() + yOffset, (int) this.xSize,
        (int) this.ySize);
  }

  @Override
  public String getShapeType() {
    return "Rectangle";
  }

  @Override
  public String toSVG() {
    return "<rect id=\"" + this.name
        + "\" x=\"" + this.pos.getX()
        + "\" y=\"" + this.pos.getY()
        + "\" width=\"" + this.xSize
        + "\" height=\"" + this.ySize
        + "\" fill=\"rgb(" + this.color.getRed()
        + "," + this.color.getGreen() + "," + this.color.getBlue()
        + ")\" visibility=\"visible\">\n";
  }

  @Override
  public String closeSVG() {
    return "</rect>\n\n";
  }

  @Override
  public String getWidthAttribute() {
    return "width";
  }

  @Override
  public String getHeightAttribute() {
    return "height";
  }

  @Override
  public String getXPosSVG() {
    return "x";
  }

  @Override
  public String getYPosSVG() {
    return "y";
  }
}
