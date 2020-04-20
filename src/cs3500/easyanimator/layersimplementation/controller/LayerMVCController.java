package cs3500.easyanimator.layersimplementation.controller;

import cs3500.easyanimator.layersimplementation.view.ILayerView;
import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.layers.BasicLayer;
import cs3500.easyanimator.model.shapes.*;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * Controller specifically for the layer implementation of a model.
 */
public class LayerMVCController implements ILayerMVCController,
        PlaybackControls, EditorControls, LayerControls {

  private Timer timer;
  private int tick;
  private double speed;
  private boolean looping;

  //TODO should really take in a layer implementation of it, this should be changed I would suggest
  //TODO adding an extra interface that extends IAnimatorModel that the new layer model implements.
  //TODO that new interface can define all new methods we need.
  private IAnimatorModel model;
  private ILayerView view;

  //TODO I think we will need to hold the current layer that is selected from the view, as we need
  //TODO to update stuff about it, alternatively we pass in the layer number for each method but
  //TODO we still need to store a layer number if the layer is in a different panel.
  private BasicLayer currentLayer;

  /**
   * Basic constructor that takes in a model and a view for the controller to use.
   * @param model the model to use.
   * @param view the view to use.
   */
  public LayerMVCController(IAnimatorModel model, ILayerView view) {
    this.model = model;
    this.view = view;
    this.tick = 0;
    this.looping = false;
    //TODO would prefer not to set to -1 but i want to make sure the user sets speed before starting
    this.speed = -1.0;
    this.view.addPlaybackControls(this);
    this.view.addEditorControls(this);
    this.view.addLayerControls(this);
  }

  @Override
  public void go() {
    if (this.speed == -1.0) {
      throw new IllegalArgumentException("Can't start if speed has not been set.");
    }

    view.makeVisible();

    //start timer with the speed of view.
    this.timer = new Timer((int)this.speed, e -> this.refreshTick());
    this.timer.stop();
  }

  @Override
  public void refreshTick() {
    //update the shapes that should be drawn. This goes at start so first frame has it.
    refreshDrawing();
    if (looping && this.tick >= model.getMaxTick()) {
      this.tick = 0;
    }
    else {
      this.tick += 1;
    }
  }

  @Override
  public void refreshDrawing() {
    //update the shapes that should be drawn. This goes at start so first frame has it.
    view.setDrawShapes(model.getShapesAtTick(this.tick));
    view.setTickLabel(this.tick);
  }

  @Override
  public void setSpeed(double tps) {
    this.speed = Math.max(tps, 1);
    this.timer.setDelay((int) Math.max(1, (1.0 / speed * 1000.0)));
  }

  //PLAYBACK CONTROLS

  @Override
  public void play() {
    if (!timer.isRunning()) {
      timer.start();
    }
  }

  @Override
  public void pause() {
    if (timer.isRunning()) {
      timer.stop();
    }
  }

  @Override
  public void restart() {
    this.tick = 0;
    if (!timer.isRunning()) {
      timer.start();
    }
  }

  @Override
  public void toggleLooping() {
    looping = !looping;
  }

  @Override
  public void increaseSpeed() {
    setSpeed(speed + 1);
  }

  @Override
  public void decreaseSpeed() {
    setSpeed(speed - 1);
  }

  @Override
  public void increaseTick() {
    if (tick == model.getMaxTick()) {
      tick = 0;
    }
    else {
      tick += 1;
    }
    refreshDrawing();
  }

  @Override
  public void decreaseTick() {
    if (tick == 0) {
      tick = model.getMaxTick();
    }
    else {
      tick -= 1;
    }
    refreshDrawing();
  }

  //EDITOR FEATURES

  private static IShapeFactory SHAPE_FACTORY = new BasicShapeFactory();
  private static WidthHeight DEFAULT_WH = new WidthHeight(100, 100);
  private static Point DEFAULT_POS = new Point(100, 100);
  private static Color DEFAULT_COL = new Color(100, 100, 100);

  @Override
  public void addShape(String shapeName, String shapeType) {
    //TODO make sure that there isnt a same name in the entire stack of layers.

    IAnimatorModel layerModel = currentLayer.getModel();

    try {
      layerModel.addShape(shapeName,
              SHAPE_FACTORY.getShape(shapeType, DEFAULT_WH, DEFAULT_POS, DEFAULT_COL));
    } catch (IllegalArgumentException iae) {
      iae.printStackTrace();
      // Something failed.
      view.makeErrorSound();
    }
  }

  @Override
  public void deleteShape(String shapeName) {
    IAnimatorModel layerModel = currentLayer.getModel();

    try {
      layerModel.removeShape(shapeName);
    } catch (IllegalArgumentException iae) {
      view.makeErrorSound();
    }
  }

  @Override
  public void renameShape(String name, String newName, String shapeType) {

    //TODO make sure that there isnt a same name in the entire stack of layers.

    // This is a trickier situation than calling something in the model.
    IAnimatorModel layerModel = currentLayer.getModel();
    // We won't rename ontop another shape. Or rename a non-existent shape.
    List<String> shapeNames = layerModel.getShapeNames();
    if (!shapeNames.contains(name) || shapeNames.contains(newName)) {
      view.makeErrorSound();
    }

    try {
      // Fetch old data.
      Map<Integer, IShape> oldKeyframes = layerModel.getKeyframes(name);
      // Create new recipient.
      layerModel.addShape(newName, layerModel.getShapes().get(name));
      for (Map.Entry<Integer, IShape> keyframe: oldKeyframes.entrySet()) {
        layerModel.addKeyframe(newName, keyframe.getValue(), keyframe.getKey());
      }
      // Done with old shape.
      layerModel.removeShape(name);
    } catch (IllegalArgumentException iae) {
      view.makeErrorSound();
    }
  }

  @Override
  public void saveKeyFrame(String shapeName,
                           String tick,
                           String x, String y,
                           String width, String height,
                           String r, String g, String b) {

    IAnimatorModel layerModel = currentLayer.getModel();

    try {
      if (!layerModel.getShapeNames().contains(shapeName)) {
        view.makeErrorSound();
      }
      IShape keyframe = layerModel.getShapes().get(shapeName).accept(new ShapeF(
              new WidthHeight(Integer.parseInt(width), Integer.parseInt(height)),
              new Point(Integer.parseInt(x), Integer.parseInt(y)),
              new Color(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b))));
      layerModel.addKeyframe(shapeName, keyframe, Integer.parseInt(tick));

    } catch (IllegalArgumentException iae) {
      view.makeErrorSound();
    }
  }

  @Override
  public void deleteKeyFrame(String shapeName, String tickOfKeyFrame) {

    IAnimatorModel layerModel = currentLayer.getModel();

    try {
      layerModel.removeKeyframe(shapeName, Integer.parseInt(tickOfKeyFrame));
    } catch (IllegalArgumentException iae) {
      view.makeErrorSound();
    }
  }

  @Override
  public void selectShape(String shapeName) {

  }

  @Override
  public void selectTick(String shapeName, String tick) {

  }
}
