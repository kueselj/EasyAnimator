package testmodel;

import cs3500.easyanimator.model.Point;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test class that test all the methods of the Point class.
 */
class TestPoint {
  private Point testPoint = new Point(5, 10);

  @Test
  public void testGetX() {
    assertEquals(5, testPoint.getX());
  }

  @Test
  public void testGetY() {
    assertEquals(10, testPoint.getY());
  }

  @Test
  public void testEquals() {
    Point point1 = new Point(10, 20);
    Point point2 = new Point(11, 21);
    Point point3 = new Point(10, 20);

    assertEquals(point1, point3);
    assertNotEquals(point1, point2);
    assertNotEquals(point2, point3);
  }
}
