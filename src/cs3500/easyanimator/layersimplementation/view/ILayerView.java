package cs3500.easyanimator.layersimplementation.view;

import cs3500.easyanimator.layersimplementation.controller.*;
import cs3500.easyanimator.model.shapes.IShape;

import java.util.List;

public interface ILayerView {

  /**
   * Makes the layer view visible.
   */
  void makeVisible();

  /**
   * Sets the playbackListener controls.
   * @param playbackControls the listener for playback controls.
   */
  void addPlaybackControls(PlaybackControls playbackControls);

  /**
   * Sets the editorListener controls.
   * @param editorControls the listener for editor controls.
   */
  void addEditorControls(EditorControls editorControls);

  /**
   * Sets the layerListener controls.
   * @param layerControls the listener for layer controls.
   */
  void addLayerControls(LayerControls layerControls);

  /**
   * Sets the tick label in playback controls to the given tick.
   * @param tick the tick to set.
   */
  void setTickLabel(int tick);

  /**
   * Sets the shapes, this is for a specific tick. Should pass to the draw component of the view.
   * @param shapes the shapes to display.
   */
  void setDrawShapes(List<IShape> shapes);


  /**
   * Sets the available shapes of the editor panel.
   * @param shapes the available shapes.
   */
  void setAvailableShapes(List<String> shapes);


  /**
   * Sets the current shape in the editor view to the given shape.
   * @param shape the shape to set.
   */
  void setCurrentShape(String shape);

  /**
   * Sets the available shapes that the user can choose from.
   * @param shapeType the shapes the user can pick from.
   */
  void setShapeType(String shapeType);

  /**
   * Sets the available ticks that the user can pick from for the selected shape.
   * @param ticks the ticks the user can pick from.
   */
  void setAvailableTicks(List<Integer> ticks);

  /**
   * Sets the text fields to the integers provided representing keyFrame tick, x, y, width, height,
   * and rgb values of the selected shape at the selected tick.
   * @param components the integers to set the fields as.
   * @throws IllegalArgumentException if it tries to pass more than 8 components.
   */
  void setTextFields(List<Integer> components);

  /**
   * Sets the available layers as a list of integers representing the layers. Should pass on
   * to the layers selector component.
   * @param layers
   */
  void setLayers(List<Integer> layers);

  /**
   * Sounds the alarm.
   */
  void makeErrorSound();
}
