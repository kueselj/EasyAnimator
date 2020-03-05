package testmodel.testmotion;

import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.WidthHeight;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.*;

/**
 * Test class for Motion methods.
 * TODO: may want to make it abstract if we add more types of motions in the future.
 */
public class MotionTest {

  private IMotion testMotion;
  private WidthHeight startSize = new WidthHeight(10, 20);
  private WidthHeight endSize = new WidthHeight(6, 25);
  private Point startPosition = new Point(10, 15);
  private Point endPosition = new Point(5, 60);
  private Color startColor = new Color(10, 20, 30);
  private Color endColor = new Color(25, 35, 45);

  /**
   * Resets/Initializes the testMotion.
   */
  private void reset() {
    this.testMotion = new BasicMotion(10, 70,
            startSize, endSize,
            startPosition, endPosition,
            startColor, endColor);
  }

  @Test
  public void testGetStartTime() {
    this.reset();
    assertEquals(10, testMotion.getStartTime());
  }

  @Test
  public void testGetEndTime() {
    this.reset();
    assertEquals(70, testMotion.getEndTime());
  }

  @Test
  public void testGetStartPosition() {
    this.reset();
    assertEquals(new Point(10, 15), testMotion.getStartPosition());
  }

  @Test
  public void testGetEndPosition() {
    this.reset();
    assertEquals(new Point(5, 60), testMotion.getEndPosition());
  }

  @Test
  public void testGetStartSize() {
    this.reset();
    assertEquals(new WidthHeight(10, 20), testMotion.getStartSize());
  }

  @Test
  public void testGetEndSize() {
    this.reset();
    assertEquals(new WidthHeight(6, 25), testMotion.getEndSize());
  }

  @Test
  public void testGetStartColor() {
    this.reset();
    assertEquals(new Color(10, 20, 30), testMotion.getStartColor());
  }

  @Test
  public void testGetEndColor() {
    this.reset();
    assertEquals(new Color(25, 35, 45), testMotion.getEndColor());
  }
}
