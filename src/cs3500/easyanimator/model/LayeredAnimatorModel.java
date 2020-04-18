package cs3500.easyanimator.model;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

import cs3500.easyanimator.model.layers.ILayer;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * A LayeredAnimatorModel is a model that implements layers by stacking together IAnimatorModels
 * contained within Layerss.
 */
public class LayeredAnimatorModel implements ILayeredAnimatorModel {
  @Override
  public void addLayer(ILayer layer) {

  }

  @Override
  public void removeLayer(ILayer layer) {

  }

  @Override
  public void removeLayer(int index) {

  }

  @Override
  public void swapLayer(int i, int j) {

  }

  @Override
  public List<ILayer> getLayers() {
    return null;
  }

  @Override
  public ILayer getLayer(int i) {
    return null;
  }

  @Override
  public WidthHeight getCanvasSize() {
    return null;
  }

  @Override
  public Point getCanvasPosition() {
    return null;
  }

  @Override
  public Map<String, IShape> getShapes() {
    return null;
  }

  @Override
  public Map<String, SortedSet<IMotion>> getSortedMotions() {
    return null;
  }

  @Override
  public IShape getShapeAtTick(String id, int tick) {
    return null;
  }

  @Override
  public List<IShape> getShapesAtTick(int tick) {
    return null;
  }

  @Override
  public int getShapeMaxTick(String id) {
    return 0;
  }

  @Override
  public SortedMap<Integer, IShape> getKeyframes(String id) {
    return null;
  }
}
