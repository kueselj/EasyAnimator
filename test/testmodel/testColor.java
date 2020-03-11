package testmodel;
import cs3500.easyanimator.model.Color;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests class for all the methods of the Color class.
 */
public class testColor {
  private Color testColor = new Color(50, 100, 150);

  @Test
  public void testGetRed() {
    assertEquals(50, testColor.getRed());
  }

  @Test
  public void testGetGreen() {
    assertEquals(100, testColor.getGreen());
  }

  @Test
  public void testGetBlue() {
    assertEquals(150, testColor.getBlue());
  }

  @Test
  public void testEquals() {
    Color color1 = new Color(10, 20, 30);
    Color color2 = new Color(10, 20, 31);
    Color color3 = new Color(10, 20, 30);

    assertEquals(color1, color3);
    assertNotEquals(color1, color2);
    assertNotEquals(color2, color3);

  }

}
