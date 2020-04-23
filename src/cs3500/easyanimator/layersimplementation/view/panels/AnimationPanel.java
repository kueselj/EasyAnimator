package cs3500.easyanimator.layersimplementation.view.panels;

import cs3500.easyanimator.layersimplementation.controller.PlaybackControls;
import cs3500.easyanimator.layersimplementation.controller.ScrubbingControls;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.view.DrawPanel;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

import java.awt.Dimension;

import java.util.ArrayList;
import java.util.List;

/**
 * JPanel containing all the components of the actual drawing and
 * playback controls of the animation.
 */
public class AnimationPanel extends JPanel {

  private PlaybackPanel playbackPanel;
  private ScrubbingPanel scrubbingPanel;
  private DrawPanel drawPanel;


  /**
   * Basic constructor for an animation panel, will construct a
   * DrawPanel and a PlayBack panel to use.
   */
  public AnimationPanel() {

    drawPanel = new DrawPanel();
    playbackPanel = new PlaybackPanel();
    scrubbingPanel = new ScrubbingPanel();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    //setLayout(new BorderLayout());

    List<IShape> shapes = new ArrayList<>();
    setShapes(shapes);

    //DRAW PANEL
    //TODO fix the scroll pane holy shit wtf.
    drawPanel.setSize(500, 500);
    drawPanel.setPreferredSize(new Dimension(500, 500));
    JScrollPane scrollPane = new JScrollPane(drawPanel);
    scrollPane.setSize(0, 0);
    add(scrollPane);

    add(scrubbingPanel);

    //PLAYBACK PANEL
    add(playbackPanel);




    drawPanel.repaint();
  }

  /**
   * Sets the drawPanels shapes to draw to the given shapes.
   * @param shapes the shapes to set the draw panel to draw.
   */
  public void setShapes(List<IShape> shapes) {
    this.drawPanel.setShapes(shapes);
    this.drawPanel.repaint();
  }

  /**
   * Adds playback controls to the playback panel.
   * @param playbackControls the playback controls the playback panel can use.
   */
  public void addPlaybackControls(PlaybackControls playbackControls) {
    this.playbackPanel.addPlaybackControls(playbackControls);
  }

  /**
   * Sets the scrubbing listener controls.
   * @param scrubbingControls the scrubbing controls the listener can use.
   */
  public void addScrubbingControls(ScrubbingControls scrubbingControls) {
    scrubbingPanel.addScrubbingControls(scrubbingControls);
  }

  /**
   * Sets the max size of the scrubber.
   * @param max the max size of the scrubber.
   */
  public void setScrubbingMax(int max) {
    scrubbingPanel.setScrubbingMax(max);
  }

  /**
   * Sets the tick for any components that need the current tick.
   * @param tick the tick to set.
   */
  public void setTick(int tick) {
    playbackPanel.setTickLabel(tick);
    scrubbingPanel.setScrubberPosition(tick);
  }



}
