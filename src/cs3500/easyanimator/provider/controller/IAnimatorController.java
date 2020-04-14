package cs3500.easyanimator.provider.controller;

import javax.swing.AbstractButton;
import javax.swing.JSlider;

/**
 * Interface for the provider controller. The controller sets up the action listeners for the
 * buttons within the view component.
 */
public interface IAnimatorController {

  /**
   * Add listeners for the following buttons presses in the client's view to correctly control the
   * timer.
   * @param start       The button to start the timer for playback.
   * @param restart     The button to restart the current tick of playback.
   * @param pause       The button to pause the current tick of playback.
   * @param resume      The button to resume the timer if stopped.
   * @param looping     The button to toggle if the timer resets at the end of animation and
   *                    keeps playing.
   * @param speedSlider A component to control the speed of animation playback in TPS.
   */
  void setupButtonCallbacks(AbstractButton start,
                                   AbstractButton restart,
                                   AbstractButton pause,
                                   AbstractButton resume,
                                   AbstractButton looping,
                                   JSlider speedSlider);
}
