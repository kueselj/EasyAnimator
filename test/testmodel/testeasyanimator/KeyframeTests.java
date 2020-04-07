package testmodel.testeasyanimator;

import org.junit.Test;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;

import static org.junit.Assert.assertEquals;

/**
 * A collection of tests pertaining to if the model can take in new keyframes.
 */
public abstract class KeyframeTests {

  /**
   * Creates a test suite specifically for the EasyAnimator implementation.
   */
  public static class EasyAnimatorTests extends KeyframeTests {
    @Override
    IAnimatorModel model() {
      return new EasyAnimator();
    }
  }

  /**
   * Get the model for this specific implementation.
   * @return  The model for this implementation, a new one.
   */
  abstract IAnimatorModel model();

  // A collection of constants I use in my tests.
  WidthHeight SMALL = new WidthHeight(100, 100);
  WidthHeight LARGE = new WidthHeight(200, 200);
  Point HERE = new Point(0, 0);
  Point THERE = new Point(100, 100);
  Color RED = new Color(255, 0, 0);
  Color BLUE = new Color(0, 0, 255);

  /**
   * A test to verify that adding a keyframe adds a keyframe, AND means we have at least one motion.
   */
  @Test
  public void testAddKeyframe() {
    IAnimatorModel model = model();
    IShape start = new Rectangle(SMALL, HERE, RED);
    IShape end = new Rectangle(LARGE, THERE, BLUE);
    model.addShape("example", start);

    // One keyframe.
    model.addKeyframe("example", start, 0);
    assertEquals("Expected one keyframe from when we added the keyframe.",
            1, model.getKeyframes("example").size());
    assertEquals("Expected that one keyframe to have the properties we wanted.",
            start, model.getKeyframes("example").get(0));

    // Another one. DJ Khalid.
    model.addKeyframe("example", end, 100);
    assertEquals("Expected two keyframes from when we added the second keyframe.",
            2, model.getKeyframes("example").size());
    assertEquals("Expected that one keyframe to have the properties we wanted.",
            end, model.getKeyframes("example").get(100));

    // Now that we have two keyframes (with different states) we expect one motion.
    assertEquals("Expected one motion to be created from two keyframes.",
            1, model.getSortedMotions().get("example").size());
    assertEquals("Expected motion to match the keyframes added.",
            new BasicMotion(0, 100,
                    start.getSize(), end.getSize(),
                    start.getPosition(), end.getPosition(),
                    start.getColor(), end.getColor()),
            model.getSortedMotions().get("example").first());
  }

  WidthHeight MED = new WidthHeight(150, 150);
  Point MID = new Point(50, 50);
  Color IN_BETWEEN = new Color(255 / 2, 0, 255 / 2);

  /**
   * A test to verify how adding a keyframe intermixes with adding a motion.
   */
  @Test
  public void testAddKeyframeAndMotion() {
    IAnimatorModel model = model();
    IShape start = new Rectangle(SMALL, HERE, RED);
    IShape middle = new Rectangle(MED, MID, IN_BETWEEN);
    IShape end = new Rectangle(LARGE, THERE, BLUE);
    model.addShape("example", start);

    model.addMotion("example", new BasicMotion(0, 100,
            start.getSize(), end.getSize(),
            start.getPosition(), end.getPosition(),
            start.getColor(), end.getColor()));

    model.addKeyframe("example", middle, 50);

    assertEquals("We should have three different keyframes.",
            3, model.getKeyframes("example").size());
    assertEquals("We should have two motions.",
            2, model.getSortedMotions().get("example").size());
    assertEquals("Expected first motion to match the start and middle keyframes.",
            new BasicMotion(0, 50,
                    start.getSize(), middle.getSize(),
                    start.getPosition(), middle.getPosition(),
                    start.getColor(), middle.getColor()),
            model.getSortedMotions().get("example").first());

    assertEquals("Expected second motion to match the middle and end keyframes.",
            new BasicMotion(50, 100,
                    middle.getSize(), end.getSize(),
                    middle.getPosition(), end.getPosition(),
                    middle.getColor(), end.getColor()),
            model.getSortedMotions().get("example").last());
  }
}
