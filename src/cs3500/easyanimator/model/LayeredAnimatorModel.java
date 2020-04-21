package cs3500.easyanimator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.stream.Collectors;

import cs3500.easyanimator.model.layers.ILayer;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * A LayeredAnimatorModel is a model that implements layers by stacking together IAnimatorModels
 * contained within Layerss.
 */
public class LayeredAnimatorModel implements ILayeredAnimatorModel {

  private final List<ILayer> layers;

  /**
   * Create a new LayeredAnimatorModel.
   */
  public LayeredAnimatorModel() {
    layers = new ArrayList<>();
  }

  @Override
  public void addLayer(ILayer layer) {
    if (layer == null) {
      throw new IllegalArgumentException("Unable to add uninitialized layer.");
    }
    layers.add(layer); // This will append it to the end.
  }

  @Override
  public void removeLayer(ILayer layer) {
    if (!layers.contains(layer)) {
      throw new IllegalArgumentException("Unable to remove layer that is not in the model.");
    }
    layers.remove(layer);
  }

  /**
   * A private helper method to check if an index is in bounds.
   * @param index The index to lookup.
   * @return      If the index is in bounds (true) or not (false).
   */
  private boolean indexCheck(int index) {
    return index >= 0 && index < layers.size();
  }

  @Override
  public void removeLayer(int index) {
    if (!indexCheck(index)) {
      throw new IllegalArgumentException("Unable to remove layer out of index.");
    }
    layers.remove(index);
  }

  @Override
  public void swapLayer(int i, int j) {
    if (!indexCheck(i) || !indexCheck(j)) {
      throw new IllegalArgumentException("Unable to swap layers, argument(s) out of bounds.");
    }
    // I prefer to use a library that already does this since this is such a common piece of code.
    Collections.swap(layers, i, j);
  }

  @Override
  public List<ILayer> getLayers() {
    // We return a soft-copy to avoid obvious direct mutation.
    // Plus we actually want the models in the layers to be mutated as well.
    return new ArrayList<>(layers);
  }

  @Override
  public ILayer getLayer(int i) {
    if (!indexCheck(i)) {
      throw new IllegalArgumentException("Unable to give layer at that index, out of bounds.");
    }
    return layers.get(i);
  }

  @Override
  public WidthHeight getCanvasSize() {
    // So this requires a little bit of a complex calculation.
    // Our canvas size needs to stretch from the top-left-most offset,
    // down to the bottom-right-most point.
    // We can re-use getCanvasPosition for that top-left most point.
    Point topLeft = getCanvasPosition();
    int bottomRightX = topLeft.getX();
    int bottomRightY = topLeft.getY();
    for (ILayer layer: layers) {
      IAnimatorModel model = layer.getModel();
      Point offset = model.getCanvasPosition();
      WidthHeight size = model.getCanvasSize();
      bottomRightX = Math.max(bottomRightX, offset.getX() + size.getWidth());
      bottomRightY = Math.max(bottomRightY, offset.getX() + size.getWidth());
    }
    return new WidthHeight(bottomRightX - topLeft.getX(),
            bottomRightY - topLeft.getY());
  }

  @Override
  public Point getCanvasPosition() {
    // We get the top-left most offset.
    int topLeftX = Integer.MAX_VALUE;
    int topLeftY = Integer.MAX_VALUE;
    for (ILayer layer: layers) {
      IAnimatorModel model = layer.getModel();
      Point offset = model.getCanvasPosition();
      topLeftX = Math.min(topLeftX, offset.getX());
      topLeftY = Math.min(topLeftY, offset.getY());
    }
    return new Point(topLeftX, topLeftY);
  }

  @Override
  public Map<String, IShape> getShapes() {
    // If there are name conflicts, shapes are going to disappear here.
    // I decided to give stream magic a try here.
    return layers.stream()
            .map(layer -> layer.getModel()) // Turn a stream of layers into one of models.
            .flatMap(model -> model.getShapes().entrySet().stream()) // -> named shapes.
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                    (k1, k2) -> {throw new IllegalStateException("Unable to merge shape names.");},
                    LinkedHashMap::new)); // collect.
  }

  @Override
  public Map<String, SortedSet<IMotion>> getSortedMotions() {
    Map<String, SortedSet<IMotion>> map = new HashMap<>();
    for (ILayer layer: layers) {
      IAnimatorModel model = layer.getModel();
      map.putAll(model.getSortedMotions());
    }
    return map;
  }

  /**
   * A private method to lookup a named shape across multiple layers.
   * @param id  The name of the shape to lookup.
   * @return    The layer containing the named shape, or null if not found.
   */
  private ILayer shapeLookup(String id) {
    for (ILayer layer: layers) {
      if (layer.getModel().getShapeNames().contains(id)) {
        return layer;
      }
    }
    return null;
  }

  /**
   * Some custom code as a private helper method to verify that a shape exists and return the layer
   * for it, or else throws an IllegalArgumentException. This also checks for null
   * @param id    The id to check.
   * @throws IllegalArgumentException If the id is null, or doesn't exist in any layer.
   */
  private ILayer validShapeOrException(String id) {
    if (id == null) {
      throw new IllegalArgumentException("Unable to lookup null id for max-tick.");
    }
    ILayer lookup = shapeLookup(id);
    if (lookup == null) {
      throw new IllegalArgumentException("Unable to lookup max-tick for a shape not in the model.");
    }
    return lookup;
  }

  @Override
  public IShape getShapeAtTick(String id, int tick) {
    // So this is likely never going to be used.
    // I don't even use it since it requires multi-layer lookup for the named shape.
    // I'll implement it ...
    ILayer lookup = validShapeOrException(id);
    IShape shape = lookup.getModel().getShapeAtTick(id, tick);
    if (shape == null) {
      return null;
    }
    // If the shape was valid then we need to move it to align with the global canvas.
    Point layerOffset = lookup.getModel().getCanvasPosition();
    Point globalOffset = getCanvasPosition();
    int diffX = layerOffset.getX() - globalOffset.getX();
    int diffY = layerOffset.getY() - globalOffset.getY();
    Point originalPos = shape.getPosition();
    shape.setPosition(new Point(originalPos.getX() + diffX, originalPos.getY() + diffY));
    return shape;
  }

  @Override
  public List<IShape> getShapesAtTick(int tick) {
    List<IShape> shapes = new ArrayList<>();
    Point globalOffset = getCanvasPosition();
    for (ILayer layer: layers) {
      if (!layer.getVisibility()) {
        continue; // Next loop.
      }
      Point layerOffset = layer.getModel().getCanvasPosition();
      int diffX = layerOffset.getX() - globalOffset.getX();
      int diffY = layerOffset.getY() - globalOffset.getY();
      for (IShape shape: layer.getModel().getShapesAtTick(tick)) {
        Point originalPosition = shape.getPosition();
        shape.setPosition(new Point(originalPosition.getX() + diffX,
                originalPosition.getY() + diffY));
        shapes.add(shape);
      }
    }
    return shapes;
  }

  @Override
  public int getShapeMaxTick(String id) {
    ILayer lookup = validShapeOrException(id);
    return lookup.getModel().getShapeMaxTick(id);
  }

  @Override
  public SortedMap<Integer, IShape> getKeyframes(String id) {
    ILayer lookup = validShapeOrException(id);
    return lookup.getModel().getKeyframes(id);
  }
}
