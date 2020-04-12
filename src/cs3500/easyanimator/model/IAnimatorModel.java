package cs3500.easyanimator.model;

import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * An IAnimatorModel is an interface describing what the model for an animation program will need
 * to do. It will need the ability to edit motions and shapes (add, remove) as well as observe th
 * motions for some named shape (id).
 */
public interface IAnimatorModel extends IAnimatorModelViewOnly {
  /**
   * Sets the canvas for the given animation. This is a viewport for all of our shapes and affects
   * how getShapesAtTick and getShapeAtTick behave.
   * @param topLeftCorner The top left corner of the canvas in absolute coordinates like the shapes.
   * @param widthHeight   The width and height of the canvas viewport.
   *                      This is added to the top left corner.
   * @throws IllegalArgumentException If the top left corner or width and height are uninitialized.
   */
  void setCanvas(Point topLeftCorner, WidthHeight widthHeight);

  /**
   * Adds the given named IShape to the model. If a shape already exists at the given id,
   * it is replaced (with the same motions). If it did not originally exist under the same name,
   * then a new blank list of motions is created.
   * @param id The id to associate with the added shape.
   * @param shape The shape to add to the model.
   * @throws IllegalArgumentException If the shape or id are uninitialized.
   */
  void addShape(String id, IShape shape);

  /**
   * Removes the given named IShape from the model. This also clears the motions associated with
   * this shape.
   * @param id  The id of the shape to remove.
   * @throws IllegalArgumentException If the given id doesn't match anything in the model.
   */
  void removeShape(String id);

  /**
   * Adds a motion bound to the specific id representing an IShape.
   * @param id the id of the IShape.
   * @param motion the motion to be added.
   * @throws IllegalArgumentException if the motion is invalid, for example if forces teleportation
   *                                  or clashes in tick range with other motions.
   */
  void addMotion(String id, IMotion motion);

  /**
   * Adds a motion bound to the specific id representing an IShape.
   * @param id the id of the IShape.
   * @param motion the motion to be removed.
   * @throws IllegalArgumentException if this removal forces us to break our invariants
   *                                  (teleportation and tick range clashing) or if it does not
   *                                  belong to the given id.
   */
  void removeMotion(String id, IMotion motion);

  /**
   * Adds a keyframe to the model (splitting a motion if necessary or creating new ones).
   * @param id    The id of the shape to add a keyframe to.
   * @param state   The state of the the shape at the given time.
   * @param tick    The tick to add the keyframe at.
   * @throws IllegalArgumentException If the id is invalid, or the id and state are uninitialized.
   */
  void addKeyframe(String id, IShape state, int tick);

  /**
   * Deletes the keyframe in the model at the given time (joining motions if necessary).
   * @param id      The id of the shape to remove a keyframe from.
   * @param tick    The tick at which we wish to remove a keyframe.
   * @throws IllegalArgumentException If the id is invalid, or the id and state are uninitialized.
   */
  void removeKeyframe(String id, int tick);
}
