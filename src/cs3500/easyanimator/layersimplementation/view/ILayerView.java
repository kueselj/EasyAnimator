package cs3500.easyanimator.layersimplementation.view;

import cs3500.easyanimator.model.shapes.IShape;

import java.util.List;

public interface ILayerView {

  /**
   * Sets the shapes, this is for a specific tick. Should pass to the draw component of the view.
   * @param shapes the shapes to display.
   */
  void setDrawShapes(List<IShape> shapes);

  /**
   * Sets the available shapes that the user can choose from.
   * @param shapes the shapes the user can pick from.
   */
  void setAvailableShapes(List<String> shapes);

  /**
   * Sets the available shape types to choose from.
   * @param shapeTypes the shape types to choose from.
   */
  void setAvailableShapeTypes(List<String> shapeTypes);

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
}
