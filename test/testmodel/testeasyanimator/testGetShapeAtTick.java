package testmodel.testeasyanimator;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the getShapeAtTick and getShapesAtTick methods.
 */
public class testGetShapeAtTick {
  IAnimatorModel model = new EasyAnimator();


  @Test
  public void testGetShapeAtTick() {
    //Oval c = new Oval(new WidthHeight(100, 100),
    //new Color(255, 0, 0), new Point(100, 100));
    Rectangle r = new Rectangle(new WidthHeight(100, 100),
            new Color(100,100,100), new Point(100, 100));
    //model.addShape("C", c); // We add an oval named C.
    model.addShape("R", r); // We add a rectangle namedR.

    model.addMotion("R", new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(200, 300),
            new Point(100, 100), new Point(300, 400),
            new Color(0,0,0), new Color(50,100,200)));

    this.model.setCanvas(new Point(0,0), new WidthHeight(500, 500));

    IShape getShape = model.getShapeAtTick(5, "R");

    assertEquals(150, getShape.getSize().getWidth());
    assertEquals(200, getShape.getSize().getHeight());
    assertEquals(200, getShape.getPosition().getX());
    assertEquals(250, getShape.getPosition().getY());
    assertEquals(25, getShape.getColor().getRed());
    assertEquals(50, getShape.getColor().getGreen());
    assertEquals(100, getShape.getColor().getBlue());

  }

  @Test
  public void testGetShapesAtTick() {
    Oval c = new Oval(new WidthHeight(100, 100),
            new Color(255, 0, 0), new Point(100, 100));

    Rectangle r = new Rectangle(new WidthHeight(100, 100),
            new Color(100,100,100), new Point(100, 100));
    model.addShape("C", c); // We add an oval named C.
    model.addShape("R", r); // We add a rectangle namedR.

    model.addMotion("C", new BasicMotion(10, 20,
            new WidthHeight(100, 100), new WidthHeight(200, 200),
            new Point(100, 100), new Point(300, 300),
            new Color(0,0,0), new Color(200,200,200)));

    model.addMotion("R", new BasicMotion(0, 10,
            new WidthHeight(100, 100), new WidthHeight(200, 200),
            new Point(100, 100), new Point(300, 300),
            new Color(0,0,0), new Color(200,200,200)));

    this.model.setCanvas(new Point(0,0), new WidthHeight(500, 500));
    assertEquals(1, model.getShapesAtTick(5).size());
  }
}
