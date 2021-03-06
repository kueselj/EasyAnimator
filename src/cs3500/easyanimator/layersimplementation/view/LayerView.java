package cs3500.easyanimator.layersimplementation.view;

import cs3500.easyanimator.layersimplementation.controller.EditorControls;
import cs3500.easyanimator.layersimplementation.controller.LayerControls;
import cs3500.easyanimator.layersimplementation.controller.PlaybackControls;
import cs3500.easyanimator.layersimplementation.controller.ScrubbingControls;
import cs3500.easyanimator.layersimplementation.view.panels.AnimationPanel;
import cs3500.easyanimator.layersimplementation.view.panels.EditorPanel;
import cs3500.easyanimator.layersimplementation.view.panels.LayerPanel;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.WidthHeight;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Toolkit;
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

  /**
   * Construct a new LayerView with a given width and height of the window.
   * @param width   The width of the window.
   * @param height  The height of the window.
   */
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
    add(animationPanel, BorderLayout.CENTER);

    //LAYER PANEL.
    add(layerPanel, BorderLayout.EAST);

    pack();
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
  public void addScrubbingControls(ScrubbingControls scrubbingControls) {
    animationPanel.addScrubbingControls(scrubbingControls);
  }

  @Override
  public void setTick(int tick) {
    animationPanel.setTick(tick);

  }

  @Override
  public void setDrawShapes(List<IShape> shapes) {
    animationPanel.setShapes(shapes);
  }

  public void setAvailableShapes(List<String> shapes) {
    editorPanel.setAvailableShapes(shapes);
  }

  @Override
  public void setCurrentShape(String shape) {
    editorPanel.setCurrentShape(shape);
  }

  @Override
  public void setShapeType(String shapeType) {
    editorPanel.setShapeType(shapeType);
  }

  @Override
  public void setAvailableTicks(List<String> ticks) {
    editorPanel.setAvailableTicks(ticks);
  }

  @Override
  public void setTextFields(List<String> components) {
    editorPanel.setTextFields(components);
  }

  @Override
  public void setLayers(List<String> layers) {
    layerPanel.setLayers(layers);
  }

  @Override
  public void setPreferredCanvasSize(WidthHeight wH) {
    animationPanel.setPreferredCanvasSize(wH);
  }

  @Override
  public void setScrubbingMax(int max) {
    animationPanel.setScrubbingMax(max);
  }

  private static Toolkit tk = Toolkit.getDefaultToolkit();

  @Override
  public void makeErrorSound() {
    tk.beep();
  }
}
