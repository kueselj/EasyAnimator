package testmodel.testlayers;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.ILayeredAnimatorModel;
import cs3500.easyanimator.model.LayeredAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.layers.BasicLayer;
import cs3500.easyanimator.model.layers.ILayer;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * A test suite around layered models. This is abstract in order to be generalized to any
 * layered model implementation. For layers we will use BasicLayer's
 * and will use EasyAnimators for models.
 */
public abstract class TestILayeredAnimatorModelTest {
  /**
   * Returns a new instance of the layered animator model.
   * @return  A fresh instance of a model.
   */
  abstract ILayeredAnimatorModel getModel();

  /**
   * A class meant to instantiate tests for the LayeredAnimatorModel implementation.
   */
  public static class TestLayeredAnimatorModel extends TestILayeredAnimatorModelTest {

    @Override
    ILayeredAnimatorModel getModel() {
      return new LayeredAnimatorModel();
    }
  }

  // First we start with the read-only methods.

  private static WidthHeight SMALL = new WidthHeight(100, 100);
  private static Point ORIGIN = new Point(0, 0);
  private static Color RED = new Color(255, 0, 0);
  private static WidthHeight LARGE = new WidthHeight(200, 200);
  private static Point ELSEWHERE = new Point(100, 100);
  private static Color BLUE = new Color(0, 0, 255);

  // We have two in order to test animations later.
  private static Rectangle RECT = new Rectangle(SMALL, ORIGIN, RED);
  private static Rectangle RECT_TWO = new Rectangle(LARGE, ELSEWHERE, BLUE);
  private static Oval OVAL = new Oval(SMALL, ORIGIN, RED);
  private static Oval OVAL_TWO = new Oval(LARGE, ELSEWHERE, BLUE);

  private static ILayer LAYER_ONE;
  private static ILayer LAYER_TWO;

  static {
    IAnimatorModel modelOne = new EasyAnimator();
    modelOne.addShape("rectangle", RECT);
    modelOne.addKeyframe("rectangle", RECT, 0);
    modelOne.addKeyframe("rectangle", RECT_TWO, 100);
    modelOne.setCanvas(ELSEWHERE, SMALL);
    LAYER_ONE = new BasicLayer("TOP", true, modelOne);

    IAnimatorModel modelTwo = new EasyAnimator();
    modelTwo.addShape("oval", OVAL);
    modelTwo.addKeyframe("oval", OVAL, 25);
    modelTwo.addKeyframe("oval", OVAL_TWO, 75);
    modelTwo.setCanvas(ORIGIN, LARGE);
    LAYER_TWO = new BasicLayer("BOTTOM", true, modelTwo);
  }

  /**
   * A small helper method to give an instance of a model with two layers.
   * With two different shapes. We add keyframes at times 0 and 100 for the rectangle,
   * and 25 and 75 for the oval. Additionally, we set an overarching canvas.
   * @return  A model with two layers, one containing a rectangle, the other a circle.
   */
  private ILayeredAnimatorModel doubleLayered() {
    ILayeredAnimatorModel model = getModel();
    model.addLayer(LAYER_ONE);
    model.addLayer(LAYER_TWO);

    return model;
  }

  /**
   * A test to verify that get layers works as intended on the standard model setup. This also
   * tests getLayer.
   */
  @Test
  public void testGetLayers() {
    ILayeredAnimatorModel model = doubleLayered();
    assertEquals("Expected there to be two layers in the model.",
            2, model.getLayers().size());
    assertEquals("Expected the first layer in the list to be the first one we added.",
            LAYER_ONE, model.getLayers().get(0));
    assertEquals("Expected the layer at the 0th index to be the first one we added.",
            LAYER_ONE, model.getLayer(0));
    assertEquals("Expected the second layer in the list to be the second one we added.",
            LAYER_TWO, model.getLayers().get(1));
    assertEquals("Expected the layer at the 1st index to be the second one we added.",
            LAYER_TWO, model.getLayer(1));

  }

