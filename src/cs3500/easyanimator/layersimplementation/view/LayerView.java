package cs3500.easyanimator.layersimplementation.view;

import cs3500.easyanimator.layersimplementation.controller.EditorControls;
import cs3500.easyanimator.layersimplementation.controller.ILayerMVCController;
import cs3500.easyanimator.layersimplementation.controller.LayerControls;
import cs3500.easyanimator.layersimplementation.controller.PlaybackControls;
import cs3500.easyanimator.layersimplementation.view.panels.AnimationPanel;
import cs3500.easyanimator.layersimplementation.view.panels.EditorPanel;
import cs3500.easyanimator.layersimplementation.view.panels.LayerPanel;
import cs3500.easyanimator.layersimplementation.view.panels.PlaybackPanel;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.view.DrawPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LayerView extends JFrame implements ILayerView {

  //Animation panel will hold a drawPanel and the playBackControls. the original animation panel
  //is now called DrawPanel
  private AnimationPanel animationPanel;
  private EditorPanel editorPanel;
  private LayerPanel layerPanel;

  private ILayerMVCController listener;

  public LayerView(int width, int height) {

    //Initialize panels
    this.animationPanel = new AnimationPanel();
    this.editorPanel = new EditorPanel();
    this.layerPanel = new LayerPanel();

    this.setResizable(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }



  @Override
  public void addPlaybackControls(PlaybackControls playbackControls) {
    this.animationPanel.addPlaybackControls(playbackControls);
  }

  @Override
  public void addEditorControls(EditorControls editorControls) {
    this.editorPanel.addEditorControls(editorControls);
  }

  @Override
  public void addLayerControls(LayerControls layerControls) {
    this.layerPanel.addLayerControls(layerControls);
  }

  @Override
  public void setDrawShapes(List<IShape> shapes) {
    this.animationPanel.setShapes(shapes);
  }

  @Override
  public void setAvailableShapes(List<String> shapes) {

  }

  @Override
  public void setAvailableShapeTypes(List<String> shapeTypes) {

  }

  @Override
  public void setAvailableTicks(List<Integer> ticks) {

  }

  @Override
  public void setTextFields(List<Integer> components) {

  }

  @Override
  public void setLayers(List<Integer> layers) {

  }


}
