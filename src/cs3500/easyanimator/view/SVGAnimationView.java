package cs3500.easyanimator.view;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.IAnimatorModelViewOnly;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.motions.IMotion;
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

  /**
   * Returns the time for the given tick in the animation. This is in milliseconds and is rounded.
   * @param tick  The tick in the animation.
   * @param speed The speed of the animation.
   * @param loop  If the looping bit should be added.
   * @return      A string that looks like "xxxxms" never a decimal point.
   */
  private static String getTime(long tick, double speed, boolean loop) {
    int timems = (int) ((1.0 / speed) * tick * 1000);
    if (loop) {
      return String.format("base.end+%dms", timems);
    } else {
      return String.format("%dms", timems);
    }
  }

  @Override
  public void makeVisible() {
    // A small variable to bring out into an interface if we ever need to toggle it.
    boolean loop = true;

    if (this.model == null || this.speed == null) {
      throw new IllegalStateException("Unable to make view with null parameters.");
    }

    // We could use the library, but I prefer to write this by hand.
    StringBuilder document = new StringBuilder();
    // Start my svg block.
    document.append(String.format("<svg version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"%d %d %d %d\">\n",
            model.getCanvasPosition().getX(), model.getCanvasPosition().getY(),
            model.getCanvasSize().getWidth(), model.getCanvasSize().getHeight()));

    long lastTick = model.getMaxTick();
    if (lastTick >= 0 && loop) {
      // We we want to loop, and there are things to loop (we give a broken document otherwise ...)
      document.append("<line id=\"loopingline\">\n" +
              "<animate id=\"base\" attributeName=\"visibility\">\n from=\"hide\" to=\"hide\" " +
              "begin=\"0ms\" dur=\"" + getTime(lastTick, speed, false) + "ms\"></animate>" +
              "</line>\n");
    }

    // We reuse these visitor.
    Function<IMotion, String> getAnimates = new SVGMotionVisitor(speed, loop);
    IShapeVisitor<String> getShapeFormat = new SVGShapeVisitor();

    Map<String, IShape> shapes = model.getShapes();
    Map<String, List<IMotion>> motions = model.getMotions();

    // I think this is valid syntax. Just no control thing. I want to signify we are in svg block.
    for (Map.Entry<String, IShape> shapeEntry: shapes.entrySet()) {
      String shapeName = shapeEntry.getKey();
      List<IMotion> shapeMotions = motions.get(shapeName);

      String animates = shapeMotions.stream()
              .sorted(Comparator.comparingInt(IMotion::getEndTime)) // We sort. Needed in our tests.
              .map(getAnimates) // We map those motions into the associated animates.
              .collect(Collectors.joining("\n")); // We combine them with \ns.

      Optional<Integer> firstVisibleTick = shapeMotions.stream()
              .map(IMotion::getStartTime)
              .min(Comparator.naturalOrder());
      Optional<Integer> lastVisibleTick = shapeMotions.stream()
              .map(IMotion::getEndTime)
              .max(Comparator.naturalOrder());

      if (firstVisibleTick.isPresent()) {
        // If there is a first visible tick then there is a last visible tick.
        // We prepend an animation to make it visible.
        animates = "<animate attributeType=\"xml\" begin=\"" +
                getTime(0, speed, true) +
                "\" dur=\"" +
                getTime(firstVisibleTick.get(), speed, false) +
                "\" fill=\"freeze\" from=\"hide\" to=\"visible\"></animate>" +
                animates +
                "<animate attributeType=\"xml\" begin=\"" +
                getTime(lastVisibleTick.get(), speed, true) +
                "\" dur=\"" +
                getTime(lastTick, speed, false) +
                "\" fill=\"freeze\" from=\"hide\" to=\"visible\"></animate>";
      }

      // If we are looping then we also need to add an element at the end to transition
      // to our initial state.
      if (loop) {
        animates += "<animate attributeType=\"xml\" begin=\"base.end\" dur=\"1ms\"" +
                "attributeName=\"visibility\" />";
      }

      document.append(String.format(shapeEntry.getValue()
              .accept(getShapeFormat), // We get the shape's format string.
              shapeName,    // We actually get a format string, we add the id here.
              animates));   // We then add in the animates elements.

      // Finally if we loop we need an element to reset to the first state
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

  /**
   * A SVGMotionVisitor is a large piece of visitor code to get the svg elements needed to animate
   * the properties of a shape. It returns a string of the animate element(s).
   */
  private static class SVGMotionVisitor implements Function<IMotion, String> {

    private double speed;
    private boolean loop;

    /**
     * Create a new motion visitor with the specified speed.
     * @param speed The speed of the animation in tps.
     * @param loop  If the animate element should work off the special loop element.
     */
    public SVGMotionVisitor(double speed, boolean loop) {
      this.speed = speed;
      this.loop = loop;
    }

    // A list of property names we will use.
    private static String PROPERTIES[] = new String[]{"width", "height", "x", "y", "fill"};

    @Override
    public String apply(IMotion b) {
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
                          "fill=\"freeze\"></animate>\n",
                  PROPERTIES[j],
                  SVGAnimationView.getTime(b.getStartTime(), speed, true),
                  SVGAnimationView.getTime(b.getEndTime() - b.getStartTime(), speed, false),
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
  private static class SVGShapeVisitor implements IShapeVisitor<String> {

    /**
     * A little private method to return a suffix for what should be the svg element.
     * You just need to add at the beginning.
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
}