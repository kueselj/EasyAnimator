package cs3500.easyanimator.model;

import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;

import java.util.*;

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

  /**
   * Outputs a String representing a text rendering output of the model.
   *
   * @return a text rendering output of the model as a String.
   */
  public String textOutput() {

    //output string builder to build.
    StringBuilder output = new StringBuilder();


    //iterate through each shape or entry in
    for (Map.Entry<String, List<IMotion>> entry: this.motions.entrySet()) {

      String shapeType = this.shapes.get(entry).accept(new ShapeTypeVisitor());
      String shapeString = "shape " + entry + " " + shapeType;

      //add the shape to the output.
      output.append(shapeString + "\n");

      Collections.sort(this.motions.get(entry), Comparator.comparingInt(IMotion::getStartTime));

      //now iterate through the list of motions for the given entry.
      for (IMotion motion: this.motions.get(entry)) {
        String motionString = "motion " + entry + " " + motion.toString();
        output.append(motionString + "\n");
      }
    }

    return output.toString().trim();
  }

  /**
   * Private visitor class to return the type of shape as a string.
   */
  private class ShapeTypeVisitor implements IShapeVisitor<String> {

    @Override
    public String applyToRectangle(Rectangle r) {
      return "rectangle";
    }

    @Override
    public String applyToOval(Oval o) {
      return "oval";
    }
  }
}
