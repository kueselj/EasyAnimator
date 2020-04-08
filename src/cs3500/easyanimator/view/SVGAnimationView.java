package cs3500.easyanimator.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.IAnimatorModelViewOnly;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;

/**
 * A SVGAnimationVie is a view of a model's motions in svg format. This creates a String and prints
 * it out to the appendable output and makeVisible's method call.
 */
public class SVGAnimationView implements IAnimatorView {

  private final Appendable out;
  private IAnimatorModelViewOnly model;
  private double speed;

  private static HashMap<String, String> defaultShapeProperties;
  private static HashMap<String, String> defaultAnimateProperties;

  // Staticially initialize these default maps.
  static {
    defaultAnimateProperties = new HashMap<>();
    defaultAnimateProperties.put("fill", "freeze");
    defaultAnimateProperties.put("attributeType", "xml");
    defaultShapeProperties = new HashMap<>();
    defaultShapeProperties.put("visibility", "visible");
  }

  /**
   * Create a new SVGAnimationView with the given appendable to output to.
   * @param out The output to write to.
   */
  public SVGAnimationView(Appendable out) {
    this.out = out;
  }

  /**
   * Create the string for an element.
   * @param tagName     The tag name for an element.
   * @param attributes  The attributes of that element as a map.
   * @param elements    The child elements as a list of a strings.
   * @return            Returns a string of the element.
   */
  private static String createElement(String tagName,
                                      Map<String, String> attributes,
                                      List<String> elements) {
    StringBuilder attributeStringBuilder = new StringBuilder();
    for (Map.Entry<String, String> attribute: attributes.entrySet()) {
      attributeStringBuilder.append(String.format("%s=\"%s\" ",
              attribute.getKey(), attribute.getValue()));
    }
    return String.format("<%s %s>\n%s\n</%s>",
            tagName,
            attributeStringBuilder.toString().trim(),
            elements.stream()
                    .filter(s -> !s.equals(""))
                    .collect(Collectors.joining("\n"))
                    .trim(),
            tagName);
  }

  /**
   * Give the color code for the given color instance.
   * @param c     The color to return the code of.
   * @return      A string in the format rgb(r,g,b)
   */
  private static String getColorCode(Color c) {
    return String.format("rgb(%d, %d, %d)", c.getRed(), c.getGreen(), c.getBlue());
  }

  /**
   * Get a localized time given the given tick and speed.
   * @param tick  The tick.
   * @param speed The ticks per second to interpret the tick by.
   * @return      The time in the correct format.
   */
  private static String getTime(long tick, double speed) {
    return String.format("%dms", (long) ((double) tick / speed * 1000.0));
  }

  /**
   * Create an animate element for the given motion using the functions and name.
   * @param motion  The motion to create the element from.
   * @param attributeName The attributeName to use.
   * @param from    The function used to transform the motion into a value for from.
   * @param to      The same as from but for to.
   * @param speed   The speed of animation.
   * @return        A string of animates from a motion.
   */
  private static String motionToAnimate(IMotion motion, String attributeName,
                                        Function<IMotion, String> from,
                                        Function<IMotion, String> to,
                                        double speed) {
    String fromStr = from.apply(motion);
    String toStr = to.apply(motion);
    if (!toStr.equals(fromStr)) {
      HashMap<String, String> animateAttributes = new HashMap<>(defaultAnimateProperties);
      animateAttributes.put("begin",
              "base.begin+" + getTime(motion.getStartTime(), speed));
      animateAttributes.put("dur",
              getTime(motion.getEndTime() - motion.getStartTime(), speed));

      animateAttributes.put("attributeName", attributeName);
      animateAttributes.put("from", from.apply(motion));
      animateAttributes.put("to", to.apply(motion));
      return createElement("animate", animateAttributes, Collections.emptyList());
    } else {
      return "";
    }
  }

  /**
   * Create an animate element that loops back to some initial state.
   * @param attributeName The name of the attribute to reset.
   * @param from          The field to start from.
   * @param to            The field to transform to.
   * @return              A string of an animate element.
   */
  private static String loopbackElement(String attributeName, String from, String to) {
    if (!from.equals(to)) {
      HashMap<String, String> animateAttributes = new HashMap<>(defaultAnimateProperties);
      animateAttributes.put("attributeName", attributeName);
      animateAttributes.put("begin", "base.end");
      animateAttributes.put("dur", "1ms");
      animateAttributes.put("from", from);
      animateAttributes.put("to", to);
      return createElement("animate", animateAttributes, Collections.emptyList());
    } else {
      return "";
    }
  }

