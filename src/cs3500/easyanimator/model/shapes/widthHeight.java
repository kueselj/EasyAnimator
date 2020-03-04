package cs3500.easyanimator.model.shapes;

public class widthHeight {
  private final int width;
  private final int height;

  public widthHeight(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public widthHeight(widthHeight size) {
    this.width = size.width;
    this.height = size.height;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

}
