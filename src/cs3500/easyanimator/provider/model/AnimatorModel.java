package cs3500.easyanimator.provider.model;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.WidthHeight;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class from an IAnimatorModel(Our code) to an AnimatorModel(Provider code). Must have
 * name Animator model as class type instead of interface was used in the provider code. This
 * Implementation must also keep track of the current tick in the animation as that was integral
 * to the providers implementation.
 */
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

    // While we would have liked to use a visitor, we did not get that from the provider.
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
    throw new UnsupportedOperationException("Unable to request textual animation view. " +
            "It is unnecessary for the editor view.");
  }

  @Override
  public void tick(int time) {
    this.tick += time;
  }

  @Override
  public boolean finished(int time) {
    return time == this.adaptee.getMaxTick();
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

  /**
   * This method is required by some views in order to compile but not specified by the interface.
   * We suppress this behavior.
   * @throws UnsupportedOperationException Since this method is unnecessary for the provided editor.
   * @return Not a string.
   */
  public String toSVG() {
    throw new IllegalArgumentException("This method is unused by the editor, " +
            "and so should not be called.");
  }

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
      int width = Math.max(r.getSize().getWidth(), 1);
      int height = Math.max(r.getSize().getHeight(), 1);

      return new Rectangle("rect", pos, col, width, height);
    }

    @Override
    public AShape applyToOval(cs3500.easyanimator.model.shapes.Oval o) {
      Pos2D pos = new Pos2D(o.getPosition().getX(), o.getPosition().getY());
      java.awt.Color col = new java.awt.Color(o.getColor().getRed(),
              o.getColor().getGreen(),
              o.getColor().getBlue());
      // The provided classes do not allow shapes of sizes zero.
      // So we make the minimum one.
      int width = Math.max(o.getSize().getWidth(), 1);
      int height = Math.max(o.getSize().getHeight(), 1);

      return new Oval("oval", pos, col, width, height);
    }
  }
}