  /**
   * A small rule to check that when attempting to get a layer at an index that doesn't exist
   * yields an exception.
   */
  @Test
  public void testGetInvalidLayer() {
    ILayeredAnimatorModel model = doubleLayered();
    try {
      model.getLayer(2);
      fail("Expected to fail when fetching a layer index that doesn't exist.");
    } catch (IllegalArgumentException iae) {
      // Nice.
    }

    try {
      model.getLayer(-1);
      fail("Expected to fail when fetching a layer index that doesn't exist.");
    } catch (IllegalArgumentException iae) {
      // Nice.
    }
  }

  // Now for backwards-compatibility read-only methods.

  // The below test means we need some tricky behavior when it comes to getShapesAtTick.
  // We will need to adjust shapes with a new offset.

  /**
   * A test to verify that getCanvasSize returns the size of the canvas required to view all shapes.
   * And that getCanvasPosition returns the top-left most point of all the layers.
   */
  @Test
  public void testCanvasDimensions() {
    ILayeredAnimatorModel model = doubleLayered();
    // Our canvas needs to stretch from offset (0,0) in layer two,
    // to 100 pixels forward from offset (100, 100). That means a w/h of 200.
    assertEquals("Expected the canvas size to be 200x200 to contain all the layers.",
            new WidthHeight(200, 200), model.getCanvasSize());
    assertEquals("Expected the canvas position to be the top-left most point. 0,0",
            ORIGIN, model.getCanvasPosition());
  }

  /**
   * A test to verify that getShapes returns a collection of all the shapes from all the layers.
   */
  @Test
  public void testGetShapes() {
    ILayeredAnimatorModel model = doubleLayered();
    assertTrue("Expected shapes map to contain both our rectangle and our oval.",
            model.getShapes().containsKey("rectangle") &&
                    model.getShapes().containsKey("oval"));
  }

  /**
   * A test to verify that getShapeKeyframeTicks still works as expected (assuming there are
   * no name conflicts). Since getShapesAtTick is just a packaging of this behavior so it is going
   * untested.
   */
  @Test
  public void testGetShapeKeyframeTicks() {
    // Something subtle is happening here.
    // The oval is coming from a model that has an offset of 100, 100.
    // That means the oval, which is located at 0,0 will come out of the model at -100, -100.
    // In this version, it is still at the origin where it should be.
    // That means we need to compute offsets for the shapes coming out of each layer.
    ILayeredAnimatorModel model = doubleLayered();
    assertEquals("Expected the rectangle shape at tick 0 to match what we had set for it.",
            RECT, model.getShapeAtTick("rectangle", 0));
    assertEquals("Expected the rect shape at tick 100 to match what we had set for it.",
            RECT_TWO, model.getShapeAtTick("rectangle", 100));
    assertEquals("Expected the oval shape at tick 25 to match what we had set for it.",
            OVAL, model.getShapeAtTick("oval", 25));
    assertEquals("Expected the oval shape at tick 75 to match what we had set for it.",
            OVAL_TWO, model.getShapeAtTick("oval", 75));
  }

  /**
   * A test to verify that getshapemaxtick still works. This means that the sorted motion will have
   * to search it's layers for the shape name.
   */
  @Test
  public void testShapeMaxTick() {
    ILayeredAnimatorModel model = doubleLayered();
    assertEquals("Expected max tick of the rectangle to be 100.",
            100, model.getShapeMaxTick("rectangle"));
    assertEquals("Expected max tick of the oval to be 75 as well.",
            75, model.getShapeMaxTick("oval"));
  }

  // I don't get getSortedMotions because really the methods we are now looking to use are below.

