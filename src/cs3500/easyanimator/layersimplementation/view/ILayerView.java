package cs3500.easyanimator.layersimplementation.view;

import cs3500.easyanimator.layersimplementation.controller.PlaybackControls;
import cs3500.easyanimator.layersimplementation.controller.EditorControls;
import cs3500.easyanimator.layersimplementation.controller.LayerControls;

import cs3500.easyanimator.layersimplementation.controller.ScrubbingControls;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.WidthHeight;

import java.util.List;

/**
 * An ILayerView is the interface behind a view of the animation's layers.
 */
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
   * Sets the scrubbing listener controls.
   * @param scrubbingControls the controls the listener can use.
   */
  void addScrubbingControls(ScrubbingControls scrubbingControls);

  /**
   * Gives the view to tick to update any components that need to know the current tick.
   * @param tick the tick to set.
   */
  void setTick(int tick);

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
  void setAvailableTicks(List<String> ticks);

  /**
   * Sets the text fields to the integers provided representing keyFrame tick, x, y, width, height,
   * and rgb values of the selected shape at the selected tick.
   * @param components the integers to set the fields as.
   * @throws IllegalArgumentException if it tries to pass more than 8 components.
   */
  void setTextFields(List<String> components);

  /**
   * Sets the available layers as a list of strings representing the layer names. Should pass on
   * to the layers selector component.
   * @param layers  The names of the layers.
   */
  void setLayers(List<String> layers);

  /**
   * Sets the preferred canvas size of the draw panel.
   * @param wH the width height to use.
   */
  void setPreferredCanvasSize(WidthHeight wH);

  /**
   * Sets the max size of the scrubber.
   * @param max the max to set.
   */
  void setScrubbingMax(int max);

  /**
   * Sounds the alarm.
   */
  void makeErrorSound();
}
