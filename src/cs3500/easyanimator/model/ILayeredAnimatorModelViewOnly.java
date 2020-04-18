package cs3500.easyanimator.model;

import java.util.List;

import cs3500.easyanimator.model.layers.ILayer;

/**
 * A ILayeredAnimatorModelViewOnly is the read-only version of IAnimatorModel with layers.
 * Layers are a way to group (and set the z-index of) a collection of shapes and their motions.
 * The read-only version of this model supplies methods to get layer information encapsulated in
 * layer objects.
 * Note that since this extends a regular IAnimatorModelViewOnly that it needs an interpretation.
 */
public interface ILayeredAnimatorModelViewOnly extends IAnimatorModelViewOnly {
  /**
   * Get all the layers currently in this model.
   * @return  A list of all the layers in this model.
   */
  List<ILayer> getLayers();

  /**
   * Get the layer located at the given index.
   * @param i Gets the layer located at a given index.
   * @return  The layer located at a certain index.
   */
  ILayer getLayer(int i);
}
