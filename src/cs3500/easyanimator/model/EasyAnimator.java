package cs3500.easyanimator.model;

import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* TODO: JavaDocs.
*/
public class EasyAnimator {

  HashMap<String, IShape> shapes;
  HashMap<String, List<IMotion>> motions;

  /**
   * Basic constructor for an EasyAnimator, just gives sets both fields to empty HashMaps.
   */
  public EasyAnimator() {
    this.shapes = new HashMap<String, IShape>();
    this.motions = new HashMap<String, List<IMotion>>();
  }

  /**
   * Adds a motion bound to the specific id representing an IShape.
   * @param id the id of the IShape.
   * @param motion the motion to be added.
   *
   * @throws IllegalArgumentException if the motion is invalid.
   */
  public void addMotion(String id, IMotion motion) {
    //TODO: Implement this! After Testing of course!
  }

  /**
   * Adds a motion bound to the specific id representing an IShape.
   * @param id the id of the IShape.
   * @param motion the motion to be removed.
   *
   * @throws IllegalArgumentException if the motion is invalid or the
   * motion is not bound to the specified id.
   */
  public void removeMotion(String id, IMotion motion) {
    //TODO: Implement this! After Testing of course!
  }

  /**
   * Returns the map containing all of the ids of the IShapes and their respective list of motions.
   *
   * @return the map of shape id's to their motions
   */
  public Map<String, List<IMotion>> getMotions() {
    //TODO: Implement this! After Testing of course!
    return null;
  }

}
