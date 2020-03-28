package cs3500.easyanimator.view;

import cs3500.easyanimator.model.IAnimatorModelViewOnly;

/**
 * A view interface for an IAnimatorModel, displays the animation. Behaviors that are not applicable
 * for the particular implementation of the view should be suppressed and simply do nothing when
 * called upon.
 */
public interface IAnimatorView {
  /**
   * Make the view visible to start the session. Weirdly works for both text and visual views.
   * @throws IllegalStateException  If the model was not previously set.
   */
  void makeVisible();

  /**
   * Sets the instance of the model the view interacts with.
   * @param model   The model for the view to communicate with.
   * @throws IllegalArgumentException If the model is uninitialized.
   */
  void setModel(IAnimatorModelViewOnly model);

  /**
   * Sets the "speed" for the given view if applicable.
   * @param speed   The ticks per second of the animation. Since the model understands animation
   *                in terms of unitless ticks we need to convert to seconds.
   * @throws IllegalArgumentException if the speed is below 0.
   */
  void setSpeed(double speed);
}
