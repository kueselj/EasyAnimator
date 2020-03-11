package cs3500.easyanimator.model.shapes;

/**
 * The interface for the visitor pattern
 * @param <T> The return type for this visitor function.
 */
public interface IShapeVisitor<T> {
  /**
   * Apply the function to a rectangle object.
   * @param r The rectangle to apply code to.
   * @return  The result of the function.
   */
  T applyToRectangle(Rectangle r);

  /**
   * Apply the function oto an oval object.
   * @param o The oval to apply code to.
   * @return  The result of the function.
   */
  T applyToOval(Oval o);
}
