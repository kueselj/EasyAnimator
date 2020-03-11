package cs3500.easyanimator.model;

/**
 * Class representing a color as a RGB value. All values must be in range of 0-255.
 */
public final class Color {

  private final int red;
  private final int green;
  private final int blue;

  /**
   * Basic constructor for a color, all values must be in range of 0-255.
   *
   * @param r an int representing the level of red in the color.
   * @param g an int representing the level of green in the color.
   * @param b an int representing the level of green in the color.
   *
   * @throws IllegalArgumentException if any color value is not in range of 0-255.
   */
  public Color(int r, int g, int b) {

    //if any color value not in range of 0-255 throw an exception.
    if (r < 0 || g < 0 || b < 0 || r > 255 || g > 255 || b > 255) {
      throw new IllegalArgumentException("All color values must be in range of 0-255");
    }

    this.red = r;
    this.green = g;
    this.blue = b;
  }

  /**
   * Gets the red value of the color.
   * @return an int representing the red value of the color.
   */
  public int getRed() {
    return this.red;
  }

  /**
   * Gets the green value of the color.
   * @return an int representing the green value of the color.
   */
  public int getGreen() {
    return this.green;
  }

  /**
   * Gets the blue value of the color.
   * @return an int representing the blue value of the color.
   */
  public int getBlue() {
    return this.blue;
  }
}
