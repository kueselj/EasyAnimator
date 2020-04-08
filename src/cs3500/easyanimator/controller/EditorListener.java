package cs3500.easyanimator.controller;

import cs3500.easyanimator.model.shapes.IShape;

/**
 * Defines a listener for the different events that happens while a user is interacting with an
 * editor.
 */
public interface EditorListener {
  /**
   * Respond to the editor wishing to add a shape of the given name and type.
   * @param name  The name of the shape.
   * @param type  A string of the type of shape we wish to add.
   * @return      A boolean of success.
   */
  boolean addShape(String name, String type);

  /**
   * Respond to the editor wishing to remove a shape of the given name.
   * @param name  The name of the shape.
   * @return      A boolean of success.
   */
  boolean removeShape(String name);

  /**
   * Respond to the editor wishing to rename a shape with the given names.
   * @param name  The old name to replace.
   * @param newName The new name to use.
   * @param shapeType They may be trying to bundle a shape type change in as well.
   */
  boolean renameShape(String name, String newName, String shapeType);

  /**
   * Respond to the editor wishing to remove a keyframe for the given named shape,
   * at the given tick.
   * @param name  The name of the shape we wish to remove a keyframe for.
   * @param keyframe  The tick of the keyframe we wish to remove.
   * @return      A boolean of success.
   */
  boolean removeKeyframe(String name, int keyframe);

  /**
   * Respond to the editor wishing to add a keyframe for the given named shape,
   * with the given state, at the given tick.
   * @param name  The name of the shape we wish to add a keyframe to.
   * @param tick  The time to set the keyframe for.
   * @param w     The width of the keyframe to add.
   * @param h     The height of the keyframe to add.
   * @param x     The x position of the keyframe to add.
   * @param y     The y position of the keyframe to add.
   * @param r     The red component of the keyframe to add.
   * @param g     The green component of the keyframe to add.
   * @param b     The blue component of the keyframe to add.
   * @return      A boolean noting success.
   */
  boolean setKeyframe(String name,
                   int tick,
                   String w, String h,
                   String x, String y,
                   String r, String g, String b);

  /**
   * Saves the current model to a file.
   */
  void saveModel();

  /**
   * Loads a model.
   */
  void loadModel();
}
