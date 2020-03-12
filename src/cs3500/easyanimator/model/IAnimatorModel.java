package cs3500.easyanimator.model;

import java.util.List;
import java.util.Map;

import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;

/**
 * An IAnimatorModel is an interface describing what the model for an animation program will need
 * to do. It will need the ability to edit motions and shapes (add, remove) as well as observe th
 * motions for some named shape (id).
 */
public interface IAnimatorModel {

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
   * Gets all the named shapes in this model.
   * @returns A map between the names of shapes and their instances.
   *          This will be a copy to disallow outside mutation.
   */
  Map<String, IShape> getShapes();

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
}
