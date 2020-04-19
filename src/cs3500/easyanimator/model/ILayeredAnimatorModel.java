package cs3500.easyanimator.model;

import cs3500.easyanimator.model.layers.ILayer;

/**
 * An ILayeredAnimatorModel is an implementation of the AnimatorModel with an extra feature set for
 * layers. These are groups of shapes and their motions, that have names, and visibility. The order
 * of layers in this model determines the order of shapes delivered as a regular animator model.
 *
 * Note that this does not extend an IAnimatorModel, only the ViewOnly version. That is because
 * some operations such as addShape, and removeShape, and addMotion and removeMotion, and so on,
 * have no natural interpretation for layers.
 *
 * Get shapes and get motions will still return what they need to. The only important difference is
 * that these will be lists made by compiling the results across all layers. The same thing happens
 * for canvases as well. There is one caveat to all of this, shapes across layers cannot have
 * duplicate names or there will be undefined behavior. The last added layer will have its shapes
 * placed last into getShapesAtTick so they are displayed on top.
 */
public interface ILayeredAnimatorModel extends ILayeredAnimatorModelViewOnly {
  /**
   * Add a new layer to this model. Layers are ordered by insertion order. So this layer will be
   * towards the bottom by default.
   * @param layer   The layer to add to the model.
   * @throws IllegalArgumentException If the layer is uninitialized.
   */
  void addLayer(ILayer layer);

  /**
   * Remove a layer from this model.
   * @param layer   The layer instance to remove from this model.
   * @throws IllegalArgumentException If the layer is uninitialized.
   */
  void removeLayer(ILayer layer);

  /**
   * Remove a layer, by index, from this model.
   * @param index   The layer index to remove from this model.
   * @throws IllegalArgumentException If the layer is out of bounds.
   */
  void removeLayer(int index);

  /**
   * Swap the layers located at the given indices.
   * @param i   The index of the first layer to swap.
   * @param j   The index of the second layer to swap.
   * @throws IllegalArgumentException
   */
  void swapLayer(int i, int j);
}