  /**
   * A test to verify that getKeyframes combines the results from all the layers.
   */
  @Test
  public void testShapeGetKeyframes() {
    ILayeredAnimatorModel model = doubleLayered();
    // The model will have to search it's layers for these things.
    assertEquals("Expected the keyframes returned to contain an entry for the rectangle.",
            2, model.getKeyframes("rectangle").size());
    assertEquals("Expected the keyframes returned to contain an entry for the oval.",
            2, model.getKeyframes("oval").size());
  }

  // Finally, we have some tests are methods to edit layers,
  // and the effects of that on other methods.

  /**
   * A test to verify that add layer correctly throws an IllegalArgumentException for uninitialized
   * inputs.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testBadAddLayer() {
    ILayeredAnimatorModel model = getModel();
    model.addLayer(null);
  }

  /**
   * A test to verify that add layer's effects are correctly shown in other methods.
   */
  @Test
  public void testAddLayerWorks() {
    ILayeredAnimatorModel model = getModel();
    model.addLayer(LAYER_TWO);
    model.addLayer(LAYER_ONE);
    // Since I've added layers in this order, I expect the oval to always appear first.
    assertEquals("Expected oval to be the first shape in the list due to layer order.",
            "oval", model.getShapeNames().get(0));
    assertEquals("Expected oval to be the first shape when fetching shapes at tick " +
            "due to layer order. This means it's on the bottom.",
            OVAL, model.getShapesAtTick(25).get(0)); // Has to be tick 25, when oval first appears.
    assertEquals("Expected the first layer in getLayers to be LAYER_TWO " +
            "since we added it first in this test.",
            LAYER_TWO, model.getLayer(0));
  }

  /**
   * A test to verify that giving bad inputs to either signatures of removeLayer results in an
   * IllegalArgumentException.
   */
  @Test
  public void testBadRemoveLayer() {
    ILayeredAnimatorModel model = doubleLayered();

    // Out of bounds tests.
    try {
      model.removeLayer(-1);
      fail("Expected to fail trying to remove a layer out of bounds.");
    } catch (IllegalArgumentException iae) {
      // Success!
    }
    try {
      model.removeLayer(2);
      fail("Expected to fail trying to remove a layer out of bounds.");
    } catch (IllegalArgumentException iae) {
      // Success!
    }

    // Null parameter test.
    try {
      model.removeLayer(null);
      fail("Expected to fail trying to remove an uninitialized layer.");
    } catch (IllegalArgumentException iae) {
      // Success!
    }
  }

  /**
   * A test to verify that both signatures of removeLayer work as intended.
   */
  @Test
  public void testRemoveLayerWorks() {
    // First we give ourselves two layers.
    ILayeredAnimatorModel model = doubleLayered();
    // Let's remove both layers. We will remove both by removing at index 0.
    model.removeLayer(0);
    assertEquals("Expected number of layers to be 1 after removing 1.",
            1, model.getLayers().size());
    assertEquals("Expected layer removed to be layer one by order of layer addition. " +
                    "The one remaining should have been layer two, now at index 0.",
            LAYER_TWO, model.getLayer(0)); // We test the remaining layer.
    model.removeLayer(0);
    assertEquals("Expected no layers after removing both.",
            0, model.getLayers().size());
    // Time for a reset.
    model = doubleLayered();
    model.removeLayer(LAYER_TWO);
    // We've removed layer two, the only one remaining should be layer one.
    assertEquals("Expected there to be only one layer left after removing layer two.",
            1, model.getLayers().size());
    assertEquals("Expected the only layer left to be layer one.",
            LAYER_ONE, model.getLayer(0));
  }

  /**
   * A set of test rules to check that when attempting to swap layers that are out of bounds that
   * we correctly get an IllegalArgumentException.
   */
  @Test
  public void testBadSwapLayer() {
    ILayeredAnimatorModel model = new LayeredAnimatorModel();
    try {
      model.swapLayer(-1, 1);
      fail("Expected to fail swap layer method call with out of index argument 1.");
    } catch (IllegalArgumentException iae) {
      // Success! We should survive this call to get to the next one.
    }

    try {
      model.swapLayer(0, 3);
      fail("Expected to fail swap layer method call with out of index argument 2.");
    } catch (IllegalArgumentException iae) {
      // Success! We should survive this call to get to the next one.
    }
  }

