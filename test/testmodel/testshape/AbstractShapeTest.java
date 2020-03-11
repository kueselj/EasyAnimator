package testmodel.testshape;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;



/**
 * Tests for {@link IShape}s.
 */
public abstract class AbstractShapeTest {

  private IShape testShape;
  private WidthHeight testSize = new WidthHeight(3, 10);
  private static final Color testShapeColor = new Color(100, 100, 100);
  private final Point testShapePosition = new Point(50, 100);

  /**
   * Resets/Initializes the testShape.
   */
  private void reset() {
    this.testShape = rec(this.testSize, this.testShapeColor, this.testShapePosition);
  }

  @Test
  public void testGetSize() {
    this.reset();
    assertEquals(new WidthHeight(3, 10), testShape.getSize());
  }

  @Test
  public void testGetSizeWidth() {
    this.reset();
    assertEquals(3, testShape.getSize().getWidth());
  }

  @Test
  public void testGetSizeHeight() {
    this.reset();
    assertEquals(10, testShape.getSize().getHeight());
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
    assertEquals(3, testShape.getSize().getWidth());
    assertEquals(10, testShape.getSize().getHeight());
    assertEquals(new WidthHeight(3, 10), testShape.getSize());
    testShape.setSize(15, 20);
    assertEquals(15, testShape.getSize().getWidth());
    assertEquals(20, testShape.getSize().getHeight());
    assertEquals(new WidthHeight(15, 20), testShape.getSize());
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
   * @param size the size of the rectangle.
   * @param color the color of the rectangle.
   * @param position the position of the rectangle.
   *
   * @return an instance of the class under test
   */
  protected abstract IShape rec(WidthHeight size, Color color, Point position);

  /**
   * Constructs an instance of the class under test representing the Shape
   * given as an Oval.
   *
   * @param size the width of the oval.
   * @param color the color of the oval.
   * @param position the position of the oval.
   *
   * @return an instance of the class under test.
   */
  protected abstract IShape oval(WidthHeight size, Color color, Point position);

  /**
   * Concrete class for testing the Rectangle implementation of IShape.
   */
  public static final class RectangleShapeTest extends AbstractShapeTest {

    @Override
    protected IShape rec(WidthHeight size, Color color, Point position) {
      return new Rectangle(size, color, position);
    }

    @Override
    protected IShape oval(WidthHeight size, Color color, Point position) {
      return new Rectangle(size, color, position);
    }

    /**
     * Concrete class for testing the Oval implementation of IShape.
     */
    public static final class OvalShapeTest extends AbstractShapeTest {

      @Override
      protected IShape rec(WidthHeight size, Color color, Point position) {
        return new Oval(size, color, position);
      }

      @Override
      protected IShape oval(WidthHeight size, Color color, Point position) {
        return new Oval(size, color, position);
      }
    }
  }
}
