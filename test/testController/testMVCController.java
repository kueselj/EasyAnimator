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

public class testMVCController {

  IAnimatorModel model;
  MVCController controller;
  IAnimatorView view;

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
    model.addMotion("R", new BasicMotion(0, 100,
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


  }

  @Test
  public void testRenameShape() {


  }

  @Test
  public void testRemoveKeyFrame() {

  }

  @Test
  public void testSetKeyFrame() {

  }

  @Test
  public void testSetView() {

  }

}
