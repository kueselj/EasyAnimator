package cs3500.easyanimator.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Map;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.BasicShapeFactory;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeFactory;
import cs3500.easyanimator.model.shapes.ShapeF;
import cs3500.easyanimator.model.shapes.WidthHeight;
import cs3500.easyanimator.view.IAnimatorView;

/**
 * A MVCController is a model view controller implementation tailored towards an editor view.
 */
public class MVCController implements IController, EditorListener {

  IAnimatorModel model;
  IAnimatorView view;

  /**
   * Create a new controller with the given model and given view.
   * @param model The model to create the controller for.
   */
  public MVCController(IAnimatorModel model) {
    this.model = model;

  }

  private static IShapeFactory SHAPE_FACTORY = new BasicShapeFactory();
  private static WidthHeight DEFAULT_WH = new WidthHeight(100, 100);
  private static Point DEFAULT_POS = new Point(100, 100);
  private static Color DEFAULT_COL = new Color(100, 100, 100);


  @Override
  public boolean addShape(String name, String type) {
    viewCheck();
    try {
      this.model.addShape(name,
              SHAPE_FACTORY.getShape(type, DEFAULT_WH, DEFAULT_POS, DEFAULT_COL));
    } catch (IllegalArgumentException iae) {
      iae.printStackTrace();
      // Something failed.
      return false;
    }
    return true;
  }

  @Override
  public boolean removeShape(String name) {
    viewCheck();
    try {
      model.removeShape(name);
    } catch (IllegalArgumentException iae) {
      return false;
    }
    return true;
  }

  @Override
  public boolean renameShape(String name, String newName, String shapeType) {
    viewCheck();
    // This is a trickier situation than calling something in the model.

    // We won't rename ontop another shape. Or rename a non-existent shape.
    List<String> shapeNames = model.getShapeNames();
    if (!shapeNames.contains(name) || shapeNames.contains(newName)) {
      return false;
    }

    try {
      // Fetch old data.
      Map<Integer, IShape> oldKeyframes = model.getKeyframes(name);
      // Create new recipient.
      model.addShape(newName, model.getShapes().get(name));
      for (Map.Entry<Integer, IShape> keyframe: oldKeyframes.entrySet()) {
        model.addKeyframe(newName, keyframe.getValue(), keyframe.getKey());
      }
      // Done with old shape.
      model.removeShape(name);
    } catch (IllegalArgumentException iae) {
      return false;
    }
    return true;
  }

  @Override
  public boolean removeKeyframe(String name, int keyframe) {
    viewCheck();
    try {
      model.removeKeyframe(name, keyframe);
    } catch (IllegalArgumentException iae) {
      return false;
    }
    return true;
  }

  @Override
  public boolean setKeyframe(String name, int tick, String w, String h, String x, String y, String r, String g, String b) {
    viewCheck();
    try {
      if (!model.getShapeNames().contains(name)) {
        return false;
      }
      IShape keyframe = model.getShapes().get(name).accept(new ShapeF(
              new WidthHeight(Integer.parseInt(w), Integer.parseInt(h)),
              new Point(Integer.parseInt(x), Integer.parseInt(y)),
              new Color(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b))));
      model.addKeyframe(name, keyframe, tick);
    } catch (IllegalArgumentException iae) {
      return false;
    }
    return true;
  }

  /**
   * A small helper method to refuse to do anything without the view set.
   */
  private void viewCheck() {
    if (this.view == null) {
      throw new IllegalStateException("Unable to carry out this operation " +
              "without the view set in the controller.");
    }
  }

  @Override
  public void setView(IAnimatorView view, double speed) {
    if (view == null) {
      throw new IllegalArgumentException("Unable to do anything with a null view.");
    }
    this.view = view;
    this.view.setSpeed(speed);
    this.view.setModel(this.model);
  }

  @Override
  public void go() {
    viewCheck();
    this.view.makeVisible();
  }

  @Override
  public void saveModel() {
    System.out.println("Saved!");

  }

  @Override
  public void loadModel() {
    System.out.println("Loaded!");
  }

}
