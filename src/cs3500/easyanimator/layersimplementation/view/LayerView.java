package cs3500.easyanimator.layersimplementation.view;

import cs3500.easyanimator.layersimplementation.controller.EditorControls;
import cs3500.easyanimator.layersimplementation.controller.LayerControls;
import cs3500.easyanimator.layersimplementation.controller.PlaybackControls;
import cs3500.easyanimator.layersimplementation.view.panels.AnimationPanel;
import cs3500.easyanimator.layersimplementation.view.panels.EditorPanel;
import cs3500.easyanimator.layersimplementation.view.panels.LayerPanel;
import cs3500.easyanimator.model.shapes.IShape;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Frame representing the visual view of an EasyAnimator, contains three main panels.
 * Editor Panel: Used for editing shapes and their keyFrames.
 * Animation Panel: Used for displaying animation and contains playback controls.
 * Layer Panel: Contains the layer information of the easyAnimator.
 */
public class LayerView extends JFrame implements ILayerView {

  //Animation panel will hold a drawPanel and the playBackControls. the original animation panel
  //is now called DrawPanel
  private AnimationPanel animationPanel;
  private EditorPanel editorPanel;
  private LayerPanel layerPanel;

  //TODO this constructor needs to be changed, no width height probably.
  public LayerView(int width, int height) {

    //Initialize panels
    animationPanel = new AnimationPanel();
    editorPanel = new EditorPanel();
    layerPanel = new LayerPanel();

    //FRAME OPERATIONS
    setResizable(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    //EDITOR PANEL.
    add(editorPanel, BorderLayout.WEST);

    //ANIMATION PANEL.
    add(animationPanel);

    //LAYER PANEL.
    add(layerPanel, BorderLayout.EAST);
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void addPlaybackControls(PlaybackControls playbackControls) {
    animationPanel.addPlaybackControls(playbackControls);
  }

  @Override
  public void addEditorControls(EditorControls editorControls) {
    editorPanel.addEditorControls(editorControls);
  }

  @Override
  public void addLayerControls(LayerControls layerControls) {
    layerPanel.addLayerControls(layerControls);
  }

  @Override
  public void setTickLabel(int tick) {
    animationPanel.setTickLabel(tick);
  }

  @Override
  public void setDrawShapes(List<IShape> shapes) {
    animationPanel.setShapes(shapes);
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
