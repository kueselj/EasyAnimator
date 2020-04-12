package cs3500.easyanimator.provider.model;

/**
 * An interface which defines the behavior expected within the model for an Animator.
 * It keeps track of four things. The shapes that are in the animation, the animations in the
 * animation, a log of the animations that have happened and the time in the animation. It has
 * methods for adding shapes and animations, advancing the animation one tick, running the complete
 * animation and outputting the log of the animation.
 */
public interface IAnimatorModel {

  /**
   * Adds a shape into the list of shapes.
   *
   * @param shape - The shape to insert.
   */
  void addShape(AShape shape);

  /**
   * Removes the shape with the given name from the animation.
   *
   * @param name the name of the shape
   */
  void removeShape(String name);

  /**
   * Used to add a new animation to the model.
   *
   * @param a - The animation being added.
   */
  void addAnimation(AAnimation a);

  /**
   * Removes the given animation from the model.
   *
   * @param a the animation to remove
   */
  void removeAnimation(AAnimation a);

  /**
   * Generates a string representing the important frames of the animation. Outputted text should
   * represent the entirety of the animation in a compact manner.
   */
  String getTextualAnimationView();

  /**
   * Updates the worlds state by one 'unit'.
   */
  void tick(int time);

  /**
   * Returns if the model has any animations in progress or not.
   *
   * @return a boolean representing if there is more animation left or not.
   */
  boolean finished(int time);

  /**
   * Adds a new keyframe to the animation.
   *
   * @param target the affected shape
   * @param s the keyframe in String format
   */
  void addKeyFrame(AShape target, String s);
}
