package cs3500.easyanimator.provider.model;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;

public class IAnimatorModelToProviderModel implements ProviderModel {
  private  IAnimatorModel adaptee;
  private int tick;


  @Override
  public void addShape(AShape shape) {

    int sX = shape.getPos().getX();
    int sY = shape.getPos().getY();
    Point sPoint = new Point(sX, sY);
    Color sColor = new Color(shape.getColor().getRed(),
            shape.getColor().getGreen(),
            shape.getColor().getBlue());
    int width = (int) shape.getXSize();
    int height = (int) shape.getYSize();
    WidthHeight sWidthHeight = new WidthHeight(width, height);
    IShape shapeToAdd;

    //TODO REPLACE WITH VISITOR.
    if (shape instanceof PRectangle) {
      shapeToAdd = new Rectangle(sWidthHeight, sPoint, sColor);
    }

    else if (shape instanceof POval) {
      shapeToAdd = new Oval(sWidthHeight, sPoint, sColor);
    }
    else shapeToAdd = null;

    adaptee.addShape(shape.name, shapeToAdd);

  }

  @Override
  public void removeShape(String name) {
    adaptee.removeShape(name);
  }

  @Override
  public void addAnimation(AAnimation a) {

    String id = a.getTarget().getName();

    //TODO How to i get the motion fields
    adaptee.addMotion(a, null);


  }

  @Override
  public void removeAnimation(AAnimation a) {
    String id = a.getTarget().getName();
    //TODO How to i get the motion fields.
    adaptee.removeMotion(id, null);

  }

  @Override
  public String getTextualAnimationView() {
    return null;
  }

  @Override
  public void tick(int time) {

    //TODO - In the model, the tick method essentially just dispatches the update to each of the
    // active animations within the model. The time variable in this case,
    // would technically be delta time, representing essentially what 'tick' of the animation
    // we are on. This way the actual code within the AAnimation class can determine if/when
    // it needs to stop running.

    if (time == adaptee.getMaxTick()) {
      this.tick = 0;
    }
    else this.tick++;
  }

  //TODO - In the model, the finished method is really just a simple method which looks through
  // the state of everything in the animation and determines if there is any animation left.
  // This would mostly be used to detect if the animation should be repeated in the case that
  // the user decides to loop it when completed.
  @Override
  public boolean finished(int time) {
    return false;
  }

  @Override
  public void addKeyFrame(AShape target, String s) {
    //TODO how to process s? We have no idea what this looks like.
    adaptee.addKeyframe();

  }
}
