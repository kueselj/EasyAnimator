package cs3500.easyanimator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * View Only interface for an IAnimatorView, only has getter methods.
 */
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
   * Gets all the shapes in this model.
   * @returns A map between the names of shapes and their instances.
   *          This will be a copy to disallow outside mutation.
   *          The keyset is guaranteed to follow insertion order.
   */
  Map<String, IShape> getShapes();

  /**
   * Gets the names of shapes in this model.
   * @returns A list (that happens to be sorted by the insertion order) of the shapes contained in
   *          the animation.
   */
  default List<String> getShapeNames() {
    return new ArrayList<>(getShapes().keySet());
  }

  /**
   * Gets the significant ticks at which keyframes exist in this model.
   * @param id  The id of the shape to get ticks for.
   * @returns A list of ticks (in order) for which there exists keyframes for, for the given shape.
   */
  default List<Integer> getShapeKeyframeTicks(String id) {
    return new ArrayList<>(getKeyframes(id).keySet());
  }

  /**
   * Returns the map containing all of the ids of the IShapes and their respective list of motions.
   * These motions will be sorted by IMotion endTime.
   *
   * @return the map of shape id's to their motions. This will be a copy to disallow mutation.
   */
  Map<String, SortedSet<IMotion>> getSortedMotions();

  /**
   * Returns a map containing all the ids of shapes and their respective lists.
   * These motions will be sorted, but this not guaranteed by type.
   * @return A map from string ids to lists of motions.
   * @deprecated This method is deprecated in favor of get sorted motions.
   */
  default Map<String, List<IMotion>> getMotions() {
    HashMap<String, List<IMotion>> map = new HashMap<>();
    for (Map.Entry<String, SortedSet<IMotion>> entry: getSortedMotions().entrySet()) {
      map.put(entry.getKey(), new ArrayList<>(entry.getValue()));
    }
    return map;
  }

  /**
   * Gets the desired shape's state at the desired tick.
   *
   * @param id the name of the shape to return at a desired tick.
   * @param tick the tick to get the specified shape state at.
   * @return the desired shape at a given tick, or null if outside motions.
   * @throws IllegalArgumentException if the shape name or tick is invalid.
   */
  IShape getShapeAtTick(String id, int tick);

  /**
   * Gets a list of shapes and their state at the desired tick.
   *
   * @param tick the tick to get the shapes at.
   * @return a list of shapes with their states at the desired tick.
   *         These shapes are sorted by insertion time.
   * @throws IllegalArgumentException if the shape name or tick is invalid.
   */
  List<IShape> getShapesAtTick(int tick);

  /**
   * Gets the last tick used by any motion in the model.
   * @return the max tick of the model. 0 if not applicable.
   */
  default int getMaxTick() {
    int maxTick = 0;
    // Iterate through each shape.
    for (String key: getShapes().keySet()) {
      int maxEndTime = this.getShapeMaxTick(key);
      if (maxEndTime > maxTick) {
        maxTick = maxEndTime;
      }
    }
    return maxTick;
  }

  /**
   * Gets the last tick used by the named shape in the model.
   * @param id  The id of the shape to lookup.
   * @return    The last tick of motion for the given shape, or null if non applicable.
   * @throws IllegalArgumentException If the given id doesn't match up to anything in the model.
   */
  int getShapeMaxTick(String id);

  /**
   * Gets a list of keyframes from the model for the given shape. These keyframes will in the form
   * of IShapes. The map is orderered so that the ticks match natural ascending order.
   * @param id  The id of the shape to lookup.
   * @return    A sorted map from integer ticks to states in the form of IShapes.
   */
  SortedMap<Integer, IShape> getKeyframes(String id);

}
