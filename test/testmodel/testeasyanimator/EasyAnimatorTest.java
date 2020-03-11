package testmodel.testeasyanimator;

import org.junit.Test;

import java.awt.*;

import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test class for EasyAnimator methods.
 */
public class EasyAnimatorTest {
  IAnimatorModel model = new EasyAnimator();

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
            Color.RED, new Point(100, 100));
    Rectangle r = new Rectangle(new WidthHeight(100, 100),
            Color.BLUE, new Point(100, 100));
    model.addShape("C", c); // We add an oval named C.
    model.addShape("R", r); // We add a rectangle named C.
    assertEquals("Expected the # of shapes in the model to be 2 after addition.",
            0, model.getShapes().size(), 2);
    // We add a motion for this shape so we can use it later.
    model.addMotion("R", new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(100, 100),
            new Point(100, 100), new Point(100, 100),
            Color.BLUE, Color.BLUE));

    // I am unsure if we can exactly test that the shapes are now definitely in the model with the
    // same properties.
    // We CAN test that whatever properties are in there are not the result of mutation.
    r.setColor(Color.BLACK);
    assertNotEquals("Expected the rectangle in the model to be unmutated.",
            Color.RED, model.getShapes().get("R").getColor());

    // We then want to verify the next behavior of addShape, replacement with the same key.
    // We mutated r, now lets add it in.
    model.addShape("R", r);
    assertEquals("Expected the # of shapes to stay the same, used addShape for replacement.",
            model.getShapes().size(), 2);
    assertEquals("Expected motions after replacement to remain the same",
            1, model.getMotions().get("R").size());
    assertNotEquals("Expected the rectangle in the model to have the new color.",
            Color.BLACK, model.getShapes().get("R").getColor());
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
            Color.RED, new Point(100, 100));
    model.addShape(null, c);
  }

  /**
   * Tests that it is possible to remove a named shape with a valid id.
   */
  @Test
  public void testRemoveShapeValidId() {
    Oval c = new Oval(new WidthHeight(100, 100),
            Color.RED, new Point(100, 100));
    model.addShape("C", c);
    Rectangle r = new Rectangle(new WidthHeight(100, 100),
            Color.BLUE, new Point(100, 100));
    model.addShape("R", r);
    assertEquals("Verify that adding two shape brings the total number to 2.",
            1, model.getShapes().size());
    model.removeShape("R");
    assertEquals("Then after removing a shape, we expected the total number to be 1.",
            1, model.getShapes().size());
    assertTrue("Since we removed the circle named C," +
            " we expect the key for the rectangle named R to still exist.",
            model.getShapes().containsKey("R"));
    assertFalse("Since we removed the circle named C," +
            " we expect the key for the circle named C to not exist anymore.",
            model.getShapes().containsKey("C"));
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
            Color.RED, new Point(100, 100));
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
            Color.RED, new Point(100, 100));
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
            Color.RED, new Point(100, 100));
    model.addShape("C", c);
    model.addMotion("C", new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(120, 120),
            new Point(100, 100), new Point(100, 100),
            Color.BLUE, Color.BLUE));
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
            Color.RED, new Point(100, 100));
    model.addShape("C", c);
    model.addMotion("C", null);
  }

  /**
   * Tests that the proper exception is thrown for adding motions with an invalid id (uninit).
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddMotionNullId() {
    Oval c = new Oval(new WidthHeight(100, 100),
            Color.RED, new Point(100, 100));
    model.addShape("C", c);
    model.addMotion(null, new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(100, 100),
            new Point(100, 100), new Point(100, 100),
            Color.RED, Color.BLUE));
  }

  /**
   * Tests that the proper exception is thrown for adding motions with an id that doesn't exist
   * in the model.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddMotionInvalidId() {
    Oval c = new Oval(new WidthHeight(100, 100),
            Color.RED, new Point(100, 100));
    model.addShape("C", c);
    model.addMotion("R", new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(100, 100),
            new Point(100, 100), new Point(100, 100),
            Color.RED, Color.BLUE));
  }

  /**
   * Tests that it is impossible to add motions that cause us to defy invariants (clashing tick
   * ranges, different start and end points). We should get an IllegalArgumentException every time.
   */
  @Test
  public void testAddInvalidMotions() {
    Oval c = new Oval(new WidthHeight(100, 100),
            Color.RED, new Point(100, 100));
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
            Color.RED, new Point(100, 100));
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
            Color.RED, new Point(100, 100));
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
            Color.RED, new Point(100, 100));
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
            new WidthHeight(100, 100), new WidthHeight(200, 200),
            new Point(100, 100), new Point(200, 200),
            new Color(200, 200, 200), new Color(200, 200, 200));

    model.addShape("C", c);
    model.addMotion("C", left);
    model.addMotion("C", middle);
    model.addMotion("C", right);
    // Now if we remove the middle, we actually have a broken state.
    model.removeMotion("C", middle);
  }
}
