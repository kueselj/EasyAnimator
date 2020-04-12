package cs3500.easyanimator.provider.controller;

import javax.swing.*;

import cs3500.easyanimator.provider.model.IAnimatorModel;
import cs3500.easyanimator.provider.views.AnimationEditView;
import cs3500.easyanimator.provider.views.IAnimationViewer;

/**
 * An implementation of a controller that we think accomplishes what the provided code needs.
 * This contains a mechanism to continually refresh the view with the draw method call and react
 * to different button presses to adjust that playback.
 */
public class AnimatorController {
  IAnimatorModel model;
  IAnimationViewer view;
  Timer timer;
  boolean loop;
  int tick;

  /**
   * Creates a new AnimatorController to run the provider editor view that has controls for
   * playback (unfortunately no editing features).
   * @param model The model to create the controller around initially.
   * @param speed The default speed in ticks per second to use to update the view and model.
   */
  public AnimatorController(AnimatorModel model, double speed) {
    this.view = new AnimationEditView(this, model);
    this.timer = new Timer((int) (1.0 / speed * 1000), e -> this.refresh());
    this.tick = 0;
    this.loop = false;
    this.timer.start();
  }

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
  public void setupButtonCallbacks(AbstractButton start,
                            AbstractButton restart,
                            AbstractButton pause,
                            AbstractButton resume,
                            AbstractButton looping,
                            JSlider speedSlider) {
    start.addActionListener(e -> {
      if (tick == 0) {
        timer.start();
      }
    });
    pause.addActionListener(e -> timer.stop());
    restart.addActionListener(e -> {
      timer.stop();
      tick = 0;
      timer.restart();
    });
    resume.addActionListener(e -> {
      if (tick != 0) {
        timer.start();
      }
    });
    looping.addActionListener(e -> loop = looping.isSelected());
    speedSlider.addChangeListener(e -> timer.setDelay((int) (1.0 / speedSlider.getValue() * 1000)));
  }

  /**
   * A private helper method dedicated to refreshing the view and model every tick.
   */
  private void refresh() {
    tick += 1;

    // NEED MODEL INTERPRETATION OF TIME.

    view.draw();
  }
}