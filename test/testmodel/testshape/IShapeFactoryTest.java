package testmodel.testshape;

import org.junit.Test;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.IShapeFactory;
import cs3500.easyanimator.model.shapes.WidthHeight;

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
    getInstance().getShape("Invalid Shape!", size, color, position);
  }

  /**
   * Tests that when attempting to use the method with uninitialized parameters that we get an
   * exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testUninitializedArguments() {
    getInstance().getShape(null, size, null, position);
  }
}
