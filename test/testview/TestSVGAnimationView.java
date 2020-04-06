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
import static org.junit.Assert.assertNotNull;
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
    System.out.println(out.toString());
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
  // The namespace.
  private static String XMLNS = "http://www.w3.org/2000/svg";

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
    assertEquals("Expected svg element to be version" + SVG_VERSION,
            SVG_VERSION,
            doc.getDocumentElement().getAttribute("version"));
    assertEquals("Expected svg element to contain xmls property.",
            XMLNS,
            doc.getDocumentElement().getAttribute("xmlns"));
  }

  /**
   * Verifies that the given node contains an animate element with the following properties.
   * @param e             The node to check.
   * @param from          The start value.
   * @param to            The end value.
   * @param begin         The begin of the animate.
   * @param dur           The duration of the animate.
   */
  private void verifyAnimation(Element e,
                       String attributeName,
                       String from, String to,
                       String begin, String dur) {
    NodeList list = e.getChildNodes();
    for (int i = 0; i < list.getLength(); i++){
      Node item = list.item(i);
      if (item.getNodeType() == Node.ELEMENT_NODE) {
        Element child = (Element) item;
        if (attributeName.equals(child.getAttribute("attributeName")) &&
                from.equals(child.getAttribute("from")) &&
                to.equals(child.getAttribute("to")) &&
                begin.equals(child.getAttribute("begin")) &&
                dur.equals(child.getAttribute("dur"))) {
          // We found it.
          return;
        }
      }
    }

    fail("Unable to find an animate element with the given properties.");
  }

  /**
   * Fetch an element by it's unique id in the document by searching the children of the given root.
   * @param id      The id of the element to search for.
   * @param root    The root to start searching at.
   * @return  The element or null.
   */
  private Element getElementById(String id, Element root) {
    NodeList list = root.getChildNodes();
    for (int i = 0; i < list.getLength(); i++) {
      Node item = list.item(i);
      if (item.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) item;
        if (id.equals(element.getAttribute("id"))) {
          return element;
        }
      }
    }
    return null;
  }

  /**
   * Tests the standard shapes at 30 FPS.
   */
  @Test
  public void testStandardShapeAnimates() {
    initialize();
    standardShapes();
    Document doc = parseDocument();

    Element svg = doc.getDocumentElement();

    // First we test for elements.

    Element rectangle = getElementById("R", svg);
    assertNotNull("Expected there to exist an element with an id of R.",
            rectangle);
    assertEquals("100",
            rectangle.getAttribute("width"));
    assertEquals("50",
            rectangle.getAttribute("height"));
    assertEquals("100",
            rectangle.getAttribute("x"));
    assertEquals("100",
            rectangle.getAttribute("y"));
    assertEquals("rgb(200, 0, 0)",
            rectangle.getAttribute("fill"));

    Element ellipse = getElementById("C", svg);
    assertNotNull("Expected there to exist an element with an id of C.",
            ellipse);
    assertEquals("200",
            ellipse.getAttribute("rx"));
    assertEquals("100",
            ellipse.getAttribute("ry"));
    assertEquals("1100",
            ellipse.getAttribute("cx"));
    assertEquals("800",
            ellipse.getAttribute("cy"));
    assertEquals("rgb(0, 0, 200)",
            ellipse.getAttribute("fill"));

    // Now we check their motions.
    // I don't really mind extraneous motions, but I'm not going to test for them.
    // First the rectangle, we changed it's color.
    verifyAnimation(rectangle,
            "fill",
            "rgb(200, 0, 0)",
            "rgb(0, 0, 200)",
            "base.begin+0ms",
            "1000ms");
    verifyAnimation(rectangle,
            "fill",
            "rgb(0, 0, 200)",
            "rgb(200, 0, 0)",
            "base.begin+1000ms",
            "1000ms");

    // Next the ellipse, we changed it's size.
    // This should trigger a position change since the center will be different.
    verifyAnimation(ellipse,
            "rx",
            "200",
            "50",
            "base.begin+0ms",
            "2000ms");
    verifyAnimation(ellipse,
            "ry",
            "100",
            "25",
            "base.begin+0ms",
            "2000ms");

    verifyAnimation(ellipse,
            "cx",
            "1100",
            "950",
            "base.begin+0ms",
            "2000ms");
    verifyAnimation(ellipse,
            "cy",
            "800",
            "725",
            "base.begin+0ms",
            "2000ms");
  }

  /**
   * Tests the standard shapes at 60 FPS. We test a snipper of the normal standard
   * shapes test suite to verify times have shrunk.
   */
  @Test
  public void testStandardShapesAnimatesSpeedup() {
    initialize();
    standardShapes();
    view.setSpeed(60);
    Document doc = parseDocument();
    Element svg = doc.getDocumentElement();
    Element rectangle = getElementById("R", svg);
    assertNotNull("Expected there to exist an element with an id of R.",
            rectangle);

    verifyAnimation(rectangle,
            "fill",
            "rgb(200, 0, 0)",
            "rgb(0, 0, 200)",
            "base.begin+0ms",
            "500ms");
    verifyAnimation(rectangle,
            "fill",
            "rgb(0, 0, 200)",
            "rgb(200, 0, 0)",
            "base.begin+500ms",
            "500ms");
  }
}
