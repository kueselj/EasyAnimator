package cs3500.easyanimator.controller;

import cs3500.easyanimator.model.shapes.IShape;

public interface IController {

  /**
   * Start the program, i.e. give control to the controller
   */
  void go();

  /**
   * Refreshes the controller.
   */
  void refresh();

  /**
   * Adds the keyFrame at the specified tick.
   * @param id the shape's id.
   * @param state the state of the shape.
   * @param tick the tick to add the keyFrame at
   */
  void addKeyFrame(String id, IShape state, int tick);

  /**
   * Removes the keyFrame of the given shape id at the given tick.
   * @param id the id of the shape.
   * @param tick the tick to remove the keyFrame of.
   */
  void removeKeyFrame(String id, int tick);
}
