package testmodel.testmotion;

import org.junit.Test;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.WidthHeight;

import static org.junit.Assert.assertEquals;

/**
 * A test suite specifically for our implementation of IMotion in BasicMotion.
 * The majority of these tests are for the linear interpolation.
 */
public class TestBasicMotion {
  private static WidthHeight startSize = new WidthHeight(0, 0);
  private static WidthHeight middleSize = new WidthHeight(50, 50);
  private static WidthHeight endSize = new WidthHeight(100, 100);

  private static Point startPosition = new Point(-1000, 1000);
  private static Point middlePosition = new Point(0, 0);
  private static Point endPosition = new Point(1000, -1000);

  private static Color startColor = new Color(100, 0, 200);
  private static Color middleColor = new Color(50, 50, 100);
  private static Color endColor = new Color(0, 100, 0);

  @Test
  public void testLinearInterpolation() {
    IMotion motion = new BasicMotion(0, 100,
            startSize, endSize,
            startPosition, endPosition,
            startColor, endColor);
    assertEquals("Expected size at tick 0 to equal our starting value.",
            startSize, motion.getSize(0));
    assertEquals("Expected position at tick 0 to equal our starting value.",
            startPosition, motion.getPosition(0));
    assertEquals("Expected color at tick 0 to equal our starting value.",
            startColor, motion.getColor(0));

    assertEquals("Expected size at tick 100 to equal our ending value.",
            endSize, motion.getSize(100));
    assertEquals("Expected position at tick 100 to equal our ending value.",
            endPosition, motion.getPosition(100));
    assertEquals("Expected color at tick 100 to equal our ending value.",
            endColor, motion.getColor(100));

    assertEquals("Expected size at tick 50 to equal the middle values.",
            middleSize, motion.getSize(50));
    assertEquals("Expected position at tick 50 to equal the middle values.",
            middlePosition, motion.getPosition(50));
    assertEquals("Expected color at tick 50 to equal the middle values.",
            middleColor, motion.getColor(50));
  }
}
