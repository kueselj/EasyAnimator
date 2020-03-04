package testmodel.testshape;

import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.Color;
import java.awt.Point;

/**
 * Tests for {@link IShape}s.
 */
public abstract class AbstractShapeTest {

  private IShape testShape;
  private static final Color testShapeColor = new Color(100, 100, 100);
  private final Point testShapePosition = new Point(50, 100);

  /**
   * Resets the testShape
   */
  private void reset() {
    this.testShape = rec(3, 10, this.testShapeColor, this.testShapePosition);
  }

  @Test
  public void testGetWidth() {
    this.reset();
    assertEquals(3, testShape.getWidth());
  }

  @Test
  public void testGetHeight() {
    this.reset();
    assertEquals(10, testShape.getHeight());
  }

  @Test
  public void testGetColor() {
    this.reset();
    assertEquals(new Color(100, 100, 100), testShape.getColor());
  }

  @Test
  public void testGetPosition() {
    this.reset();
    assertEquals(new Point(50, 100), testShape.getPosition());
  }

  @Test
  public void testSetSize() {
    this.reset();
    assertEquals(3, testShape.getWidth());
    assertEquals(10, testShape.getHeight());
    testShape.setSize(15, 20);
    assertEquals(15, testShape.getWidth());
    assertEquals(20, testShape.getHeight());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSetSizeWidth() {
    this.reset();
    testShape.setSize(0, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSetSizeHeight() {
    this.reset();
    testShape.setSize(10, -10);
  }

  @Test
  public void testSetColor() {
    this.reset();
    assertEquals(new Color(100, 100, 100), testShape.getColor());
    testShape.setColor(new Color(200, 50, 150));
    assertEquals(new Color(200, 50, 150), testShape.getColor());

  }

  @Test
  public void testSetPositionUsingPoint() {
    this.reset();
    assertEquals(new Point(50, 100), testShape.getPosition());
    testShape.setPosition(new Point(150, 175));
    assertEquals(new Point(150, 175), testShape.getPosition());
  }

  @Test
  public void testSetPositionUsingXY() {
    this.reset();
    assertEquals(new Point(50, 100), testShape.getPosition());
    testShape.setPosition(150, 175);
    assertEquals(new Point(150, 175), testShape.getPosition());
  }


  /**
   * Constructs an instance of the class under test representing the Shape
   * given as a Rectangle.
   *
   * @param width the width of the rectangle.
   * @param height the height of the rectangle.
   * @param color the color of the rectangle.
   * @param position the position of the rectangle.
   *
   * @return an instance of the class under test
   */
  protected abstract IShape rec(int width, int height, Color color, Point position);

  /**
   * Constructs an instance of the class under test representing the Shape
   * given as an Oval.
   *
   * @param width the width of the oval.
   * @param height the height of the oval.
   * @param color the color of the oval.
   * @param position the position of the oval.
   *
   * @return an instance of the class under test.
   */
  protected abstract IShape oval(int width, int height, Color color, Point position);

  /**
   * Concrete class for testing the Rectangle implementation of IShape.
   */
  public static final class RectangleShapeTest extends AbstractShapeTest {

    @Override
    protected IShape rec(int width, int height, Color color, Point position) {
      return new Rectangle(width, height, color, position);
    }

    @Override
    protected IShape oval(int width, int height, Color color, Point position) {
      return new Rectangle(width, height, color, position);
    }

    /**
     * Concrete class for testing the Oval implementation of IShape.
     */
    public static final class OvalShapeTest extends AbstractShapeTest {

      @Override
      protected IShape rec(int width, int height, Color color, Point position) {
        return new Oval(width, height, color, position);
      }

      @Override
      protected IShape oval(int width, int height, Color color, Point position) {
        return new Oval(width, height, color, position);
      }
    }
  }
}
