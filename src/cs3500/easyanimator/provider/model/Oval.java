package cs3500.easyanimator.provider.model;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A class which represents an Oval. A child class of an AShape.
 */
public class Oval extends AShape {

  /**
   * Constructor for a rectangle object.
   *
   * @param name  - The name of the oval (ID).
   * @param pos   - The position of the oval.
   * @param color - The color of this oval.
   * @param xSize - The width of this oval.
   * @param ySize - The height of this oval.
   */
  public Oval(String name, Pos2D pos, Color color, float xSize, float ySize) {
    super(name, pos, color, xSize, ySize);
  }

  @Override
  public void render(Graphics2D g2d, int xOffset, int yOffset) {
    g2d.setColor(this.color);
    g2d.fillOval((int) this.pos.getX() + xOffset, (int) this.pos.getY() + yOffset, (int) xSize,
        (int) ySize);
  }

  @Override
  public String getShapeType() {
    return "Oval";
  }

  @Override
  public String toSVG() {
    return "<ellipse id=\"" + this.name
        + "\" cx=\"" + this.pos.getX()
        + "\" cy=\"" + this.pos.getY()
        + "\" rx=\"" + this.xSize
        + "\" ry=\"" + this.ySize
        + "\" fill=\"rgb(" + this.color.getRed()
        + "," + this.color.getGreen() + "," + this.color.getBlue()
        + ")\" visibility=\"visible\"> \n";
  }

  @Override
  public String closeSVG() {
    return "</ellipse>\n\n";
  }

  @Override
  public String getWidthAttribute() {
    return "rx";
  }

  @Override
  public String getHeightAttribute() {
    return "ry";
  }

  @Override
  public String getXPosSVG() {
    return "cx";
  }

  @Override
  public String getYPosSVG() {
    return "cy";
  }
}
