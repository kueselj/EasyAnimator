package cs3500.easyanimator.view;

import cs3500.easyanimator.model.IAnimatorModelViewOnly;
import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.motions.IMotionVisitor;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A textual representation view of an IAnimatorModel.
 */
public class TextualView implements IAnimatorView {

  Appendable out;
  IAnimatorModelViewOnly model;

  /**
   * Basic constructor for a Textual view, needs an appendable to output to.
   * @param ap the appendable to output the text view to.
   */
  public TextualView(Appendable ap) {
    this.out = ap;
  }

  @Override
  public void makeVisible() {

    //output string builder to build.
    StringBuilder output = new StringBuilder();

    String x = Integer.toString(this.model.getCanvasPosition().getX());
    String y = Integer.toString(this.model.getCanvasPosition().getY());
    String width = Integer.toString(this.model.getCanvasSize().getWidth());
    String height = Integer.toString(this.model.getCanvasSize().getHeight());

    output.append("canvas " + x + " " + y + " " + width +  " " + height + "\n");

    //iterate through each shape or entry in
    for (Map.Entry<String, List<IMotion>> entry : this.model.getMotions().entrySet()) {

      String shapeType = this.model.getShapes().get(entry.getKey()).accept(new ShapeTypeVisitor());
      String shapeString = "shape " + entry.getKey() + " " + shapeType;

      //add the shape to the output.
      output.append(shapeString + "\n");

      Collections.sort(entry.getValue(), Comparator.comparingInt(IMotion::getStartTime));

      //now iterate through the list of motions for the given entry.
      for (IMotion motion : entry.getValue()) {
        String motionString = "motion " + entry.getKey() + " "
                + motion.accept(new MotionToTextVisitor());
        output.append(motionString + "\n");
      }
    }

    //now append to the appendable output.
    try {
      this.out.append(output.toString().trim());
    } catch (IOException e) {
      throw new IllegalArgumentException("Problem Appending to output");
    }
  }

  @Override
  public void setModel(IAnimatorModelViewOnly model) {
    this.model = model;
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

  /**
   * A private visitor class to return an IMotion as represented by a string.
   */
  private class MotionToTextVisitor implements IMotionVisitor<String> {

    @Override
    public String applyToBasicMotion(BasicMotion b) {
      return b.getStartTime() + " "
              + b.getStartPosition().getX() + " "
              + b.getStartPosition().getY() + " "
              + b.getStartSize().getWidth() + " "
              + b.getStartSize().getHeight() + " "
              + b.getStartColor().getRed() + " "
              + b.getStartColor().getGreen() + " "
              + b.getStartColor().getBlue() + " "
              + b.getEndTime() + " "
              + b.getEndPosition().getX() + " "
              + b.getEndPosition().getY() + " "
              + b.getEndSize().getWidth() + " "
              + b.getEndSize().getHeight() + " "
              + b.getEndColor().getRed() + " "
              + b.getEndColor().getGreen() + " "
              + b.getEndColor().getBlue();
    }
  }

  @Override
  public void setSpeed(double speed) {
    //this does nothing, textual view should not be
    // able to have a speed as it is just a text output of the model
  }
}