  private static String SVG_VERSION = "1.1";
  private static String XMLNS = "http://www.w3.org/2000/svg";

  @Override
  public void makeVisible() {
    if (model == null) {
      throw new IllegalStateException("Unable to create the given view without a model");
    }

    // We will use the function createElement in order to craft everything.
    // The first thing we need is the svg element. Let's make that now.
    Map<String, String> svgAttributes = new HashMap<>();
    svgAttributes.put("xmlns", XMLNS);
    svgAttributes.put("version", SVG_VERSION);
    svgAttributes.put("width", Integer.toString(model.getCanvasSize().getWidth()));
    svgAttributes.put("height", Integer.toString(model.getCanvasSize().getHeight()));
    svgAttributes.put("viewBox", String.format("%d %d %d %d",
            model.getCanvasPosition().getX(), model.getCanvasPosition().getY(),
            model.getCanvasSize().getWidth(), model.getCanvasSize().getHeight()));
    List<String> svgElements = new ArrayList<>();
    // We populate the elements array with entries from the model.
    Map<String, SortedSet<IMotion>> motions = model.getSortedMotions();
    // We find the last motion in the bunch since it will affect the elements of our shapes.
    long loopback = model.getMaxTick();

    for (Map.Entry<String, IShape> shape: model.getShapes().entrySet()) {
      svgElements.add(shape.getValue().accept(
              new SVGTagCreator(shape.getKey(),
                      motions.get(shape.getKey()),
                      speed,
                      loopback)));
    }

    // The last element we need to add is the loopingline.
    HashMap<String, String> loopingLineAttrs = new HashMap<>();
    loopingLineAttrs.put("id", "loopingline");
    HashMap<String, String> loopingLineAnimateAttrs = new HashMap<>();
    loopingLineAnimateAttrs.put("id", "base");
    loopingLineAnimateAttrs.put("begin", "0;base.end");
    loopingLineAnimateAttrs.put("dur", getTime(model.getMaxTick(), speed));
    loopingLineAnimateAttrs.put("attributeName", "visibility");
    loopingLineAnimateAttrs.put("from", "hide");
    loopingLineAnimateAttrs.put("to", "hide");
    List<String> loopingLineElements = Collections.singletonList(createElement("animate",
            loopingLineAnimateAttrs, Collections.emptyList()));
    svgElements.add(createElement("line", loopingLineAttrs, loopingLineElements));

    try {
      out.append(createElement("svg", svgAttributes, svgElements));
    } catch (IOException e) {
      throw new IllegalStateException("Unable to print out svg into malformed output.");
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
   * A visitor class to take in a shape (with an id, and a list of motions) and return
   * the correct string of the shape and it's animates as children.
   */
  private static class SVGTagCreator implements IShapeVisitor<String> {
    private final String id;
    private final SortedSet<IMotion> motions;
    private final double speed;
    private final long loopback;

    /**
     * Create a new instance of the tag creator.
     * @param id      The id of the element to use.
     * @param motions The motions to put into animates.
     * @param speed   The speed of the element.
     * @param loopback  When to loop elements back.
     */
    SVGTagCreator(String id, SortedSet<IMotion> motions, double speed, long loopback) {
      this.id = id;
      this.motions = motions;
      this.speed = speed;
      this.loopback = loopback;
    }

    @Override
    public String applyToRectangle(Rectangle r) {
      HashMap<String, String> rectangleAttributes = new HashMap<>(defaultShapeProperties);
      List<String> rectangleElements = new ArrayList<>();

      if (motions.size() == 0) {
        // We just won't create the element if it has no motions.
        return "";
      }
      // Motions is sorted. So we can initialize our shape with the first state properties.
      // We will re-use this variable later.
      IMotion initialMotion = motions.first();
      IMotion lastMotion = motions.last();
      rectangleAttributes.put("id", id);
      rectangleAttributes.put("width", Integer.toString(initialMotion.getStartSize().getWidth()));
      rectangleAttributes.put("height", Integer.toString(initialMotion.getStartSize().getHeight()));
      rectangleAttributes.put("x", Integer.toString(initialMotion.getStartPosition().getX()));
      rectangleAttributes.put("y", Integer.toString(initialMotion.getStartPosition().getY()));
      rectangleAttributes.put("fill", getColorCode(initialMotion.getStartColor()));

      for (IMotion motion: motions) {
        rectangleElements.add(motionToAnimate(motion, "width", fromW, toW, speed));
        rectangleElements.add(motionToAnimate(motion, "height", fromH, toH, speed));
        rectangleElements.add(motionToAnimate(motion, "x", fromX, toX, speed));
        rectangleElements.add(motionToAnimate(motion, "y", fromY, toY, speed));
        rectangleElements.add(motionToAnimate(motion, "fill", fromColor, toColor, speed));
      }

      addVisibilityAnimates(rectangleElements,
              initialMotion.getStartTime(), lastMotion.getEndTime());

      rectangleElements.add(loopbackElement("width",
              Integer.toString(lastMotion.getEndSize().getWidth()),
              Integer.toString(initialMotion.getStartSize().getWidth())));
      rectangleElements.add(loopbackElement("height",
              Integer.toString(lastMotion.getEndSize().getHeight()),
              Integer.toString(initialMotion.getStartSize().getHeight())));
      rectangleElements.add(loopbackElement("x",
              Integer.toString(lastMotion.getEndPosition().getX()),
              Integer.toString(initialMotion.getStartPosition().getX())));
      rectangleElements.add(loopbackElement("y",
              Integer.toString(lastMotion.getEndPosition().getY()),
              Integer.toString(initialMotion.getStartPosition().getY())));
      rectangleElements.add(loopbackElement("fill",
              getColorCode(lastMotion.getEndColor()),
              getColorCode(initialMotion.getStartColor())));

      return createElement("rect", rectangleAttributes, rectangleElements);
    }

    private Function<IMotion, String> fromW = m -> "" + m.getStartSize().getWidth();
    private Function<IMotion, String> toW = m -> "" + m.getEndSize().getWidth();
    private Function<IMotion, String> fromX = m -> "" + m.getStartPosition().getX();
    private Function<IMotion, String> toX = m -> "" + m.getEndPosition().getX();
    private Function<IMotion, String> fromH = m -> "" + m.getStartSize().getHeight();
    private Function<IMotion, String> toH = m -> "" + m.getEndSize().getHeight();
    private Function<IMotion, String> fromY = m -> "" + m.getStartPosition().getY();
    private Function<IMotion, String> toY = m -> "" + m.getEndPosition().getY();

    private Function<IMotion, String> fromColor = m -> getColorCode(m.getStartColor());
    private Function<IMotion, String> toColor = m -> getColorCode(m.getEndColor());

    private Function<IMotion, String> fromRX = m -> "" + m.getStartSize().getWidth() / 2;
    private Function<IMotion, String> toRX = m -> "" + m.getEndSize().getWidth() / 2;
    private Function<IMotion, String> fromCX = m -> "" + (m.getStartPosition().getX() +
            m.getStartSize().getWidth() / 2);
    private Function<IMotion, String> toCX = m -> "" + (m.getEndPosition().getX() +
            m.getEndSize().getWidth() / 2);
    private Function<IMotion, String> fromRY = m -> "" + m.getStartSize().getHeight() / 2;
    private Function<IMotion, String> toRY = m -> "" + m.getEndSize().getHeight() / 2;
    private Function<IMotion, String> fromCY = m -> "" + (m.getStartPosition().getY() +
            m.getStartSize().getHeight() / 2);
    private Function<IMotion, String> toCY = m -> "" + (m.getEndPosition().getY() +
            m.getEndSize().getHeight() / 2);

    /**
     * Adds visibility animates if necessary.
     * @param elements  The elements to prepend or append.
     * @param initialTick The first motion tick of the element.
     * @param finalTick The final motion tick of the element
     */
    private void addVisibilityAnimates(List<String> elements, long initialTick, long finalTick) {
      if (initialTick > 0) {
        Map<String, String> toggleOnAttributes = new HashMap<>(defaultAnimateProperties);
        toggleOnAttributes.put("begin", "base.begin+0ms");
        toggleOnAttributes.put("dur", getTime(initialTick, speed));
        toggleOnAttributes.put("attributeName", "visibility");
        toggleOnAttributes.put("from", "hide");
        toggleOnAttributes.put("to", "visible");
        elements.add(0, createElement("animate",
                toggleOnAttributes, Collections.emptyList()));
      }

      if (finalTick < loopback) {
        Map<String, String> toggleOffAttributes = new HashMap<>(defaultAnimateProperties);
        toggleOffAttributes.put("begin", "base.begin+0ms");
        toggleOffAttributes.put("dur", getTime(loopback - finalTick, speed));
        toggleOffAttributes.put("attributeName", "visibility");
        toggleOffAttributes.put("from", "visible");
        toggleOffAttributes.put("to", "hide");
        elements.add(createElement("animate",
                toggleOffAttributes, Collections.emptyList()));
      }
    }

    @Override
    public String applyToOval(Oval o) {
      HashMap<String, String> ellipseAttributes = new HashMap<>(defaultShapeProperties);
      List<String> ellipseElements = new ArrayList<>();

      if (motions.size() == 0) {
        // We just won't create the element if it has no motions.
        return "";
      }
      // Motions is sorted. So we can initialize our shape with the first state properties.
      // We will re-use this variable later.
      IMotion initialMotion = motions.first();
      IMotion lastMotion = motions.last();
      ellipseAttributes.put("id", id);
      ellipseAttributes.put("rx", Integer.toString(initialMotion.getStartSize().getWidth() / 2));
      ellipseAttributes.put("ry", Integer.toString(initialMotion.getStartSize().getHeight() / 2));
      ellipseAttributes.put("cx",
              Integer.toString(initialMotion.getStartPosition().getX() +
                      initialMotion.getStartSize().getWidth() / 2));
      ellipseAttributes.put("cy",
              Integer.toString(initialMotion.getStartPosition().getY() +
                      initialMotion.getStartSize().getHeight() / 2));
      ellipseAttributes.put("fill", getColorCode(initialMotion.getStartColor()));

      for (IMotion motion: motions) {
        ellipseElements.add(motionToAnimate(motion, "rx", fromRX, toRX, speed));
        ellipseElements.add(motionToAnimate(motion, "ry", fromRY, toRY, speed));
        ellipseElements.add(motionToAnimate(motion, "cx", fromCX, toCX, speed));
        ellipseElements.add(motionToAnimate(motion, "cy", fromCY, toCY, speed));
        ellipseElements.add(motionToAnimate(motion, "fill", fromColor, toColor, speed));
      }

      // Following this we need to create three elements.
      // Something to toggle visibility on (if applicable).
      // Something to toggle visibility off (if applicable).
      // Something to loop our initial state back.
      addVisibilityAnimates(ellipseElements,
              initialMotion.getStartTime(), lastMotion.getEndTime());
      // Now we loop back to our initial state.
      ellipseElements.add(loopbackElement("rx",
              Integer.toString(lastMotion.getEndSize().getWidth() / 2),
              Integer.toString(initialMotion.getStartSize().getWidth() / 2)));
      ellipseElements.add(loopbackElement("ry",
              Integer.toString(lastMotion.getEndSize().getHeight() / 2),
              Integer.toString(initialMotion.getStartSize().getHeight() / 2)));
      ellipseElements.add(loopbackElement("cx",
              Integer.toString(lastMotion.getEndPosition().getX() +
                      lastMotion.getEndSize().getWidth() / 2),
              Integer.toString(initialMotion.getStartPosition().getX() +
                      initialMotion.getStartSize().getWidth() / 2)));
      ellipseElements.add(loopbackElement("y",
              Integer.toString(lastMotion.getEndPosition().getY() +
                      lastMotion.getEndSize().getHeight() / 2),
              Integer.toString(initialMotion.getStartPosition().getY() +
                      initialMotion.getStartSize().getHeight() / 2)));
      ellipseElements.add(loopbackElement("fill",
              getColorCode(lastMotion.getEndColor()),
              getColorCode(initialMotion.getStartColor())));


      return createElement("ellipse", ellipseAttributes, ellipseElements);
    }
  }
}