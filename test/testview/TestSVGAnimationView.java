package testview;

import org.junit.Test;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;
import cs3500.easyanimator.view.IAnimatorView;
import cs3500.easyanimator.view.SVGAnimationView;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A collection of tests to verify the correctness of our svg view. We do this by parsing the
 * document we produce (this verifies that we made something valid) and then checking all the
 * elements that we made.
 */
public class TestSVGAnimationView {
  IAnimatorView view;
  IAnimatorModel model;
  StringWriter out;

  /**
   * Initialize this instance of our test by setting up the model. We construct the view, and
   * connect the view. It is still safe to add objects to the model, because the string should only
   * be written after we use makeVisible on the view. This sets a default speed of 30 tps.
   */
  private void initialize() {
    out = new StringWriter();
    view = new SVGAnimationView(out);
    model = new EasyAnimator();
    view.setModel(model);
    view.setSpeed(30);

  }

  private static Point CANVAS_TL = new Point(100, 100);
  private static WidthHeight CANVAS_SIZE = new WidthHeight(800, 600);

  private static WidthHeight SMALL = new WidthHeight(100, 50);
  private static WidthHeight BIG = new WidthHeight(400, 200);

  private static Color RED = new Color(200, 0, 0);
  private static Color BLUE = new Color(0, 0, 200);

  private static Point TOP_LEFT = CANVAS_TL;
  private static Point BOTTOM_RIGHT = new Point(900, 700);

  /**
   * Initializes the model with a standard set of rectangles and circles that we know how to test.
   */
  private void standardShapes() {
    model.setCanvas(CANVAS_TL, CANVAS_SIZE);
    // We give the rectangle and circle some initial states.
    // These are properties we can test for.
    model.addShape("R", new Rectangle(SMALL, TOP_LEFT, RED));
    model.addShape("C", new Oval(BIG, BOTTOM_RIGHT, BLUE));
    // We give the rectangle two motions on a single variable.
    model.addMotion("R", new BasicMotion(
            0, 30, // At a speed of 1, this should be 1 seconds.
            SMALL, SMALL,
            TOP_LEFT, TOP_LEFT,
            RED, BLUE
    ));
    model.addMotion("R", new BasicMotion(
            30, 60, // At a speed of 1, this should be 1 seconds.
            SMALL, SMALL,
            TOP_LEFT, TOP_LEFT,
            BLUE, RED
    ));

    // We give the circle a single motion, but across two of the variables.
    // Width and height.
    model.addMotion("C", new BasicMotion(
            0, 60, // 2 seconds. Depends on the speed though.
            BIG, SMALL,
            BOTTOM_RIGHT, BOTTOM_RIGHT,
            BLUE, BLUE
    ));
  }

