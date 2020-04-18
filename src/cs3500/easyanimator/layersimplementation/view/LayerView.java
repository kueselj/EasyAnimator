package cs3500.easyanimator.layersimplementation.view;

import cs3500.easyanimator.layersimplementation.view.panels.EditorPanel;
import cs3500.easyanimator.layersimplementation.view.panels.LayerPanel;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.view.AnimationPanel;

import javax.swing.*;
import java.util.List;

public class LayerView extends JFrame implements ILayerView {

  private AnimationPanel animationPanel;
  private EditorPanel editorPanel;
  private LayerPanel layerPanel;

  public LayerView(int width, int height) {
    this.animationPanel = new AnimationPanel();
    this.editorPanel = new EditorPanel();
    this.layerPanel = new LayerPanel();
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
