package cs3500.easyanimator.layersimplementation.controller;

/**
 * A collection of user events / actions that occur in a view that need to be listened to.
 */
public interface LayerControls {
  /**
   * Select the layer with the given name.
   * @param layer The name of the layer. "New Layer" being reserved for a new layer.
   */
  void selectLayer(String layer);

  /**
   * Delete the layer with the given name.
   * @param layer The name of the layer.
   */
  void deleteLayer(String layer);

  /**
   * Save the layer with the given name with a new name,
   * @param selectedLayer The name of the layer selected,
   *                      if "New Layer" then this action creates a layer.
   * @param layerName     The new name of the layer.
   */
  void saveLayer(String selectedLayer, String layerName);

  /**
   * Move the given layer by a certain delta (1 meaning upwards in index, -1 means downwards).
   * @param selectedLayer The name of the layer selected.
   * @param delta         The amount to move the layer by.
   */
  void moveLayer(String selectedLayer, int delta);
}
