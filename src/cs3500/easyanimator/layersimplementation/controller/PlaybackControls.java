package cs3500.easyanimator.layersimplementation.controller;

/**
 * Defines Playback controls that can be used.
 */
public interface PlaybackControls {
  /**
   * Starts the animation.
   */
  void play();

  /**
   * Pauses the animation.
   */
  void pause();

  /**
   * Restarts the animation.
   */
  void restart();

  /**
   * Toggles looping on the animation.
   */
  void toggleLooping();

  /**
   * Increases the speed of the animation.
   */
  void increaseSpeed();

  /**
   * Decreases the speed of the animation.
   */
  void decreaseSpeed();

  /**
   * Increases the tick of the animation.
   */
  void increaseTick();

  /**
   * Decreases the tick of the animation.
   */
  void decreaseTick();
}
