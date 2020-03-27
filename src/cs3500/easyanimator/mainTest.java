package cs3500.easyanimator;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;
import cs3500.easyanimator.view.IAnimatorView;
import cs3500.easyanimator.view.SwingView;

public class mainTest {

  public static void main(String[] args) {

    IAnimatorModel model = new EasyAnimator();


    Oval c = new Oval(new WidthHeight(100, 100),
            new Color(255, 0, 0), new Point(100, 100));
    Rectangle r = new Rectangle(new WidthHeight(100, 100),
            new Color(0,0,255), new Point(100, 100));
    model.addShape("C", c); // We add an oval named C.
    model.addShape("R", r); // We add a rectangle named R.

    // We add a motion for this shape so we can use it later.
    model.addMotion("C", new BasicMotion(0, 200,
            new WidthHeight(100, 50), new WidthHeight(50, 100),
            new Point(0, 0), new Point(400, 400),
            new Color(90,150,40), new Color(10,25,100)));

    // We add a motion for this shape so we can use it later.
    model.addMotion("C", new BasicMotion(200, 500,
            new WidthHeight(50, 100), new WidthHeight(100, 50),
            new Point(400, 400), new Point(0, 0),
            new Color(10,25,100), new Color(90,150,40)));

    // We add a motion for this shape so we can use it later.
    model.addMotion("R", new BasicMotion(0, 100,
            new WidthHeight(200, 50), new WidthHeight(50, 200),
            new Point(400, 400), new Point(0, 0),
            new Color(20,40,255), new Color(255,0,0)));

    // We add a motion for this shape so we can use it later.
    model.addMotion("R", new BasicMotion(100, 200,
            new WidthHeight(50, 200), new WidthHeight(200, 50),
            new Point(0, 0), new Point(400, 400),
            new Color(255,0,0), new Color(20,40,255)));


    IAnimatorView view = new SwingView();
    view.setModel(model);
    view.makeVisible();

  }
}
