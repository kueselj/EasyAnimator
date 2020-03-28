package cs3500.easyanimator.view;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.IAnimatorModelViewOnly;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.motions.IMotionVisitor;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * A view representation of an IAnimatorModel in SVGFormat.
 */
public class SVGAnimationView implements IAnimatorView {
  private Appendable out;
  private IAnimatorModelViewOnly model;
  private Double speed;

  /**
   * Create a new SVGAnimationView with the given output to write to.
   *
   * @param out The output to write to with our string.
   */
  public SVGAnimationView(Appendable out) {
    this.out = out;
  }

  /**
   * A public method to convert a Color into a SVG-usable color.
   *
   * @param color The color to convert.
   * @return A string in the format rgb(x, x, x) with that spacing.
   */
  public static String colorConvertSVG(Color color) {
    return String.format("rgb(%d, %d, %d)",
            color.getRed(), color.getGreen(), color.getBlue());
  }

  @Override
  public void makeVisible() {
    if (this.model == null || this.speed == null) {
      throw new IllegalStateException("Unable to make view with null parameters.");
    }

    // We could use the library, but I prefer to write this by hand.
    StringBuilder document = new StringBuilder();
    // Start my svg block.
    document.append(String.format("<svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"%d %d %d %d\">\n",
            model.getCanvasPosition().getX(), model.getCanvasPosition().getY(),
            model.getCanvasSize().getWidth(), model.getCanvasSize().getHeight()));

    // We reuse these visitor.
    IMotionVisitor<String> getAnimates = new SVGMotionVisitor(speed);
    IShapeVisitor<String> getShapeFormat = new SVGShapeVisitor();

    Map<String, IShape> shapes = model.getShapes();
    Map<String, List<IMotion>> motions = model.getMotions();

    // I think this is valid syntax. Just no control thing. I want to signify we are in svg block.
    for (Map.Entry<String, IShape> shapeEntry: shapes.entrySet()) {
      String shapeName = shapeEntry.getKey();
      List<IMotion> shapeMotions = motions.get(shapeName);

      String animates = shapeMotions.stream()
              .sorted(Comparator.comparingInt(IMotion::getEndTime)) // We sort. Needed in our tests.
              .map(m -> m.accept(getAnimates)) // We map those motions into the associated animates.
              .collect(Collectors.joining("\n")); // We combine them with \ns.

      document.append(String.format(shapeEntry.getValue()
              .accept(getShapeFormat), // We get the shape's format string.
              shapeName,    // We actually get a format string, we add the id here.
              animates));   // We then add in the animates elements.
      document.append("\n");
      document.append("\n");
      document.append("\n");
    }

    document.append("</svg>");
    // End my svg block.

    try {
      out.append(document.toString());
    } catch (IOException io) {
      throw new IllegalStateException("Unable to write to the appendable object in SVGView.");
    }
  }

  @Override
  public void setModel(IAnimatorModelViewOnly model) {
    this.model = model;
  }

  @Override
  public void setSpeed(double speed) {
    this.speed = speed;
  }
}

/**
 * A SVGMotionVisitor is a large piece of visitor code to get the svg elements needed to animate
 * the properties of a shape. It returns a string of the animate element(s).
 */
class SVGMotionVisitor implements IMotionVisitor<String> {

  private double speed;

  /**
   * Create a new motion visitor with the specified speed.
   * @param speed The speed of the animation in tps.
   */
  public SVGMotionVisitor(double speed) {
    this.speed = speed;
  }

  /**
   * Returns the time for the given tick in the animation. This is in milliseconds and is rounded.
   * @param tick  The tick in the animation.
   * @return      A string that looks like "xxxxms" never a decimal point.
   */
  private String getTime(int tick) {
    int timems = (int) ((1.0 / speed) * tick * 1000);
    return String.format("%dms", timems);
  }

  // A list of property names we will use.
  private static String PROPERTIES[] = new String[]{"width", "height", "x", "y", "fill"};

  @Override
  public String applyToBasicMotion(BasicMotion b) {
    StringBuilder builder = new StringBuilder();

    // Property changes is an array of properties that match up with the indices of PROPERTIES.
    // It's multidimensional so we can make a before and after.
    // Only if the before and after are different do we make a new animate element.
    // I use these properties more than once so I create variables with small acronym names.
    WidthHeight ss = b.getStartSize();
    WidthHeight es = b.getEndSize();
    Point sp = b.getStartPosition();
    Point ep = b.getEndPosition();
    String propertyChanges[][] = {
            {Integer.toString(ss.getWidth()), Integer.toString(es.getWidth())}, // width
            {Integer.toString(ss.getHeight()), Integer.toString(es.getHeight())}, // height
            {Integer.toString(sp.getX()), Integer.toString(ep.getX())}, // x
            {Integer.toString(sp.getY()), Integer.toString(ep.getY())}, // y

            {SVGAnimationView.colorConvertSVG(b.getStartColor()),
                    SVGAnimationView.colorConvertSVG(b.getEndColor())}, // fill
    };

    for (int j = 0; j < propertyChanges.length; j++) {
      if (!(propertyChanges[j][0].equals(propertyChanges[j][1]))) {
        // They were different. We add to the output.
        builder.append(String.format(
                "<animate attributeName=\"%s\" attributeType=\"XML\" \n" +
                        "begin=\"%s\" dur=\"%s\" \n" +
                        "from=\"%s\" to=\"%s\" \n" +
                        "fill=\"freeze\" />\n",
                PROPERTIES[j],
                getTime(b.getStartTime()),
                getTime(b.getEndTime() - b.getStartTime()),
                propertyChanges[j][0], propertyChanges[j][1]
        ));
      }
    }
    return builder.toString();
  }
}

/**
 * A SVGShapeVisitor a small visitor used to transform a given shape into it's svg representation.
 * The string it returns is a format string leaving two format code for a name id (id=\"%s\"), and a
 * string of elements respectively <rect>%s</rect> (e,g).
 */
class SVGShapeVisitor implements IShapeVisitor<String> {

  /**
   * A little private method to return a suffix for what should be the svg element.
   * You just need to add "<rect " or "<ellipse " at the beginning.
   * @param s   The shape to apply to.
   * @return    The string svg.
   */
  private static String applyToIShape(IShape s) {
    return String.format("id=\"%s\" x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"%s\"> \n" +
                    "%s \n",
            "%s",
            s.getPosition().getX(), s.getPosition().getY(),
            s.getSize().getWidth(), s.getSize().getHeight(),
            SVGAnimationView.colorConvertSVG(s.getColor()),
            "%s"
            );
  }

  @Override
  public String applyToRectangle(Rectangle r) {
    return "<rect " + applyToIShape(r) + "</rect>";
  }

  @Override
  public String applyToOval(Oval o) {
    return "<ellipse " + applyToIShape(o) + "</ellipse>";
  }
}