  /**
   * Produce a document from our view. This interacts with the instance fields and assumes we
   * already initialized.
   * @return  A w3 Document.
   */
  private Document parseDocument() {
    view.makeVisible();
    // We produce our document.
    InputStream outputReader = new ByteArrayInputStream(out.toString()
            .getBytes(StandardCharsets.UTF_8));
    DocumentBuilder builder;
    try {
      builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (ParserConfigurationException pce) {
      fail("Expected to be able to parse through given document. " +
              "Test setup was malformed, unable to get a parser.");
      return null;
    }

    try {
      return builder.parse(outputReader);
    } catch (IOException io) {
      fail("Expected to be able to parse through given document. " +
            "IO trouble, potentially between writing and reading.");
    } catch (SAXException se) {
      fail("Expected to be able to parse through given document. " +
            "There was potentially XML malformed-ness.");
    }
    return null;
  }

  // The string of the svg version that we saw and developed around the spec for.
  private static String SVG_VERSION = "1.1";

  /**
   * Verify that the root node is an svg element. Even for a blank document.
   */
  @Test
  public void testSVGRootElement() {
    initialize();
    model.setCanvas(CANVAS_TL, CANVAS_SIZE);
    Document doc = parseDocument();

    assertEquals("Expected document element to be svg like in the spec.",
            "svg", doc.getDocumentElement().getTagName());
    assertEquals(String.format("Expected svg element to be version %s", SVG_VERSION),
            SVG_VERSION, doc.getDocumentElement().getAttribute("version"));
  }

  static String[] ESSENTIAL_ATTRIBUTES = new String[]{"from", "to", "attributeName", "attributeType"};

  /**
   * Verify that all animate elements have a from, to, attributeName, and attributeType set.
   */
  @Test
  public void verifyAnimates() {
    initialize();
    standardShapes();
    Document doc = parseDocument();
    NodeList animates = doc.getElementsByTagName("animate");
    for (int i = 0; i < animates.getLength(); i++) {
      NamedNodeMap attributes = animates.item(i).getAttributes();
      for (int j = 0; j < ESSENTIAL_ATTRIBUTES.length; j++) {
        String essential_attribute = ESSENTIAL_ATTRIBUTES[j];
        assertTrue(String.format("Expected animate element to have attribute %s.",
                essential_attribute),
                attributes.getNamedItem(essential_attribute) != null);
      }
    }
  }

  static String ATTR_NAME_LOOKUP[] = new String[]{"id", "x", "y", "width", "height", "fill"};

  /**
   * A helper method to verify the properties of a shape element in the document.
   * @param shape The shape element to get the attributes of.
   * @param id    What the id parameter should equal.
   * @param x     What the x coordinate should equal.
   * @param y     What the y coordinate should equal.
   * @param width What the width property should equal.
   * @param height  What the height property should be.
   * @param fill  The color fill of the shape.
   */
  void verifyShapeElement(Node shape,
                          String id,
                          String x, String y,
                          String width, String height,
                          String fill) {
    // The indices of this array match that of ATTR_NAME_LOOKUP.
    String attrProperties[] = {id, x, y, width, height, fill};
    NamedNodeMap attributes = shape.getAttributes();
    for (int i = 0; i < ATTR_NAME_LOOKUP.length; i++) {
      String attrName = ATTR_NAME_LOOKUP[i];
      Node attr = attributes.getNamedItem(attrName);
      assertTrue(String.format("Expected attribute %s to exist in one of our shapes.",
              attrName),
              attr != null);
      assertEquals(String.format("Expected attribute %s to equal what we had picked out for it.",
              attrName),
              attrProperties[i], attr.getNodeValue());
    }
  }

  static String ANIMATE_ATTR_LOOKUP[] = new String[]{"attributeName", "from", "to", "begin", "dur"};

  /**
   * Verifies that the given node has the following properties.
   * @param e          The node to check.
   * @param attributeName The attributeName of the element.
   * @param from          The start value.
   * @param to            The end value.
   * @param begin         The begin of the animate.
   * @param dur           The duration of the animate.
   */
  void verifyAnimation(Element e,
                       String attributeName,
                       String from, String to,
                       String begin, String dur) {
    String attrProperties[] = {attributeName, from, to, begin, dur};
    NamedNodeMap attributes = e.getAttributes();
    for (int i = 0; i < ANIMATE_ATTR_LOOKUP.length; i++) {
      String attrName = ANIMATE_ATTR_LOOKUP[i];
      String attr = e.getAttribute(attrName);
      assertTrue(String.format("Expected animate element to have %s attribute.", attrName),
              attr != null);
      assertEquals(String.format("Expected animate element property %s to match.", attrName),
              attrProperties[i], attr);
    }
  }

  /**
   * Gets the first child of the given node.
   * @param n   The node to search.
   * @returns   The first element as an Element;
   */
  Element getFirstChild(Node n) {
    NodeList nodes = n.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
        return (Element) nodes.item(i);
      }
    }
    return null;
  }

  /**
   * Gets the first child of the given node.
   * @param n   The node to search.
   * @returns   The first element as an Element;
   */
  Element getLastChild(Node n) {
    NodeList nodes = n.getChildNodes();
    for (int i = nodes.getLength() - 1; i >= 0 ; i--) {
      if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
        return (Element) nodes.item(i);
      }
    }
    return null;
  }

  /**
   * Counts the number of children of the given node that are animate objects.
   * @param n  The node to count the children of.
   * @return      The number of children that are elements.
   */
  private int countChildren(Node n) {
    int sum = 0;
    NodeList nodes = n.getChildNodes();
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        sum += 1;
      }
    }
    return sum;
  }

  /**
   * Verify a pre-built animation has all the elements we expect. We assume the 30 TPS with this
   * version.
   */
  @Test
  public void testStandardShapeElements() {
    initialize();
    standardShapes();
    // We are at 30 TPS.
    Document doc = parseDocument();
    // First we verify our shapes are there.
    Node rectangle = doc.getDocumentElement().getElementsByTagName("rect").item(0);
    assertTrue("Expected to be able to find rectangle element in svg node.",
            rectangle != null);
    Node oval = doc.getDocumentElement().getElementsByTagName("ellipse").item(0);
    assertTrue("Expected to be able to find ellipse element in svg node.",
            oval != null);
    // Let's verify their properties.
    verifyShapeElement(rectangle, "R",
            Integer.toString(TOP_LEFT.getX()),
            Integer.toString(TOP_LEFT.getY()),
            Integer.toString(SMALL.getWidth()),
            Integer.toString(SMALL.getHeight()),
            "rgb(200, 0, 0)");
    verifyShapeElement(oval, "C",
            Integer.toString(BOTTOM_RIGHT.getX()),
            Integer.toString(BOTTOM_RIGHT.getY()),
            Integer.toString(BIG.getWidth()),
            Integer.toString(BIG.getHeight()),
            "rgb(0, 0, 200)");
    // Let's verify their animations.
    // The only children of these nodes (for now since searching is hard),
    // should be our animate elements.
    assertEquals("Expected the rectangle to have 2 (animate) children nodes.",
            2, countChildren(rectangle));
    assertEquals("Expected the oval to have 2 (animate) children nodes.",
            2, countChildren(oval));
    // Note we only have one motion for the oval, BUT it changes two values.
    // We need an animate element per property that is changed.
    // I'm assuming it is an ordered list the way it came out.
    // This will be something I have to then enforce when making it.
    verifyAnimation(getFirstChild(rectangle),
            "fill",
            "rgb(200, 0, 0)", "rgb(0, 0, 200)",
            "0ms", "1000ms");
    verifyAnimation(getLastChild(rectangle),
            "fill",
            "rgb(0, 0, 200)", "rgb(200, 0, 0)",
            "1000ms", "1000ms");
    // Now we can go verify that second shape.
    verifyAnimation(getFirstChild(oval),
            "width",
            "400", "100",
            "0ms", "2000ms");
    verifyAnimation(getLastChild(oval),
            "height",
            "200", "50",
            "0ms", "2000ms");
  }

  /**
   * Verify a pre-built animation has all the elements we expect, but after changing the TPS, the
   * times are correctly adjusted.
   */
  @Test
  public void verifyStandardShapes60TPS() {
    // Copied_PASTED code from verifyStandard but with a TWEEST.
    initialize();
    view.setSpeed(60); // We are now at 60 TPS.
    standardShapes();
    Document doc = parseDocument();
    Node rectangle = doc.getDocumentElement().getElementsByTagName("rect").item(0);
    assertTrue("Expected to be able to find rectangle element in svg node.",
            rectangle != null);
    Node oval = doc.getDocumentElement().getElementsByTagName("ellipse").item(0);
    assertTrue("Expected to be able to find ellipse element in svg node.",
            oval != null);
    assertEquals("Expected the rectangle to have 2 (animate) children nodes.",
            2, countChildren(rectangle));
    assertEquals("Expected the oval to have 2 (animate) children nodes.",
            2, countChildren(oval));
    verifyAnimation(getFirstChild(rectangle),
            "fill",
            "rgb(200, 0, 0)", "rgb(0, 0, 200)",
            "0ms", "500ms");
    verifyAnimation(getLastChild(rectangle),
            "fill",
            "rgb(0, 0, 200)", "rgb(200, 0, 0)",
            "500ms", "500ms");
    // Now we can go verify that second shape.
    verifyAnimation(getFirstChild(oval),
            "width",
            "400", "100",
            "0ms", "1000ms");
    verifyAnimation(getLastChild(oval),
            "height",
            "200", "50",
            "0ms", "1000ms");
  }

  /**
   * A test to verify that the viewbox property and the canvas match.
   */
  @Test
  public void verifyCanvasViewboxMatch() {
    initialize();
    standardShapes(); // This sets a canvas with some constants.
    Document doc = parseDocument();
    Element svg = doc.getDocumentElement();
    String viewbox = svg.getAttribute("viewBox");
    assertTrue("Expected viewBox property of svg element to be set.",
            viewbox != null);
    assertEquals("Expected viewBox property to match canvas.",
            String.format("%d %d %d %d",
                    CANVAS_TL.getX(), CANVAS_TL.getY(),
                    CANVAS_SIZE.getWidth(), CANVAS_SIZE.getHeight()),
                    viewbox);
  }

}
