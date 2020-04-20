package cs3500.easyanimator.layersimplementation.view.panels;

import cs3500.easyanimator.layersimplementation.controller.PlaybackControls;
import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;
import cs3500.easyanimator.view.DrawPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JPanel containing all the components of the actual drawing and
 * playback controls of the animation.
 */
public class AnimationPanel extends JPanel {

  private PlaybackPanel playbackPanel;
  private DrawPanel drawPanel;

  /**
   * Basic constructor for an animation panel, will construct a
   * DrawPanel and a PlayBack panel to use.
   */
  public AnimationPanel() {

    drawPanel = new DrawPanel();
    playbackPanel = new PlaybackPanel();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    //setLayout(new BorderLayout());





    List<IShape> shapes = new ArrayList<>();
    IShape shp = new Oval(new WidthHeight(30, 30), new cs3500.easyanimator.model.Point(60, 60), new Color(100, 100, 100));
    IShape shpd = new Rectangle(new WidthHeight(30, 30), new cs3500.easyanimator.model.Point(450, 450), new Color(255, 100, 100));
    shapes.add(shp);
    shapes.add(shpd);
    setShapes(shapes);

    //DRAW PANEL
    //TODO fix the scroll pane holy shit wtf.
    drawPanel.setSize(500, 500);
    drawPanel.setPreferredSize(new Dimension(500, 500));
    JScrollPane scrollPane = new JScrollPane(drawPanel);
    scrollPane.setSize(0, 0);
    add(scrollPane);

    //PLAYBACK PANEL
    add(playbackPanel);


    drawPanel.repaint();
  }

  /**
   * Sets the drawPanels shapes to draw to the given shapes
   * @param shapes the shapes to set the draw panel to draw.
   */
  public void setShapes(List<IShape> shapes) {
    this.drawPanel.setShapes(shapes);
  }

  /**
   * Adds playback controls to the playback panel.
   * @param playbackControls the playback controls the playback panel can use.
   */
  public void addPlaybackControls(PlaybackControls playbackControls) {
    this.playbackPanel.addPlaybackControls(playbackControls);
  }

  /**
   * Sets the Playback panels tick label to the given tick.
   * @param tick the tick to set.
   */
  public void setTickLabel(int tick) {
    this.playbackPanel.setTickLabel(tick);
  }
}
