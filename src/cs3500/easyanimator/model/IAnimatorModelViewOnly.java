package cs3500.easyanimator.model;

import java.util.List;
import java.util.Map;

import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.WidthHeight;

public interface IAnimatorModelViewOnly {
  /**
   * Gives the width and height of the canvas. This might be necessary for some views.
   * @returns The width and height of the canvas in the model.
   * @throws IllegalStateException  If there is no width and height of the canvas.
   */
  WidthHeight getCanvasSize();

  /**
   * Gives the location of the canvas as its top left corner. This might be immediately useful for
   * text views that need to know about the viewport.
   * @returns The top left corner of the canvas. But copied to avoid outside mutation.
   */
  Point getCanvasPosition();

  /**
   * Gets all the named shapes in this model.
   * @returns A map between the names of shapes and their instances.
   *          This will be a copy to disallow outside mutation.
   */
  Map<String, IShape> getShapes();

  /**
   * Returns the map containing all of the ids of the IShapes and their respective list of motions.
   * @return the map of shape id's to their motions. This will be a copy to disallow mutation.
   */
  Map<String, List<IMotion>> getMotions();

  /**
   * Gets the text output of the model.
   *
   * @return a string representing a text output.
   */
  String textOutput();

  /**
   * Gets the desired shape's state at the desired tick.
   *
   * @param tick the tick to get the specified shape state at.
   * @param shape the shape to return at a desired tick.
   * @return the desired shape at a given tick.
   * @throws IllegalArgumentException if the shape name or tick is invalid.
   */
  IShape getShapeAtTick(int tick, String shape);

  /**
   * Gets a list of shapes and their state at the desired tick.
   *
   * @param tick the tick to get the shapes at.
   * @return a list of shapes with their states at the desired tick.
   * @throws IllegalArgumentException if the shape name or tick is invalid.
   */
  List<IShape> getShapesAtTick(int tick);
}
