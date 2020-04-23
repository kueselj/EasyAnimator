package cs3500.easyanimator.layersimplementation.view.panels;


import javax.swing.*;
import java.awt.*;

/**
 * JPanel consisting of the components related to the scrubbing of an Animation.
 */
public class ScrubbingPanel extends JPanel {

  JSlider slider;

  private static Dimension MAX_SCRUBBER_SIZE = new Dimension(Integer.MAX_VALUE, 50);

  public ScrubbingPanel() {
    super();
    setBackground(new Color(49, 98, 106));
    slider = new JSlider(JSlider.HORIZONTAL);
    slider.setMajorTickSpacing(100);
    slider.setMinorTickSpacing(10);
    slider.setPaintLabels(true);
    slider.setPaintTicks(true);
  }

  /**
   * Adds scrubbing controls to the scrubbing panel.
   * @param scrubbingControls the scrubbing controls the panel can use.
   */
  public void addScrubbingControls(ScrubbingControls scrubbingControls) {
    slider.addChangeListener(evt -> scrubbingControls.scrubbingOccured(slider.getValue()));
  }

  /**
   * Sets the max value of the scrubber.
   * @param max the max value the scrubber should have.
   */
  public void setScrubbingMax(int max) {
    slider.setMaximum(max);
  }

  /**
   * Sets the position of the scrubber.
   * @param position the position of the scrubber.
   */
  public void setScrubberPosition(int position) {
    slider.setValue(position);
  }



}
