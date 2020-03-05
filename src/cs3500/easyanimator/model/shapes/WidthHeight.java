package cs3500.easyanimator.model.shapes;

import java.util.Objects;

public class WidthHeight {
  private final int width;
  private final int height;

  public WidthHeight(int width, int height) {
    this.width = width;
    this.height = height;
  }

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


