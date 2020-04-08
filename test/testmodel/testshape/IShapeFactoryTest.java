package testmodel.testshape;

import org.junit.Test;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.IShapeFactory;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.ShapeNameVisitor;
import cs3500.easyanimator.model.shapes.WidthHeight;

import static org.junit.Assert.assertEquals;

/**
 * A collection of tests for any IShapeFactory. It should be able to create ovals and rectangles.
 */
public abstract class IShapeFactoryTest {
  /**
   * Give an instance of an implementation of our factory.
   * @return  The instance of the factory.
   */
  abstract IShapeFactory getInstance();

  WidthHeight size = new WidthHeight(100, 100);
  Color color = new Color(0, 0, 0);
  Point position = new Point(0, 0);

  /**
   * Test that when attempting to use the method with an invalid shape name that we get an
   * exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidShapeName() {
    getInstance().getShape("Invalid Shape!", size, position, color);
  }

  /**
   * Tests that when attempting to use the method with uninitialized parameters that we get an
   * exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUninitializedArguments() {
    getInstance().getShape(null, size, position, null);
  }

  /**
   * A throw away test rule to check that that the shape name visitor returns a name that can be
   * used over in our factory.
   */
  @Test
  public void testShapeName() {
    IShapeVisitor<String> getName = new ShapeNameVisitor();
    Rectangle rect = new Rectangle(size, position, color);
    Oval oval = new Oval(size, position, color);
    assertEquals("Expected ShapeNameVisitor to return 'rectangle' " +
                    "and to go through our factory.",
            rect,
            getInstance().getShape(rect.accept(getName), size, position, color));

    assertEquals("Expected ShapeNameVisitor to return 'rectangle' " +
                    "and to go through our factory.",
            oval,
            getInstance().getShape(oval.accept(getName), size, position, color));
  }
}
