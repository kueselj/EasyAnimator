package testController;

import cs3500.easyanimator.controller.MVCController;
import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;
import cs3500.easyanimator.view.EditorSwingView;
import cs3500.easyanimator.view.IAnimatorView;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class testMVCController {

  IAnimatorModel model;
  IAnimatorView view;
  MVCController controller;

  /**
   * Resets the controller for further testing.
   */
  public void reset() {
    model = new EasyAnimator();

    WidthHeight wh100100 = new WidthHeight(100, 100);
    WidthHeight wh50100 = new WidthHeight(50, 100);
    WidthHeight wh10050 = new WidthHeight(50, 100);
    WidthHeight wh20050 = new WidthHeight(200, 50);
    WidthHeight wh50200 = new WidthHeight(50, 200);

    Point p100100 = new Point(100, 100);
    Point p400400 = new Point(400, 400);
    Point p00 = new Point(0, 0);

    Color red = new Color(255, 0, 0);
    Color blue = new Color(0, 0, 255);
    Color otherColor = new Color(90,150,40);
    Color diffColor = new Color(10,25,100);
    Color why = new Color(20,40,255);

    Oval c = new Oval(wh100100, p100100, red);
    Rectangle r = new Rectangle(wh100100, p100100, blue);
    model.addShape("C", c);
    model.addShape("R", r);

    model.addMotion("C", new BasicMotion(0, 200,
            new WidthHeight(100, 50), new WidthHeight(50, 100),
            p00, p400400,
            otherColor, diffColor));

    model.addMotion("C", new BasicMotion(200, 500,
            wh50100, wh10050,
            p400400,p00,
            diffColor, otherColor));

    // We add a motion for this shape so we can use it later.
    model.addMotion("R", new BasicMotion(50, 100,
            wh20050, wh50200,
            p400400, p00,
            why, red));

    // We add a motion for this shape so we can use it later.
    model.addMotion("R", new BasicMotion(100, 200,
            wh50200, wh20050,
            p00, p400400,
            red, why));
    model.setCanvas(new Point(0,0), new WidthHeight(500, 500));

    controller = new MVCController(model);
    view = new EditorSwingView(controller);
    controller.setView(view, 30);

    //controller.go();
  }

  @Test
  public void testAddShape() {
    this.reset();
    try {
      model.getKeyframes("Circle2");

    } catch (IllegalArgumentException e) {
      //assert this did not work
    }
    controller.addShape("Circle2", "oval");
    assertTrue(model.getKeyframes("Circle2") != null);
  }

  @Test
  public void testRemoveShape() {
    boolean testFailed = false;
    this.reset();
    assertTrue(model.getKeyframes("C") != null);
    controller.removeShape("C");
    try {
      assertTrue(model.getKeyframes("C") == null);
    } catch (IllegalArgumentException e) {
      testFailed = true;
    }
    assertTrue(testFailed);

    //should return false if there is no shape to remove with that name.
    assertFalse(controller.removeShape("C"));
  }

  @Test
  public void testRenameShape() {
    this.reset();
    assertTrue(model.getKeyframes("C") != null);
    try {
      model.getKeyframes("C2");
      return;
    } catch (IllegalArgumentException e) {
      //this should have failed.
    }
    controller.renameShape("C", "C2", "oval");
    try {
      model.getKeyframes("C");
    } catch (IllegalArgumentException e) {
      //this should fail
    }
    assertTrue(model.getKeyframes("C2") != null);

    //can't rename a shape that doesnt exist!
    assertFalse(controller.renameShape("Hello", "thisNoExist", "oval"));
  }

  @Test
  public void testRemoveKeyFrame() {
    this.reset();

    assertTrue(model.getKeyframes("C").containsKey(200));
    controller.removeKeyframe("C", 200);
    assertFalse(model.getKeyframes("C").containsKey(200));

    //test that trying to remove a keyFrame that isn't there will return false.
    assertFalse(controller.removeKeyframe("C", 1000));
  }

  @Test
  public void testSetKeyFrame() {
    this.reset();
    assertFalse(model.getKeyframes("C").containsKey(600));

    //Make a new keyFrame, after last tick as well.
    controller.setKeyframe("C", 600,
            "50", "100", "150", "200", "10", "20", "30");
    assertTrue(model.getKeyframes("C").containsKey(600));
    assertEquals(50, model.getKeyframes("C").get(600).getSize().getWidth());
    assertEquals(100, model.getKeyframes("C").get(600).getSize().getHeight());
    assertEquals(150, model.getKeyframes("C").get(600).getPosition().getX());
    assertEquals(200, model.getKeyframes("C").get(600).getPosition().getY());
    assertEquals(10, model.getKeyframes("C").get(600).getColor().getRed());
    assertEquals(20, model.getKeyframes("C").get(600).getColor().getGreen());
    assertEquals(30, model.getKeyframes("C").get(600).getColor().getBlue());

    assertFalse(model.getKeyframes("R").containsKey(0));

    //Make a new keyFrame, before last tick as well.
    controller.setKeyframe("R", 0,
            "50", "100", "150", "200", "10", "20", "30");
    assertTrue(model.getKeyframes("C").containsKey(600));
    assertEquals(50, model.getKeyframes("R").get(0).getSize().getWidth());
    assertEquals(100, model.getKeyframes("R").get(0).getSize().getHeight());
    assertEquals(150, model.getKeyframes("R").get(0).getPosition().getX());
    assertEquals(200, model.getKeyframes("R").get(0).getPosition().getY());
    assertEquals(10, model.getKeyframes("R").get(0).getColor().getRed());
    assertEquals(20, model.getKeyframes("R").get(0).getColor().getGreen());
    assertEquals(30, model.getKeyframes("R").get(0).getColor().getBlue());


    //Override a keyFrame.
    controller.setKeyframe("C", 200,
            "40", "100", "150", "200", "10", "20", "30");
    assertTrue(model.getKeyframes("C").containsKey(600));
    assertEquals(40, model.getKeyframes("C").get(200).getSize().getWidth());
    assertEquals(100, model.getKeyframes("C").get(200).getSize().getHeight());
    assertEquals(150, model.getKeyframes("C").get(200).getPosition().getX());
    assertEquals(200, model.getKeyframes("C").get(200).getPosition().getY());
    assertEquals(10, model.getKeyframes("C").get(200).getColor().getRed());
    assertEquals(20, model.getKeyframes("C").get(200).getColor().getGreen());
    assertEquals(30, model.getKeyframes("C").get(200).getColor().getBlue());

    //invalid parameters, should not go through!
    assertFalse(controller.setKeyframe("C", 700, "10", "10", "10",
            "10", "10", "10", "300"));
  }
}
