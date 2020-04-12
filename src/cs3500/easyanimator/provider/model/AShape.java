package cs3500.easyanimator.provider.model;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * An abstraction which represents a Shape.
 */
public abstract class AShape {

  protected final String name;
  protected Pos2D pos;
  protected Color color;
  protected float xSize;
  protected float ySize;

  /**
   * Constructor for an AShape object.
   *
   * @param name  - The name of the shape (ID).
   * @param pos   - The position of the shape as a Pos2D.
   * @param c     - The color of the shape as a java.awt.color.
   * @param xSize - A float representing the width of the shape.
   * @param ySize - A float representing the height of the shape.
   */
  public AShape(String name, Pos2D pos, Color c, float xSize, float ySize) {
    if (name == null || name.equals("")) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    if (pos == null) {
      throw new IllegalArgumentException("Position cannot be null");
    }
    if (c == null) {
      throw new IllegalArgumentException("Color cannot be null");
    }
    if (xSize <= 0 || ySize <= 0) {
      throw new IllegalArgumentException("Size must be greater than 0");
    }
    this.name = name;
    this.pos = pos;
    this.color = c;
    this.xSize = xSize;
    this.ySize = ySize;
  }

  /**
   * Shifts the current position by p.
   *
   * @param p the position to adjust by.
   */
  public void translate(Pos2D p) {
    this.pos.add(p);
  }

  /**
   * Function which will resize the shapes width/height/radius/etc. by the value delta.
   *
   * @param deltaX - The amount to shift the xSize by.
   * @param deltaY - The amount to shift the ySize by.
   */
  public void resize(double deltaX, double deltaY) {
    if (xSize <= -deltaX || ySize <= -deltaY) {
      throw new IllegalArgumentException("Cannot scale by a less than 0");
    }
    this.xSize += deltaX;
    this.ySize += deltaY;
  }

  /**
   * Mutates the color of the shape to the given Color.
   *
   * @param c - The color to update to.
   */
  public void shiftColor(Color c) {
    this.color = c;
  }

  /**
   * A function which will draw the shape based on it's state to the current graphics context.
   *
   * @param g2d     - The 2D graphics context.
   * @param xOffset - The offset that everything should be rendered in on the horizontal plane.
   * @param yOffset - The offset that everything should be rendered in on the vertical plane.
   */
  public abstract void render(Graphics2D g2d, int xOffset, int yOffset);

  /**
   * Simple function which will return a string for a type of shape.
   *
   * @return - A string representing what type of shape this object is.
   */
  public abstract String getShapeType();

  /**
   * Creates a String representing this Shape.
   *
   * @return - A string combining all the local fields of this model.
   */
  public String toString() {
    return getShapeType() + " " + name + " " + pos.toString() + " " + color.toString() + " "
        + xSize + " " + ySize;
  }

  /**
   * A simple method to return the name of this shape.
   *
   * @return a String representing the name of this shape
   */
  public String getName() {
    return this.name;
  }

  /**
   * A simple method to return a copy of this shapes position.
   *
   * @return a Pos2D representing the position of this shape.
   */
  public Pos2D getPos() {
    return new Pos2D(this.pos.getX(), this.pos.getY());
  }

  /**
   * A simple method to return a copy of this shapes color.
   *
   * @return a Color representing the color of this shape.
   */
  public Color getColor() {
    return new Color(this.color.getRGB());
  }

  /**
   * A simple method to return the xSize of this shape.
   *
   * @return a float representing the width of this shape.
   */
  public float getXSize() {
    return this.xSize;
  }

  /**
   * A simple method to return the ySize of this shape.
   *
   * @return a float representing the height of this shape.
   */
  public float getYSize() {
    return this.ySize;
  }

  public void setPos(Pos2D pos) {
    this.pos = pos;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public void setxSize(float xSize) {
    this.xSize = xSize;
  }

  public void setySize(float ySize) {
    this.ySize = ySize;
  }

  /**
   * Converts the shape to its representation in SVG.
   *
   * @return the shape in SVG format
   */
  public abstract String toSVG();

  /**
   * Returns the close of the specific shape in SVG format.
   *
   * @return a close in SVG format
   */
  public abstract String closeSVG();

  /**
   * Returns the width attribute of the shape in SVG format.
   *
   * @return a String representing the width attribute in SVG
   */
  public abstract String getWidthAttribute();

  /**
   * Returns the height attribute of the shape in SVG format.
   *
   * @return a String representing the height attribute in SVG
   */
  public abstract String getHeightAttribute();

  /**
   * Returns the x position attribute of the shape in SVG format.
   *
   * @return a String representing the x attribute in SVG
   */
  public abstract String getXPosSVG();

  /**
   * Returns the y position attribute of the shape in SVG format.
   *
   * @return a String representing the y attribute in SVG
   */
  public abstract String getYPosSVG();
}
