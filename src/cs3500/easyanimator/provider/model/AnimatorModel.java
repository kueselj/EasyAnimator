package cs3500.easyanimator.provider.model;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.WidthHeight;

import java.util.List;

public class AnimatorModel implements ProviderModel, AdapterInterface {
  private  IAnimatorModel adaptee;
  private int tick;
  public int canvasX;
  public int canvasY; // These need to be set on model construction.
  public int yOffset;
  public int xOffset; // These too need to be set on model construction.

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
    if (shape instanceof Rectangle) {
      shapeToAdd = new cs3500.easyanimator.model.shapes.Rectangle(sWidthHeight, sPoint, sColor);
    } else if (shape instanceof Oval) {
      shapeToAdd = new cs3500.easyanimator.model.shapes.Oval(sWidthHeight, sPoint, sColor);
    } else {
      shapeToAdd = null;
    }

    adaptee.addShape(shape.name, shapeToAdd);
  }

  @Override
  public void removeShape(String name) {
    adaptee.removeShape(name);
  }

  @Override
  public void addAnimation(AAnimation a) {
    throw new UnsupportedOperationException("Unable to add animations from this adapted model.");
  }

  @Override
  public void removeAnimation(AAnimation a) {
    throw new UnsupportedOperationException("Unable to remove animations from this adapted model.");
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
    throw new UnsupportedOperationException("Unable to add keyframes to this adapted model.");
  }

  public List<IShape> getShapes() {
    return adaptee.getShapesAtTick(this.tick);
    //TODO convert to their version of shapes!
  }

  @Override
  public int getTick() {
    return this.tick;
  }
}
