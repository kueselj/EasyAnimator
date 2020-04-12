package cs3500.easyanimator.provider.views;

import cs3500.animator.AnimatorModel;
import cs3500.easyanimator.provider.controller.AnimatorController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

/**
 * Represents a visual view of an animation which allows for some controls such as pause and
 * restart. A child class of JFrame and implements IAnimationViewer.
 */
public class AnimationEditView extends JFrame implements IAnimationViewer {

  private AAnimationPanel panel;
  private AnimatorController controller;

  /**
   * A public constructor for an editable visual animation view. Creates the view pane and the
   * playback controls with initialized values.
   *
   * @param controller the animator controller
   * @param model the animator view
   */
  public AnimationEditView(AnimatorController controller, AnimatorModel model) {
    super();

    this.controller = controller;

    this.setLayout(new BorderLayout());
    this.setResizable(true);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setTitle("CS3500!");

    this.panel = new VisualView(model);

    JScrollPane pane = new JScrollPane(panel);
    pane.setPreferredSize(new Dimension(model.canvasX, model.canvasY));
    pane.getViewport().setViewPosition(new Point(model.xOffset, model.yOffset));
    this.getContentPane().add(pane);

    createPlaybackControlWindow();

    this.pack();
    this.setSize(new Dimension(model.canvasX, model.canvasY));
    this.setVisible(true);
  }

  /**
   * Creates the controls window which is a JPanel that holds all the animator controls. Adds and
   * sizes buttons and initializes them.
   */
  private void createPlaybackControlWindow() {
    JFrame controlFrame = new JFrame();
    JPanel controlPanel = new JPanel();
    JButton start = new JButton("Start");
    start.setSize(100, 50);
    controlPanel.add(start);
    JButton restart = new JButton("Restart");
    restart.setSize(100, 50);
    controlPanel.add(restart);
    JButton pause = new JButton("Pause");
    pause.setSize(100, 50);
    controlPanel.add(pause);
    JButton resume = new JButton("Resume");
    resume.setSize(100, 50);
    controlPanel.add(resume);

    JRadioButton looping = new JRadioButton("Loop animation?");
    controlPanel.add(looping);

    JSlider speed = new JSlider(1, 60);
    speed.setPaintTicks(true);
    Font font = new Font("Serif", Font.PLAIN, 12);
    speed.setFont(font);
    controlPanel.add(speed);

    controlFrame.add(controlPanel);

    controlFrame.setTitle("Editor Controls");
    controlFrame.setResizable(true);
    controlFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    controlFrame.setVisible(true);
    controlFrame.pack();

    controller.setupButtonCallbacks(start, restart, pause, resume, looping, speed);
  }

  @Override
  public void draw() {
    this.panel.repaint();
  }

  @Override
  public String toString() {
    return this.panel.toString();
  }
}
