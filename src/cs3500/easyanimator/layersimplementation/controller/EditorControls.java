package cs3500.easyanimator.layersimplementation.controller;

public interface EditorControls {


  //TODO I need to think about save shape a little more and what exactly it needs.

  /**
   * Adds a shape to the model.
   * @param shapeName the name of the shape to add.
   * @param shapeType the shapeType of the the new shape.
   */
  void addShape(String shapeName, String shapeType);

  /**
   * Deletes the given shape.
   * @param shapeName the name of the shape to delete.
   */
  void deleteShape(String shapeName);

  /**
   * Renames a shape.
   * @param name the shape to rename.
   * @param newName the new name of the shape.
   * @param shapeType the shapeType of the shape.
   */
  void renameShape(String name, String newName, String shapeType);

  /**
   * Saves a keyFrame of the given shape.
   * @param shapeName the name of the shape to save a keyFrame for.
   * @param tick the tick of the keyframe.
   * @param x the x coordinate of the keyframe.
   * @param y the y coordinate of the keyframe.
   * @param width the width of the keyframe.
   * @param height the height of the keyframe.
   * @param r the red value of the keyframe.
   * @param g the green value of the keyframe.
   * @param b the blue value of the keyframe.
   */
  void saveKeyFrame(String shapeName,
                    String tick,
                    String x, String y,
                    String width, String height,
                    String r, String g, String b);

  /**
   * Deletes the keyframe of the given shape at the given tick.
   * @param shapeName the name of the shape.
   * @param tickOfKeyFrame the tick of the keyframe to delete.
   */
  void deleteKeyFrame(String shapeName, String tickOfKeyFrame);

  /**
   * Grabs all the data about the selected shape and hands it to the view.
   * @param shapeName the shape name to grab data about.
   */
  void selectShape(String shapeName);

  /**
   * Grabs all the relevant data about the shape at the given tick.
   * @param shapeName the shape to grab data about at the given tick.
   * @param tick the tick to grab data about for the given shape.
   */
  void selectTick(String shapeName, String tick);
}
