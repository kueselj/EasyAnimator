package testmodel.testeasyanimator;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.Point;
import org.junit.Test;

import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A test suite for an implementation of the model interface. This is abstract in anticipation of
 * different implementations.
 */
public abstract class AnimatorModelTest {

  /**
   * Get a fresh model instance for the implementing model to test.
   * @return  A newly constructed instance of the model ready to use.
   */
  abstract IAnimatorModel model();

  // I implement the classes within this class to avoid polluting our test folder with classes.
  /**
   * A test class to set the IAnimatorModel tests onto the EasyAnimator implementation.
   */
  public static class EasyAnimatorTest extends AnimatorModelTest {
    @Override
    IAnimatorModel model() {
      return new EasyAnimator();
    }
  }

  // When we have an implementing class this with be evaluated for each test suite instance.
  // We also have this variable because these tests were originally written with this variable
  //  equal to a specific implementation of the EasyAnimator class.
  IAnimatorModel model = model();

  /**
   * Tests what happens when adding valid shapes to the model. We depend on getShapes to verify that
   * it worked. We also test mutation and replacement here. We also depend on getMotions to verify
   * replacement.
   */
  @Test
  public void testAddValidShapes() {
    assertEquals("Expected no shapes to be in a newly constructed model.",
            0, model.getShapes().size());
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255, 0, 0));
    Rectangle r = new Rectangle(new WidthHeight(100, 100),
            new Point(100, 100), new Color(0,0,255));
    model.addShape("C", c); // We add an oval named C.
    model.addShape("R", r); // We add a rectangle named R.
    assertEquals("Expected the # of shapes in the model to be 2 after addition.",
            0, model.getShapes().size(), 2);
    // We add a motion for this shape so we can use it later.
    model.addMotion("R", new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(100, 100),
            new Point(100, 100), new Point(100, 100),
            new Color(0,0,255), new Color(0,0,255)));

    // I am unsure if we can exactly test that the shapes are now definitely in the model with the
    // same properties.
    // We CAN test that whatever properties are in there are not the result of mutation.
    r.setColor(new Color(0,0,0));
    assertNotEquals("Expected the rectangle in the model to be unmutated.",
            new Color(0,0,0), model.getShapes().get("R").getColor());

    // We then want to verify the next behavior of addShape, replacement with the same key.
    // We mutated r, now lets add it in.
    model.addShape("R", r);
    assertEquals("Expected the # of shapes to stay the same, used addShape for replacement.",
            model.getShapes().size(), 2);
    assertEquals("Expected motions after replacement to remain the same",
            1, model.getMotions().get("R").size());
    assertEquals("Expected the rectangle in the model to have the new color.",
            new Color(0,0,0), model.getShapes().get("R").getColor());
  }

  /**
   * Tests that when attempting to add an invalid shapes (uninitialized) then the proper exception
   * is thrown.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddNullShape() {
    model.addShape("valid", null);
  }

  /**
   * Tests that when attempting to add a shape with an invalid id (uninitialized) then the proper
   * exception is thrown.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddShapeNullId() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255,0,0));
    model.addShape(null, c);
  }

  /**
   * Tests that it is possible to remove a named shape with a valid id.
   */
  @Test
  public void testRemoveShapeValidId() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255,0,0));
    model.addShape("C", c);
    Rectangle r = new Rectangle(new WidthHeight(100, 100),
            new Point(100, 100), new Color(0,0,255));
    model.addShape("R", r);
    assertEquals("Verify that adding two shapes brings the total number to 2.",
            2, model.getShapes().size());
    model.removeShape("R");
    assertEquals("Then after removing a shape, we expected the total number to be 1.",
            1, model.getShapes().size());
    assertTrue("Since we removed the rectangle named R," +
            " we expect the key for the circle named C to still exist.",
            model.getShapes().containsKey("C"));
    assertFalse("Since we removed the rectangle named R," +
            " we expect the key for the rectangle named R to not exist anymore.",
            model.getShapes().containsKey("R"));
  }

  /**
   * Tests that when attempting to removeShape with an id that is uninitialized yields an Illegal
   * ArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveShapeNullId() {
    model.removeShape(null);
  }

  /**
   * Tests that when attempting to removeShape with an id that doesn't exist in the model,
   * then we get an illegal argument exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveShapeInvalidId() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255,0,0));
    model.addShape("C", c);
    model.removeShape("R");
  }

  /**
   * Tests that after adding a shape, we have a blank list of motions. Then after removal that list
   * doesn't exist anymore.
   */
  @Test
  public void testAddRemoveShapeMotions() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255,0,0));
    model.addShape("C", c);
    assertTrue("Expected new added shape to have a motion list.",
            model.getMotions().containsKey("C"));
    assertEquals("Expected the new motion list to be empty.",
            0, model.getMotions().get("C").size());
    model.addMotion("C", new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(120, 120),
            new Point(100, 100), new Point(100, 100),
            new Color(100, 100, 100), new Color(100, 100, 100)));
    model.removeShape("C");
    assertFalse("After removing the shape, we expect it to no longer have a motion list.",
            model.getMotions().containsKey("C"));
  }

  /**
   * Tests that after adding motions, they are retrievable with getMotion.
   */
  @Test
  public void testAddValidMotion() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255,0,0));
    model.addShape("C", c);
    model.addMotion("C", new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(120, 120),
            new Point(100, 100), new Point(100, 100),
            new Color(0,0,255), new Color(0,0,255)));
    assertEquals("After adding a motion expected the motion list for the shape to be size 1.",
            1, model.getMotions().get("C").size());
    assertEquals("After adding a motion we expect properties of the motion in the model to match" +
            " what we added.", 10, model.getMotions().get("C").get(0).getEndTime());
    // We should be able to do
  }

  /**
   * Tests that the proper exception is thrown for adding an invalid (uninitialized) motion.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddNullMotion() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255,0,0));
    model.addShape("C", c);
    model.addMotion("C", null);
  }

  /**
   * Tests that the proper exception is thrown for adding motions with an invalid id (uninit).
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddMotionNullId() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255,0,0));
    model.addShape("C", c);
    model.addMotion(null, new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(100, 100),
            new Point(100, 100), new Point(100, 100),
            new Color(255,0,0), new Color(0,0,255)));
  }

  /**
   * Tests that the proper exception is thrown for adding motions with an id that doesn't exist
   * in the model.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddMotionInvalidId() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255,0,0));
    model.addShape("C", c);
    model.addMotion("R", new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(100, 100),
            new Point(100, 100), new Point(100, 100),
            new Color(255,0,0), new Color(0,0,255)));
  }

  /**
   * Tests that it is impossible to add motions that cause us to defy invariants (clashing tick
   * ranges, different start and end points). We should get an IllegalArgumentException every time.
   */
  @Test
  public void testAddInvalidMotions() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255,0,0));
    model.addShape("C", c);

    // We add a basic motion that changes color.
    BasicMotion middle = new BasicMotion(10, 20,
            new WidthHeight(100, 100), new WidthHeight(100, 100),
            new Point(100, 100), new Point(100, 100),
            new Color(100, 100, 100), new Color(200, 200, 200));
    model.addMotion("C", middle);

    // We then attempt to add a motion that conflicts in terms of tick range.
    try {
      BasicMotion left = new BasicMotion(0, 11,
              new WidthHeight(100, 100), new WidthHeight(100, 100),
              new Point(100, 100), new Point(100, 100),
              new Color(200, 200, 200), new Color(100, 100, 100));
      model.addMotion("C", left);
      fail("Expected IllegalArgumentException to complain about conflicting time ranges.");
    } catch (IllegalArgumentException iae) {
      // Success!
    }

    // We then attempt to add a motion that conflicts in terms of tick range, but on the other side.
    try {
      BasicMotion right = new BasicMotion(19, 30,
              new WidthHeight(100, 100), new WidthHeight(100, 100),
              new Point(100, 100), new Point(100, 100),
              new Color(200, 200, 200), new Color(100, 100, 100));
      model.addMotion("C", right);
      fail("Expected IllegalArgumentException to complain about conflicting time ranges.");
    } catch (IllegalArgumentException iae) {
      // Success!
    }

    // We then attempt to add a motion that conflicts in terms of end state.
    try {
      // The difference is end WidthHeight.
      BasicMotion left = new BasicMotion(0, 5,
              new WidthHeight(100, 100), new WidthHeight(0, 0),
              new Point(100, 100), new Point(100, 100),
              new Color(200, 200, 200), new Color(100, 100, 100));
      model.addMotion("C", left);
      fail("Expected IllegalArgumentException to complain about conflicting states.");
    } catch (IllegalArgumentException iae) {
      // Success!
    }

    // We then attempt to add a motion that conflicts in terms of start state.
    try {
      // The difference is start WidthHeight.
      BasicMotion right = new BasicMotion(30, 40,
              new WidthHeight(100, 100), new WidthHeight(0, 0),
              new Point(0, 0), new Point(100, 100),
              new Color(200, 200, 200), new Color(100, 100, 100));
      model.addMotion("C", right);
      fail("Expected IllegalArgumentException to complain about conflicting states.");
    } catch (IllegalArgumentException iae) {
      // Success!
    }

    // We could vary it up to test all possible parameters of a motion but that would be tiring ...
  }

  /**
   * Tests that it is possible to remove a motion and that it behaves correctly when the parameters
   * are correct.
   */
  @Test
  public void testRemoveMotion() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255,0,0));
    model.addShape("C", c);
    // We change the size.
    BasicMotion b = new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(200, 200),
            new Point(100, 100), new Point(100, 100),
            new Color(100, 100, 100), new Color(100, 100, 100));
    // We add the motion.
    model.addMotion("C", b);
    // We change the color with another motion.
    BasicMotion b2 = new BasicMotion(10, 20,
            new WidthHeight(200, 200), new WidthHeight(200, 200),
            new Point(100, 100), new Point(100, 100),
            new Color(100, 100, 100), new Color(200, 200, 200));
    model.addMotion("C", b2);
    // Let's verify where that puts us now.
    assertEquals("Expected to verify the number of motions in the model as 2.",
            2, model.getMotions().get("C").size());

    // Can we remove the first motion?
    // Since motions are immutable we can remove with the reference we have right now.
    model.removeMotion("C", b);
    assertEquals("Expected the number of motions after removal in the model to be 1.",
            1, model.getMotions().get("C").size());
    assertEquals("Expected the remaining motion after removal to be our second motion.",
            b2, model.getMotions().get("C").get(0));
  }

  /**
   * Tests that when attempting to removeMotion with an id that is uninitialized yields an Illegal
   * ArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveMotionNullId() {
    BasicMotion b = new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(200, 200),
            new Point(100, 100), new Point(100, 100),
            new Color(100, 100, 100), new Color(100, 100, 100));
    model.removeMotion(null, b);
  }

  /**
   * Tests that when attempting to removeMotion with an id that doesn't exist in the model,
   * then we get an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveMotionInvalidId() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255,0,0));
    BasicMotion b = new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(200, 200),
            new Point(100, 100), new Point(100, 100),
            new Color(100, 100, 100), new Color(100, 100, 100));
    model.addShape("C", c);
    model.addMotion("C", b);
    model.removeMotion("R", b);
  }

  /**
   * Tests that when attempting to remove a motion that creates a situation where we need to
   * violate our invariants (only mismatching states are now possible) then we throw an Illegal
   * ArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testRemoveMotionInvalidState() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Point(100, 100), new Color(255,0,0));
    // We change the size here.
    BasicMotion left = new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(200, 200),
            new Point(100, 100), new Point(100, 100),
            new Color(100, 100, 100), new Color(100, 100, 100));
    // Then we change the color.
    BasicMotion middle = new BasicMotion(10, 20,
            new WidthHeight(200, 200), new WidthHeight(200, 200),
            new Point(100, 100), new Point(100, 100),
            new Color(100, 100, 100), new Color(200, 200, 200));
    // Then we change the position.
    BasicMotion right = new BasicMotion(20, 30,
            new WidthHeight(200, 200), new WidthHeight(200, 200),
            new Point(100, 100), new Point(200, 200),
            new Color(200, 200, 200), new Color(200, 200, 200));

    model.addShape("C", c);
    model.addMotion("C", left);
    model.addMotion("C", middle);
    model.addMotion("C", right);
    // Now if we remove the middle, we actually have a broken state.
    model.removeMotion("C", middle);
  }

  // This was moved outside the model interface.
  // @Test
  // public void testTextOutput() {}

  /**
   * Tests that when attempting to set the canvas for the model with an uninitialized top left
   * corner that we abort with an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSetCanvasUninitializedTopLeftCorner() {
    model.setCanvas(null, new WidthHeight(100, 100));
  }

  /**
   * Tests that when attempting to set the canvas for the model with an uninitialized widthHeight
   * that we abort with an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSetCanvasUninitializedWidthHeight() {
    model.setCanvas(new Point(0, 0), null);
  }

  /**
   * Tests that when attempting to get the canvas position from the model before it has been set is
   * disallowed with an IllegalStateException.
   */
  @Test(expected = IllegalStateException.class)
  public void testGetCanvasPositionUninitialized() {
    model.getCanvasPosition();
  }

  /**
   * Tests that when attempting to get the canvas size from the model before it has been set is
   * disallowed with an IllegalStateException.
   */
  @Test(expected = IllegalStateException.class)
  public void testGetCanvasSizeUninitialized() {
    model.getCanvasSize();
  }

  /**
   * Tests that after invoking setCanvas that the result is that it's updated in the model.
   */
  @Test
  public void testSetCanvasWorks() {
    model.setCanvas(new Point(10, 10), new WidthHeight(100, 100));
    assertEquals(model.getCanvasPosition(), new Point(10, 10));
    assertEquals(model.getCanvasSize(), new WidthHeight(100, 100));
  }

  // Instructor interpretation tests. We had a different idea of how shapes would work.

  /**
   * Test that the model is compatible with the style of motions provided by instructors.
   * Specifically a 1-tick long motions for teleports.
   */
  @Test
  public void testOneTickTeleport() {
    // These ideas of motions are a little frustrating.
    // IMO 2-tick long motions e.g starting at tick = 1, and ending at t = 2 [1,2)
    //  make more sense.
    // But we have to be compatible with their inputs, so this is what we get.
    WidthHeight startWH = new WidthHeight(100, 100);
    WidthHeight endWH = new WidthHeight(200, 200);
    Color startColor = new Color(200, 200, 200);
    Point startPoint = new Point(200, 200);
    model.addShape("sample", new Rectangle(startWH, startPoint, startColor));
    model.addMotion("sample", new BasicMotion(1, 1,
            startWH, startWH,
            startPoint, startPoint,
            startColor, startColor));
    model.addMotion("sample", new BasicMotion(1, 10,
            startWH, endWH,
            startPoint, startPoint,
            startColor, startColor));
  }

  // New utility method checks.

  /**
   * A small set of tests around the getMaxTick method.
   */
  @Test
  public void testGetMaxTick() {
    // We don't throw exceptions because it's possible that the model doesn't have any shapes
    //  and isn't in some sort of illegal state.
    WidthHeight widthHeight = new WidthHeight(100, 100);
    Point point = new Point(100, 100);
    Color color = new Color(255, 255, 255);
    model.addShape("sample", new Rectangle(widthHeight,
            point, color));
    assertEquals("The max tick of a model without any motions should be 0 (n.a).",
            0, model.getMaxTick());
    IMotion m = new BasicMotion(0, 100,
            widthHeight, widthHeight,
            point, point,
            color, color);
    model.addMotion("sample", m);
    assertEquals("The max tick of a model with one motion should be " +
                    "the end time of the motion.",
            m.getEndTime(), model.getMaxTick());
    IMotion m2 = new BasicMotion(100, 200,
            widthHeight, widthHeight,
            point, point,
            color, color);
    model.addMotion("sample", m2);
    assertEquals("The max tick of a model with two motions should be " +
                    "the end time of the latest motion.",
            m2.getEndTime(), model.getMaxTick());
    model.addShape("sample2", new Oval(widthHeight, point, color));
    IMotion m3 = new BasicMotion(0, 300,
            widthHeight, widthHeight,
            point, point,
            color, color);
    model.addMotion("sample2", m3);
    assertEquals("The max tick of a model with many motions should be " +
                    "the end time of the latest motion for any shape.",
            m3.getEndTime(), model.getMaxTick());
  }

  /**
   * A small set of tests around the getShapeMaxTick method. This is very similar to getMaxTick
   * tests except we don't add new shapes.
   */
  @Test
  public void testGetShapeMaxTick() {
    WidthHeight widthHeight = new WidthHeight(100, 100);
    Point point = new Point(100, 100);
    Color color = new Color(255, 255, 255);
    try {
      this.model.getShapeMaxTick("sample");
      fail("Expected to fail with an exception when fetching the max tick of a nonexistent shape.");
    } catch (IllegalArgumentException iae) {
      // This was supposed to happen.
    }

    model.addShape("sample", new Rectangle(widthHeight,
            point, color));
    assertEquals("The max tick of a shape without any motions should be 0 (n.a).",
            0, model.getShapeMaxTick("sample"));
    IMotion m = new BasicMotion(0, 100,
            widthHeight, widthHeight,
            point, point,
            color, color);
    model.addMotion("sample", m);
    assertEquals("The max tick of a shape with one motion should be " +
                    "the end time of the motion.",
            m.getEndTime(), model.getMaxTick());
    IMotion m2 = new BasicMotion(100, 200,
            widthHeight, widthHeight,
            point, point,
            color, color);
    model.addMotion("sample", m2);
    assertEquals("The max tick of a shape with two motions should be " +
                    "the end time of the latest motion.",
            m2.getEndTime(), model.getMaxTick());
  }
}
