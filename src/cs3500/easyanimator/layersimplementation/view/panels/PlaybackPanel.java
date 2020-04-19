package cs3500.easyanimator.layersimplementation.view.panels;

import cs3500.easyanimator.layersimplementation.controller.PlaybackControls;

import javax.swing.*;
import java.awt.*;

/**
 * Panel only consisting of the playback control features.
 */
public class PlaybackPanel extends JPanel {

  private JButton play;
  private JButton pause;
  private JButton restart;
  private JButton toggleLooping;
  private JButton speedUp;
  private JButton speedDown;
  private JButton tickUp;
  private JButton tickDown;

  private JLabel tickLabel;

  private static Dimension MAX_PLAYBACK_BUTTONS_SIZE = new Dimension(Integer.MAX_VALUE, 50);

  /**
   * A panel that handles holding the playback control components.
   */
  public PlaybackPanel() {
    setLayout(new GridLayout());
    setBackground(new Color(106, 35, 38));
    setMaximumSize(MAX_PLAYBACK_BUTTONS_SIZE);

    //play button
    play = new JButton("Play");
    play.setActionCommand("Play");
    add(play);

    //pause button
    pause = new JButton("Pause");
    pause.setActionCommand("Pause");
    add(pause);

    //restart button
    restart = new JButton("Restart");
    restart.setActionCommand("Restart");
    add(restart);

    //toggle looping button
    toggleLooping = new JButton("Loop");
    toggleLooping.setActionCommand("Loop");
    add(toggleLooping);

    //speed up button
    speedUp = new JButton("^Speed");
    speedUp.setActionCommand("SpeedUp");
    add(speedUp);

    //speed down button
    speedDown = new JButton("vSpeed");
    speedDown.setActionCommand("SpeedDown");
    add(speedDown);

    //tick up button
    tickUp = new JButton("^Tick");
    tickUp.setActionCommand("TickUp");
    add(tickUp);

    //tick down button
    tickDown = new JButton("vTick");
    tickDown.setActionCommand("TickDown");
    add(tickDown);

    tickLabel = new JLabel();
    tickLabel.setText("");
    add(tickLabel);
  }

  /**
   * Adds the actions listeners to buttons, uses a playbackControls as the thing to call upon.
   * @param playbackControls the playback controls to use.
   */
  public void addPlaybackControls(PlaybackControls playbackControls) {
    play.addActionListener(evt -> playbackControls.play());
    pause.addActionListener(evt -> playbackControls.pause());
    restart.addActionListener(evt -> playbackControls.restart());
    toggleLooping.addActionListener(evt -> playbackControls.toggleLooping());
    speedUp.addActionListener(evt -> playbackControls.increaseSpeed());
    speedDown.addActionListener(evt -> playbackControls.decreaseSpeed());
    tickUp.addActionListener(evt -> playbackControls.increaseTick());
    tickDown.addActionListener(evt -> playbackControls.decreaseTick());
  }

  /**
   * Updates the tick label to the give tick.
   * @param tick the tick to display.
   */
  public void setTickLabel(int tick) {
    this.tickLabel.setText(" " + tick);
  }
}