  /**
   * A test to verify that swap layer works as intended.
   */
  @Test
  public void testSwapLayerWorks() {
    ILayeredAnimatorModel model = doubleLayered();
    // I can only really swap once.
    model.swapLayer(0, 1);
    assertEquals("Expected layer two be the first layer after swap.",
            LAYER_TWO, model.getLayer(0));
    assertEquals("Expected layer one be the second layer after swap.",
            LAYER_ONE, model.getLayer(1));
    // We swap again, we should get the original order.
    model.swapLayer(1, 0); // This shouldn't really matter that the order is different.
    assertEquals("Expected layer one be the first layer after two swaps.",
            LAYER_ONE, model.getLayer(0));
    assertEquals("Expected layer two be the second layer after two swaps.",
            LAYER_TWO, model.getLayer(1));
  }

  /**
   * A test to verify the affects of visibility on getShapesAtTick.
   */
  @Test
  public void testVisibilityEffects() {
    ILayeredAnimatorModel model = doubleLayered();
    ILayer layerOneInvisible = model.getLayer(0).setVisibility(false);
    model.removeLayer(0);
    model.addLayer(layerOneInvisible);
    // With one invisible shape, at tick 25 (when the oval first appears) we should get only
    // one shape, the oval.
    assertEquals("After making layer one invisible, we expected only one shape to be visible.",
            1, model.getShapesAtTick(25).size());
    assertEquals("After making layer one invisible, we expected to only get the oval.",
            OVAL, model.getShapesAtTick(25).get(0));

  }

  /**
   * A test to verify the new addLayer (with an index) fails with bad arguments (out of range
   * indices or a null layer).
   */
  @Test
  public void testAddLayerWithIndexBadArguments() {
    ILayeredAnimatorModel model = doubleLayered();
    // First check null layer.
    try {
      model.addLayer(null, 0);
      fail("Expected addLayer with index to fail with uninitialized layer.");
    } catch (IllegalArgumentException iae) {
      // Nice, we wanted this.
    }
    // Now we check out of bounds indices.
    // Some valid thirdLayer.
    ILayer thirdLayer = LAYER_TWO.setName("Third Layer");
    try {
      model.addLayer(thirdLayer, -1);
      fail("Expected addLayer with index to fail with out of bounds index to the left.");
    } catch (IllegalArgumentException iae) {
      // Should be here.
    }
    try {
      model.addLayer(thirdLayer, 3);
      fail("Expected addLayer with index to fail with out of bounds index to the right.");
    } catch (IllegalArgumentException iae) {
      // This is right.
    }
  }

  /**
   * A set of rules to check that addLayer with index works as expected.
   */
  @Test
  public void testAddLayerWithIndexWorks() {
    ILayeredAnimatorModel model = doubleLayered();
    // If I add layer two again, but at index 0,
    model.addLayer(LAYER_TWO, 0);
    // then I should see it in the right spot with getLayers.
    assertEquals("Expected to get layer two at index 0 since I added it there.",
            LAYER_TWO, model.getLayers().get(0));
    assertEquals("After adding the layer, I should now have three layers.",
            3, model.getLayers().size());
    // additionally when I get shapesAtTick I should get an oval, rectangle, oval.
    assertEquals("Expected three shapes at tick 25.",
            3, model.getShapesAtTick(25).size());
    assertEquals("Expected the first shape at tick 25 to be the oval.",
            OVAL, model.getShapesAtTick(25).get(0));
    assertEquals("Expected the last shape at tick 25 to be the oval.",
            OVAL, model.getShapesAtTick(25).get(2));

  }
}
