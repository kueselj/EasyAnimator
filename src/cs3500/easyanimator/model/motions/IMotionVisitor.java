package cs3500.easyanimator.model.motions;

/**
 * The interface for the visitor pattern for some type of motion.
 * @param <T> The return type of this particular function.
 */
public interface IMotionVisitor<T> {

  /**
   * Apply this function to a basic motion object.
   * @param b The BasicMotion to apply code to.
   * @return  The returned value of this particular function.
   */
  T applyToBasicMotion(BasicMotion b);
}
