package cs3500.easyanimator.layersimplementation.controller;

public interface EditorControls {


  //TODO I need to think about save shape a little more and what exactly it needs.
  /**
   * Saves the newly created shape.
   * @param shapeName the new shape.
   */
  void saveShape(String shapeName);

  /**
   * Deletes the given shape.
   * @param shapeName the name of the shape to delete.
   */
  void deleteShape(String shapeName);

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
                    int tick,
                    int x, int y,
                    int width, int height,
                    int r, int g, int b);

  /**
   * Deletes the keyframe of the given shape at the given tick.
   * @param shapeName the name of the shape.
   * @param tickOfKeyFrame the tick of the keyframe to delete.
   */
  void deleteKeyFrame(String shapeName, int tickOfKeyFrame);

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
  void selectTick(String shapeName, int tick);
}
