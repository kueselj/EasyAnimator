package cs3500.easyanimator.layersimplementation.controller;

import cs3500.easyanimator.layersimplementation.view.ILayerView;
import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.IAnimatorModel;

import cs3500.easyanimator.model.Point;

import cs3500.easyanimator.model.ILayeredAnimatorModel;
import cs3500.easyanimator.model.layers.BasicLayer;
import cs3500.easyanimator.model.layers.ILayer;
import cs3500.easyanimator.model.shapes.BasicShapeFactory;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeFactory;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.ShapeF;
import cs3500.easyanimator.model.shapes.ShapeNameVisitor;
import cs3500.easyanimator.model.shapes.WidthHeight;

import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

/**
 * Controller specifically for the layer implementation of a model.
 */
public class LayerMVCController implements ILayerMVCController,
        PlaybackControls, EditorControls, LayerControls, ScrubbingControls {

  private Timer timer;
  private int tick;
  private double speed;
  private boolean looping;

  private ILayeredAnimatorModel model;
  private ILayerView view;

  // Current layer can be null,
  // for example if there are no layers in the editor, or none of them are selected.
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
    this.view.addScrubbingControls(this);
    this.view.setScrubbingMax(model.getMaxTick());

    // Start timer with the speed of view.
    this.timer = new Timer(0, e -> this.refreshTick());
    this.timer.stop();
  }

  @Override
  public void start() {
    if (this.speed == -1.0) {
      throw new IllegalArgumentException("Can't start if speed has not been set.");
    }

    // We update the layers before going visible.
    // Current layer is null by default.
    updateLayers();
    view.makeVisible();
  }

  @Override
  public void refreshTick() {
    //update the shapes that should be drawn. This goes at start so first frame has it.
    refreshDrawing();
    if (looping && tick >= model.getMaxTick()) {
      tick = 0;
    }
    else if (!looping && tick >= model.getMaxTick()) {
      //do nothing, timer should still run, but don't update the tick.
    }
    else {
      tick += 1;
    }
  }

  @Override
  public void refreshDrawing() {
    // Update the shapes that should be drawn. This goes at start so first frame has it.
    view.setDrawShapes(model.getShapesAtTick(this.tick));
    view.setTick(this.tick);

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
    shapeName = shapeName.trim();
    if (currentLayer == null) {
      // If there is no currently selected layer then this is not applicable.
      view.makeErrorSound();
      return;
    }

    // We make sure that there isn't a same name in the entire stack of layers.
    // This isn't a piece of state that the model itself can maintain, instead as users we must.
    // Whilst the editor is unaffected if we don't, it does affect text views.
    IAnimatorModel layerModel = currentLayer.getModel();

    if (shapeName.equals("New Shape") || shapeName.equals("")) {
      System.out.println("Bad name to add.");
      view.makeErrorSound();
      return;
    }

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
    if (currentLayer == null) {
      // If there is no currently selected layer then this is not applicable.
      view.makeErrorSound();
      return;
    }

    IAnimatorModel layerModel = currentLayer.getModel();

    try {
      layerModel.removeShape(shapeName);
      updateShapes();
    } catch (IllegalArgumentException iae) {
      view.makeErrorSound();
    }
    updateShapes();
    updateKeyframeSelector(null);
  }

  @Override
  public void renameShape(String name, String newName, String shapeType) {
    if (currentLayer == null) {
      // If there is no currently selected layer then this is not applicable.
      view.makeErrorSound();
      return;
    }

    // This is a trickier situation than calling something in the model.
    IAnimatorModel layerModel = currentLayer.getModel();

    // We won't rename a non-existent shape.
    List<String> shapeNames = layerModel.getShapeNames();
    if (!shapeNames.contains(name)) {
      System.out.println("Unable to rename non-existent shape.");
      view.makeErrorSound();
      return;
    }

    // Rename is also how we change the type of shape something is.
    if (newName.equals(name)) {
      currentLayer.getModel().addShape(name, SHAPE_FACTORY.getShape(shapeType,
              DEFAULT_WH, DEFAULT_POS, DEFAULT_COL));
      refreshDrawing(); // Shape might have changed.
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
    if (currentLayer == null) {
      // If there is no currently selected layer then this is not applicable.
      view.makeErrorSound();
      return;
    }

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
    if (currentLayer == null) {
      // If there is no currently selected layer then this is not applicable.
      view.makeErrorSound();
      return;
    }

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
    if (currentLayer == null) {
      // If there is no currently selected layer then this is not applicable.
      view.makeErrorSound();
      return;
    }

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
   * A helper method to update the layer selector.
   */
  private void updateLayers() {
    view.setLayers(model.getLayers().stream().map(l -> l.getName()).collect(Collectors.toList()));
    refreshDrawing(); // We refresh the drawing so users can see their layer changes working.
  }

  /**
   * A helper method to update the shape selector.
   */
  private void updateShapes() {
    if (currentLayer == null) {
      view.setAvailableShapes(Collections.EMPTY_LIST);
      return;
    }

    IAnimatorModel layerModel = currentLayer.getModel();

    List<String> shapesToDisplay = new ArrayList<String>();
    for (String name: layerModel.getShapeNames()) {
      shapesToDisplay.add(name);
    }
    view.setAvailableShapes(shapesToDisplay);
  }

  /**
   * A helper method to update the keyframe selector.
   * @param name  The name of the shape to use to lookup keyframes. Null if there is no shape.
   */
  private void updateKeyframeSelector(String name) {
    if (name == null) {
      view.setAvailableTicks(Collections.EMPTY_LIST);
      return;
    }

    if (currentLayer == null) {
      // If there is no currently selected layer then this is not applicable.
      view.makeErrorSound();
      return;
    }

    IAnimatorModel layerModel = currentLayer.getModel();

    List<String> ticksToDisplay = new ArrayList<String>();
    for (Integer tick: layerModel.getKeyframes(name).keySet()) {
      ticksToDisplay.add(Integer.toString(tick));
    }
    view.setAvailableTicks(ticksToDisplay);
    view.setScrubbingMax(model.getMaxTick());
    view.setPreferredCanvasSize(model.getCanvasSize());
  }

  private static String DEFAULT = "100";

  @Override
  public void selectTick(String shapeName, Integer tick) {
    if (currentLayer == null) {
      // If there is no currently selected layer then this is not applicable.
      view.makeErrorSound();
      return;
    }

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

  private static String NEW_LAYER = "New Layer";

  /**
   * A private helper method to return an ILayer by name in the model or null.
   * @param name  The name of the layer.
   * @return  The ilayer or null if it's not found.
   */
  private ILayer getLayerByName(String name) {
    return model.getLayers().stream()
            .filter(l -> l.getName().equals(name))
            .findFirst().orElse(null);
  }

  @Override
  public void selectLayer(String layer) {
    if (layer == null || layer.equals(NEW_LAYER)) {
      return; // We don't actually do anything ...
    }
    ILayer newlySelectedLayer = getLayerByName(layer);
    if (newlySelectedLayer == null) {
      view.makeErrorSound();
      return;
    }
    currentLayer = newlySelectedLayer;
    updateShapes();
  }

  @Override
  public void deleteLayer(String layer) {
    if (layer == null || layer.equals(NEW_LAYER)) {
      return; // We don't actually do anything ...
    }
    ILayer layerToDelete = getLayerByName(layer);
    if (layerToDelete == null) {
      view.makeErrorSound();
      return;
    } else if (model.getLayers().size() < 2) {
      view.makeErrorSound(); // We don't want to deal with no layers, that's an extra state ...
    }
    model.removeLayer(layerToDelete);
    currentLayer = null;
    updateShapes();
    updateKeyframeSelector(null);
    updateLayers();
  }

  @Override
  public void saveLayer(String selectedLayer, String layerName) {
    layerName = layerName.trim(); // We don't want different layer names by spaces.

    // Is it an invalid new layer name?
    if (getLayerByName(layerName) != null || layerName == null ||
            layerName.equals(NEW_LAYER) || layerName.equals("")) {
      // Is there a layer already with this name? Then we complain.
      view.makeErrorSound();
      return;
    }

    if (selectedLayer == null || selectedLayer.equals(NEW_LAYER)) {
      // We are saving a new layer.
      IAnimatorModel newModel = new EasyAnimator();
      newModel.setCanvas(model.getCanvasPosition(), model.getCanvasSize());
      ILayer newLayer = new BasicLayer(layerName, true, newModel);
      model.addLayer(newLayer);
      currentLayer = newLayer;
      updateShapes();
    } else {
      // We are renaming a layer.
      ILayer oldLayer = getLayerByName(selectedLayer);
      if (oldLayer == null) {
        // Does this layer actually exist?
        view.makeErrorSound();
        return;
      }
      int oldIndex = model.getLayers().indexOf(oldLayer);
      ILayer newLayer = oldLayer.setName(layerName);
      model.removeLayer(oldLayer);
      model.addLayer(newLayer, oldIndex);
      currentLayer = newLayer;
      updateShapes();
    }
    updateLayers();
  }

  @Override
  public void moveLayer(String layer, int delta) {
    ILayer selectedLayer = getLayerByName(layer);
    if (selectedLayer == null) {
      view.makeErrorSound();
      return;
    }
    int layerIndex = model.getLayers().indexOf(selectedLayer);
    if (delta + layerIndex < 0 || delta + layerIndex >= model.getLayers().size()) {
      view.makeErrorSound();
      return;
    }
    model.swapLayer(layerIndex, delta + layerIndex);
    updateLayers();
  }

  @Override
  public void scrubbingOccurred(int value) {
    tick = value;
    refreshDrawing();

  }
}
