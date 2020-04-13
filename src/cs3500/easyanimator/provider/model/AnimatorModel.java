package cs3500.easyanimator.provider.model;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.WidthHeight;

import java.util.ArrayList;
import java.util.List;

public class AnimatorModel implements ProviderModel, AdapterInterface {
  private  IAnimatorModel adaptee;
  private int tick;
  public int canvasX;
  public int canvasY; // These need to be set on model construction.
  public int xOffset;
  public int yOffset; // These too need to be set on model construction.


  /**
   * Constructor for an AnimatorModelAdapter, takes in the adaptee,
   * and a bunch of ints to deal with canvas size and position.
   * @param adaptee the adaptee model to use.
   * @param canvasX the x variable of the canvas.
   * @param canvasY the y variable of the canvas.
   * @param xOffset the x offset of the canvas.
   * @param yOffset the y offset of the canvas.
   */
  public AnimatorModel(IAnimatorModel adaptee, int canvasX, int canvasY, int xOffset, int yOffset) {
    this.adaptee = adaptee;
    this.tick = 0;
    this.canvasX = canvasX;
    this.canvasY = canvasY;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
  }

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

    //TODO REPLACE WITH VISITOR, but actually, we cant update their code soooo.
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

  /**
   * Returns the list of available shapes at the current tick.
   * @return the list of shapes at the current tick.
   */
  public List<AShape> getShapes() {
    List<IShape> adapteeShapes = adaptee.getShapesAtTick(this.tick);
    List<AShape> targetShapes = new ArrayList<AShape>();

    for (IShape s: adapteeShapes) {
      targetShapes.add(s.accept(new ShapeToProviderShape()));
    }
    return targetShapes;
  }

  @Override
  public int getTick() {
    return this.tick;
  }

  //TODO make this an adapter instead? Probably! Will need one for both shape types tho.
  /**
   * Private helper visitor that converts model shape implementation into the provider shape.
   */
  private class ShapeToProviderShape implements IShapeVisitor<AShape> {

    @Override
    public AShape applyToRectangle(cs3500.easyanimator.model.shapes.Rectangle r) {

      Pos2D pos = new Pos2D(r.getPosition().getX(), r.getPosition().getY());
      java.awt.Color col = new java.awt.Color(r.getColor().getRed(),
              r.getColor().getGreen(),
              r.getColor().getBlue());
      int width = r.getSize().getWidth();
      int height = r.getSize().getHeight();

      return new Rectangle("", pos, col, width, height);
    }

    @Override
    public AShape applyToOval(cs3500.easyanimator.model.shapes.Oval o) {
      Pos2D pos = new Pos2D(o.getPosition().getX(), o.getPosition().getY());
      java.awt.Color col = new java.awt.Color(o.getColor().getRed(),
              o.getColor().getGreen(),
              o.getColor().getBlue());
      int width = o.getSize().getWidth();
      int height = o.getSize().getHeight();

      return new Oval("", pos, col, width, height);
    }
  }
}
