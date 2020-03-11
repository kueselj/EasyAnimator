package cs3500.easyanimator.model.shapes;

import java.util.Objects;

/**
 * A class representing a WidthHeight, which has two fields, one for a width, and one for a height.
 */
public class WidthHeight {
  private final int width;
  private final int height;

  /**
   * Basic constructor for a WidthHeight that takes in a width and a height.
   * @param width an int representing the width.
   * @param height an int representing the height.
   */
  public WidthHeight(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Copy constructor for a WidthHeight, takes in another WidthHeight.
   *
   * @param size the WidthHeight to copy.
   */
  public WidthHeight(WidthHeight size) {
    this.width = size.width;
    this.height = size.height;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  @Override
  public boolean equals(Object other) {
    //if they are same exact object they are equal
    if (this == other) {
      return true;
    }

    //if not an instance of another WidthHeight, the two can't be equal!
    else if (!(other instanceof WidthHeight)) {
      return false;
    }

    //cast other object to a WidthHeight.
    WidthHeight otherCard = (WidthHeight) other;

    //if they have the same value and suit they are the same.
    return (this.width == otherCard.width && this.height == otherCard.height);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.width, this.height);
  }
}


