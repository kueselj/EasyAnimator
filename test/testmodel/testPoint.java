package testmodel;
import cs3500.easyanimator.model.Point;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class that test all the methods of the Point class.
 */
public class testPoint {
  private Point testPoint = new Point(5, 10);

  @Test
  public void testGetX() {
    assertEquals(5, testPoint.getX());
  }

  @Test
  public void testGetY() {
    assertEquals(10, testPoint.getY());
  }
}
