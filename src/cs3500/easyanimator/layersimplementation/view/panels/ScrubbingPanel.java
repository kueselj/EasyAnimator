package cs3500.easyanimator.layersimplementation.view.panels;


import cs3500.easyanimator.layersimplementation.controller.ScrubbingControls;

import javax.swing.*;
import java.awt.*;

/**
 * JPanel consisting of the components related to the scrubbing of an Animation.
 */
public class ScrubbingPanel extends JPanel {

  JSlider slider;

  private static Dimension MAX_SCRUBBER_SIZE = new Dimension(Integer.MAX_VALUE, 25);

  public ScrubbingPanel() {
    super();
    setMaximumSize(MAX_SCRUBBER_SIZE);
    setLayout(new BorderLayout());
    setBackground(new Color(118, 54, 38));
    slider = new JSlider(JSlider.HORIZONTAL, 0,1000,0);
    slider.setPaintLabels(true);
    slider.setPaintTicks(true);
    add(slider, BorderLayout.PAGE_END);
  }

  /**
   * Adds scrubbing controls to the scrubbing panel.
   * @param scrubbingControls the scrubbing controls the panel can use.
   */
  public void addScrubbingControls(ScrubbingControls scrubbingControls) {

    slider.addChangeListener(evt -> scrubbingControls.scrubbingOccurred(slider.getValue()));
  }

  /**
   * Sets the max value of the scrubber.
   * @param max the max value the scrubber should have.
   */
  public void setScrubbingMax(int max) {
    slider.setMaximum(max);
    slider.setMajorTickSpacing(max / 10);
    slider.setMinorTickSpacing(max / 100);
    slider.setPaintLabels(true);
    slider.setPaintTicks(true);

  }

  /**
   * Sets the position of the scrubber.
   * @param position the position of the scrubber.
   */
  public void setScrubberPosition(int position) {
    slider.setValue(position);
  }





}
