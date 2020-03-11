package cs3500.easyanimator.model;

import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* TODO: JavaDocs.
*/
public class EasyAnimator implements IAnimatorModel {

  HashMap<String, IShape> shapes;
  // The list of motions we associate with will have the invariant that the end state of the
  // previous motion will match the start state of the next motion.
  // Additionally none of the motions will clash in tick range.
  // We have another invariant here that the keysets for the shapes map and the motions map should
  // match.
  HashMap<String, List<IMotion>> motions;

  /**
   * Basic constructor for an EasyAnimator, just gives sets both fields to empty HashMaps.
   */
  public EasyAnimator() {
    this.shapes = new HashMap<String, IShape>();
    this.motions = new HashMap<String, List<IMotion>>();
  }

  @Override
  public void addShape(String id, IShape shape) {
    // TODO: Implement this! After Testing of course!
  }

  @Override
  public void removeShape(String id) {
    // TODO: Implement this! After Testing of course!
  }

  @Override
  public Map<String, IShape> getShapes() {
    // TODO: Implement this! After Testing of course!
    return null;
  }

  @Override
  public void addMotion(String id, IMotion motion) {
    // TODO: Implement this! After Testing of course!
  }

  @Override
  public void removeMotion(String id, IMotion motion) {
    // TODO: Implement this! After Testing of course!
  }

  @Override
  public Map<String, List<IMotion>> getMotions() {
    // TODO: Implement this! After Testing of course!
    return null;
  }

}
