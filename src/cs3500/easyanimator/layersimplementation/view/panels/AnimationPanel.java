package cs3500.easyanimator.layersimplementation.view.panels;

import cs3500.easyanimator.layersimplementation.controller.PlaybackControls;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.view.DrawPanel;

import javax.swing.*;
import java.util.List;

public class AnimationPanel extends JPanel {

  private PlaybackPanel playbackPanel;
  private DrawPanel drawPanel;

  public AnimationPanel() {
    this.drawPanel = new DrawPanel();
    this.playbackPanel = new PlaybackPanel();
  }

  public void setShapes(List<IShape> shapes) {
    this.drawPanel.setShapes(shapes);
  }

  public void addPlaybackControls(PlaybackControls playbackControls) {
    this.playbackPanel.addPlaybackControls(playbackControls);
  }
}
