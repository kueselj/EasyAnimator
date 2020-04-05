package cs3500.easyanimator.view;

import javax.swing.*;

/**
 * Interface for an enhanced view. Allows starting, pausing, resuming, disabling/enabling
 * of looping and increasing or decreasing the speed of the animation.
 */
public interface IEnhancedVisualView extends IVisualView {

  /**
   * Starts the animation.
   * @throws IllegalArgumentException if the animation is already started.
   */
  void start();

  /**
   * Pauses the animation.
   * @throws IllegalArgumentException if the animation is already paused.
   */
  void pause();

  /**
   * Resumes the animation
   * @throws IllegalArgumentException if the animation is already running.
   */
  void resume();

  /**
   * Rewinds and restarts the animation from the beginning.
   */
  void restart();

  /**
   * Enables the animation to loop.
   * @throws IllegalArgumentException if the animation is already looping.
   */
  void enableLooping();

  /**
   * Disables looping in the animation.
   * @throws IllegalArgumentException if the animations is already not looping.
   */
  void disableLooping();

  /**
   * Sets the speed of the animation.
   */
  void setSpeed(double speed);
}
