package cs3500.easyanimator.model.shapes;

/**
 * A ShapeNameVisitor returns the names for a shape that match with the shape factory input.
 */
public class ShapeNameVisitor implements IShapeVisitor<String> {

  @Override
  public String applyToRectangle(Rectangle r) {
    return "rectangle";
  }

  @Override
  public String applyToOval(Oval o) {
    return "oval";
  }
}
