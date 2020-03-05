package cs3500.easyanimator.model.shapes;

import java.awt.Color;
import java.awt.Point;

/**
 * Interface representing a Shape.
 */
public interface IShape {

  /**
   * Gets the size of the Shape.
   *
   * @return a widthHeight representing the size of the Shape.
   */
  WidthHeight getSize();

  /**
   * Gets the color of the shape.
   * @return a Color representing the color of the Shape.
   */
  Color getColor();

  /**
   * Gets the position of the Shape.
   * The x coordinate of the point represents the x coordinate of the Shape.
   * The y coordinate of the point represents the y coordinate of the Shape.
   * The point represents the position of the shape from the center of the shape.
   *
   * @return the position of the shape as a Point.
   */
  Point getPosition();

  /**
   * Sets the size of the Shape.
   *
   * @param width an int to set as the Shape's width.
   * @param height an int to set as the Shape's height.
   *
   * @throws IllegalArgumentException if the width or height is set to equal or below zero.
   */
  void setSize(int width, int height);

  /**
   * Sets the size of the Shape.
   *
   * @param size a widthHeight to set as the Shape's size.
   *
   * @throws IllegalArgumentException if the width or height is set to equal or below zero.
   */
  void setSize(WidthHeight size);

  /**
   * Sets the color of the Shape.
   *
   * @param color the Color to set as the Shapes color.
   *
   * @throws IllegalArgumentException if the Color is invalid or null.
   */
  void setColor(Color color);

  /**
   * Sets the position of the Shape.
   *
   * @param position the position of the Shape represented as a Point.
   *
   * @throws IllegalArgumentException  if the Point is invalid or null.
   */
  void setPosition(Point position);

  /**
   * Sets the position of the Shape given an x and a y coordinate;
   *
   * @param x the x coordinate of the Shape.
   * @param y the y coordinate of the Shape.
   */
  void setPosition(int x, int y);
}
