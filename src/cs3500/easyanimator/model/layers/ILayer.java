package cs3500.easyanimator.model.layers;

import cs3500.easyanimator.model.IAnimatorModel;

/**
 * An ILayer is an interface describing the basic features needed out of a layer.
 * Namely, it should have a name, a state for describing visibility, and a link to an AnimatorModel.
 * Note that we don't have setModel. That would be weird to dump all your objects like that and
 * NOT delete the layer.
 */
public interface ILayer {
  /**
   * Returns the name set for this layer.
   * @return  The name as a string. This probably shouldn't include newlines.
   */
  String getName();

  /**
   * Returns the visibility of this layer.
   * @return  Returns if this layer is currently visible or not.
   */
  boolean getVisibility();

  /**
   * Sets the visibility of this layer by returning a new layer with the specified visibility,
   * the same name and the same link to a model.
   * @param visibility  The visibility state to set on this layer.
   * @return  A layer with set visibility.
   */
  ILayer setVisibility(boolean visibility);

  /**
   * Sets the name of this layer by returning a new layer with the specified name, the same
   * visibility and the same link to a model.
   * @param name  The new name to set for this layer.
   * @return      A layer with the new name.
   */
  ILayer setName(String name);

  /**
   * Returns the model that is linked to by this layer.
   * @return      The model behind this layer.
   */
  IAnimatorModel getModel();
}
