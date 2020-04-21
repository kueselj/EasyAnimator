package cs3500.easyanimator.layersimplementation.controller;

import cs3500.easyanimator.layersimplementation.view.ILayerView;
import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.IAnimatorModel;

import cs3500.easyanimator.model.Point;

import cs3500.easyanimator.model.ILayeredAnimatorModel;
import cs3500.easyanimator.model.layers.ILayer;
import cs3500.easyanimator.model.shapes.*;

import javax.swing.Timer;
import java.util.*;

/**
 * Controller specifically for the layer implementation of a model.
 */
public class LayerMVCController implements ILayerMVCController,
        PlaybackControls, EditorControls, LayerControls {

  private Timer timer;
  private int tick;
  private double speed;
  private boolean looping;

  private ILayeredAnimatorModel model;
  private ILayerView view;

  private ILayer currentLayer;

  /**
   * Basic constructor that takes in a model and a view for the controller to use.
   * @param model the model to use.
   * @param view the view to use.
   */
  public LayerMVCController(ILayeredAnimatorModel model, ILayerView view) {
    this.model = model;
    this.view = view;
    this.tick = 0;
    this.looping = false;
    // We use the value negative -1 for the speed as a way to mark the speed as uninitialized.
    this.view.addPlaybackControls(this);
    this.view.addEditorControls(this);
    this.view.addLayerControls(this);

    // Start timer with the speed of view.
    this.timer = new Timer(0, e -> this.refreshTick());
    this.timer.stop();
  }

  @Override
  public void go() {
    if (this.speed == -1.0) {
      throw new IllegalArgumentException("Can't start if speed has not been set.");
    }

    // We update the shapes and select a layer before going visible.
    currentLayer = model.getLayer(0); // We expect at least one layer.
    updateShapes();
    view.makeVisible();
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
    // Update the shapes that should be drawn. This goes at start so first frame has it.
    view.setDrawShapes(model.getShapesAtTick(this.tick));
    view.setTickLabel(this.tick);
  }

  @Override
  public void setSpeed(double tps) {
    this.speed = Math.max(tps, 1);
    if (this.timer != null) {
      this.timer.setDelay((int) Math.max(1, (1.0 / speed * 1000.0)));
    }
  }

  // PLAYBACK CONTROLS

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

  // EDITOR FEATURES

  private static IShapeFactory SHAPE_FACTORY = new BasicShapeFactory();
  private static WidthHeight DEFAULT_WH = new WidthHeight(100, 100);
  private static Point DEFAULT_POS = new Point(100, 100);
  private static Color DEFAULT_COL = new Color(100, 100, 100);

  @Override
  public void addShape(String shapeName, String shapeType) {
    // We make sure that there isn't a same name in the entire stack of layers.
    // This isn't a piece of state that the model itself can maintain, instead as users we must.
    // Whilst the editor is unaffected if we don't, it does affect text views.
    IAnimatorModel layerModel = currentLayer.getModel();
    // If we aren't replacing a shape in the current layer,
    // then we'd be adding a shape with the same name.
    // We error out in this specific case.
    if (model.getShapeNames().contains(shapeName) &&
            !layerModel.getShapeNames().contains(shapeName)) {
      System.out.println("Unable to add shape with the same name as another in a different layer.");
      view.makeErrorSound();
      return;
    }
    // We passed that check, time to update / add the shape.
    try {
      layerModel.addShape(shapeName,
              SHAPE_FACTORY.getShape(shapeType, DEFAULT_WH, DEFAULT_POS, DEFAULT_COL));
      updateShapes();
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
      updateShapes();
    } catch (IllegalArgumentException iae) {
      view.makeErrorSound();
    }
  }

  @Override
  public void renameShape(String name, String newName, String shapeType) {
    // This is a trickier situation than calling something in the model.
    IAnimatorModel layerModel = currentLayer.getModel();

    // We won't rename a non-existent shape.
    List<String> shapeNames = layerModel.getShapeNames();
    if (!shapeNames.contains(name)) {
      System.out.println("Unable to rename non-existent shape.");
      view.makeErrorSound();
      return;
    }

    // We also make sure that there isn't a same name in the entire stack of layers.
    if (model.getShapeNames().contains(newName)) {
      System.out.println("Unable to add shape with the same name as another in a different layer.");
      view.makeErrorSound();
      return;
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
      updateShapes();
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
      updateKeyframeSelector(shapeName);

    } catch (IllegalArgumentException iae) {
      view.makeErrorSound();
    }
  }

  @Override
  public void deleteKeyFrame(String shapeName, String tickOfKeyFrame) {

    IAnimatorModel layerModel = currentLayer.getModel();

    try {
      layerModel.removeKeyframe(shapeName, Integer.parseInt(tickOfKeyFrame));
      updateKeyframeSelector(shapeName);
    } catch (IllegalArgumentException iae) {
      view.makeErrorSound();
    }
  }

  private IShapeVisitor<String> getName = new ShapeNameVisitor();

  @Override
  public void selectShape(String shapeName) {

    IAnimatorModel layerModel = currentLayer.getModel();

    // If the shape doesn't exist then we don't bother updating fields.
    if (!layerModel.getShapes().containsKey(shapeName)) {
      return;
    }
    // Shape fields.
    view.setCurrentShape(shapeName);
    //shapeName.setText(name);

    view.setShapeType(layerModel.getShapes().get(shapeName).accept(getName));

    // Keyframe fields.
    updateKeyframeSelector(shapeName);
    if (layerModel.getKeyframes(shapeName).size() > 0) {
      selectTick(shapeName, layerModel.getKeyframes(shapeName).firstKey());
    }
    // We don't select a keyframe if there are no keyframes.
    // This will leave the keyframes fields on whatever they were previously.
    // I'm okay with this.
  }

  /**
   * A helper method to update the shape selector.
   */
  private void updateShapes() {

    IAnimatorModel layerModel = currentLayer.getModel();

    List<String> shapesToDisplay = new ArrayList<String>();
    for (String name: layerModel.getShapeNames()) {
      shapesToDisplay.add(name);
    }
    view.setAvailableShapes(shapesToDisplay);
  }

  /**
   * A helper method to update the keyframe selector.
   * @param name  The name of the shape to use to lookup keyframes.
   */
  private void updateKeyframeSelector(String name) {

    IAnimatorModel layerModel = currentLayer.getModel();

    List<String> ticksToDisplay = new ArrayList<String>();
    for (Integer tick: layerModel.getKeyframes(name).keySet()) {
      ticksToDisplay.add(Integer.toString(tick));
    }
    view.setAvailableTicks(ticksToDisplay);
  }

  private static String DEFAULT = "100";

  @Override
  public void selectTick(String shapeName, Integer tick) {

    IAnimatorModel layerModel = currentLayer.getModel();

    String w = DEFAULT;
    String h = DEFAULT;
    String x = DEFAULT;
    String y = DEFAULT;
    String r = DEFAULT;
    String g = DEFAULT;
    String b = DEFAULT;
    // If we have actually selected the New Keyframe we just want to use the playback tick.
    if (tick == null) {
      tick = this.tick;
    }
    if (shapeName == null || !layerModel.getShapeNames().contains(shapeName)) {
      return;
    }
    SortedMap<Integer, IShape> keyframes = layerModel.getKeyframes(shapeName);
    if (keyframes.size() > 0) {
      IShape state;
      SortedMap<Integer, IShape> tailMap = keyframes.tailMap(tick);
      SortedMap<Integer, IShape> headMap = keyframes.headMap(tick);
      if (keyframes.containsKey(tick)) {
        state = keyframes.get(tick);
        // I have this extra from the bottom since I think getShapeAtTick may have rounding errors.
      } else if (tailMap.size() > 0 && headMap.size() > 0) {
        state = layerModel.getShapeAtTick(shapeName, tick);
      } else if (tailMap.size() > 0) {
        state = keyframes.get(tailMap.firstKey());
      } else {
        state = keyframes.get(headMap.firstKey());
      }
      w = Integer.toString(state.getSize().getWidth());
      h = Integer.toString(state.getSize().getHeight());
      x = Integer.toString(state.getPosition().getX());
      y = Integer.toString(state.getPosition().getY());
      r = Integer.toString(state.getColor().getRed());
      g = Integer.toString(state.getColor().getGreen());
      b = Integer.toString(state.getColor().getBlue());
    }
    // We do NOT set the selected tick. That must be done somewhere else.

    List<String> components = new ArrayList<String>();
    components.addAll(Arrays.asList(Integer.toString(tick), x, y, w, h, r, g, b));
    view.setTextFields(components);

  }
}
