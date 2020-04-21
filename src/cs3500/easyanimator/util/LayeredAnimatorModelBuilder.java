package cs3500.easyanimator.util;

import java.util.HashMap;
import java.util.Map;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.ILayeredAnimatorModel;
import cs3500.easyanimator.model.LayeredAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.layers.BasicLayer;
import cs3500.easyanimator.model.layers.ILayer;
import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.BasicShapeFactory;
import cs3500.easyanimator.model.shapes.IShapeFactory;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * A LayeredAnimatorModelBuilder supplies the methods needed by an advanced animation reader.
 * This will translate build calls about layers, and shapes with possible rotations into the
 * necessary commands to build a layered model.
 */
public class LayeredAnimatorModelBuilder
        implements AdvancedAnimationBuilder<ILayeredAnimatorModel> {

  // We will build on this model.
  // We will have to search through the layers a lot.
  private final ILayeredAnimatorModel model;
  // I don't want to look this up since shapes won't have a name for it.
  private final ILayer baseLayer;
  // We use the following map to remember types.
  private final Map<String, String> shapes;

  // We will apply these canvas properties over all the layers eventually.
  private Point canvasOffset;
  private WidthHeight canvasWH;

  /**
   * Create a new LayeredAnimatorModelBuilder with the given default base layer name.
   * @param name  The name of the base layer.
   */
  public LayeredAnimatorModelBuilder(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Unable to create a base layer with a null name.");
    }
    // I use a map since it's easier than checking a list of layers for their name property.
    // I will add the entry set of the layers when I build.
    IAnimatorModel baseModel = new EasyAnimator();
    baseLayer = new BasicLayer(name, true, baseModel);
    model = new LayeredAnimatorModel();
    model.addLayer(baseLayer);
    shapes = new HashMap<>();
  }

  /**
   * A private helper method to lookup the layer that contains the shape named this name.
   * @param name  The name to lookup.
   * @return      The layer containing the named shape, or null.
   */
  private ILayer getShapeLayer(String name) {
    for (ILayer layer: model.getLayers()) {
      if (layer.getModel().getShapeNames().contains(name)) {
        return layer;
      }
    }
    return null;
  }

  /**
   * A private helper method to lookup a layer by name.
   * @param name The unique name of a layer.
   * @return     The layer with that name or null if it doesn't exist.
   */
  public ILayer getLayer(String name) {
    for (ILayer layer: model.getLayers()) {
      if (layer.getName().equals(name)) {
        return layer;
      }
    }
    return null;
  }

  @Override
  public AdvancedAnimationBuilder<ILayeredAnimatorModel> declareLayer(String name,
                                                                      boolean visibility) {
    if (getLayer(name) != null) {
      throw new IllegalStateException("A layer with that name already exists.");
    }
    ILayer newLayer = new BasicLayer(name, visibility, new EasyAnimator());
    model.addLayer(newLayer);
    return this;
  }

  private static IShapeFactory SHAPE_FACTORY = new BasicShapeFactory();

  private static WidthHeight DEFAULT_SIZE = new WidthHeight(100, 100);
  private static Color DEFAULT_COLOR = new Color(100, 100, 100);
  private static Point DEFAULT_POSITION = new Point(-100, -100);

  /**
   * A private helper method to declare a shape once we have a pointer to a layer.
   * @param name  The name of the shape to declare. We assume checking has occurred prior.
   * @param type  The type of the shape.
   * @param layer The layer to add the shape to.
   * @return
   */
  private AdvancedAnimationBuilder<ILayeredAnimatorModel> declareShape(String name, String type,
                                                                       ILayer layer) {
    layer.getModel().addShape(name, SHAPE_FACTORY.getShape(type,
            DEFAULT_SIZE, DEFAULT_POSITION, DEFAULT_COLOR));
    shapes.put(name, type);
    return this;
  }

  @Override
  public AdvancedAnimationBuilder<ILayeredAnimatorModel> declareShape(String name, String type,
                                                                      String layer) {
    // First let's check that the name isn't already taken.
    if (getShapeLayer(name) != null) {
      throw new IllegalStateException("Unable to create a shape with the same name.");
    }
    ILayer layerByName = getLayer(layer);
    if (layerByName == null) {
      throw new IllegalStateException("Unable to add a shape to an undefined layer.");
    }
    return declareShape(name, type, layerByName);
  }

  @Override
  public AdvancedAnimationBuilder<ILayeredAnimatorModel> declareShape(String name, String type) {
    if (getShapeLayer(name) != null) {
      throw new IllegalStateException("Unable to create a shape with the same name.");
    }
    return declareShape(name, type, baseLayer);
  }

  // TODO: Rotation is suppressed as an argument here. It's just ignored.
  @Override
  public AdvancedAnimationBuilder<ILayeredAnimatorModel> addMotion(String name,
                                                                   int t1,
                                                                   int x1, int y1,
                                                                   int w1, int h1,
                                                                   int r1, int g1, int b1,
                                                                   int rot1,
                                                                   int t2,
                                                                   int x2, int y2,
                                                                   int w2, int h2,
                                                                   int r2, int g2, int b2,
                                                                   int rot2) {
    // First let's check that the shape exists.
    ILayer shapeLayer = getShapeLayer(name);
    if (shapeLayer == null) {
      throw new IllegalStateException("Unable to create a motion for an undefined shape.");
    }
    IMotion motion = new BasicMotion(t1, t2,
            new WidthHeight(w1, h1), new WidthHeight(w2, h2),
            new Point(x1, y1), new Point(x2, y2),
            new Color(r1, g1, b1), new Color(r2, g2, b2));
    shapeLayer.getModel().addMotion(name, motion);
    return this;
  }

  private static int DEFAULT_ROT = 0;

  @Override
  public AdvancedAnimationBuilder<ILayeredAnimatorModel> addMotion(String name, int t1,
                                                                   int x1, int y1,
                                                                   int w1, int h1,
                                                                   int r1, int g1, int b1,
                                                                   int t2,
                                                                   int x2, int y2,
                                                                   int w2, int h2,
                                                                   int r2, int g2, int b2) {
    return this.addMotion(name, t1, x1, y1, w1, h1, r1, g1, b1, DEFAULT_ROT,
            t2, x2, y2, w2, h2, r2, g2, b2, DEFAULT_ROT);
  }

  // TODO: Rotation is suppressed as an argument here. It's just ignored.
  @Override
  public AdvancedAnimationBuilder<ILayeredAnimatorModel> addKeyframe(String name,
                                                                     int t,
                                                                     int x, int y,
                                                                     int w, int h,
                                                                     int r, int g, int b,
                                                                     int rot) {

    // First let's check that the shape exists.
    ILayer shapeLayer = getShapeLayer(name);
    if (shapeLayer == null) {
      throw new IllegalStateException("Unable to create a motion for an undefined shape.");
    }
    shapeLayer.getModel().addKeyframe(name,
            SHAPE_FACTORY.getShape(shapes.get(name),
                    new WidthHeight(w, h),
                    new Point(x, y),
                    new Color(r, g, b)),
            t);
    return this;
  }

  @Override
  public AdvancedAnimationBuilder<ILayeredAnimatorModel> addKeyframe(String name, int t,
                                                                     int x, int y,
                                                                     int w, int h,
                                                                     int r, int g, int b) {
    return this.addKeyframe(name, t, x, y, w, h, r, g, b, DEFAULT_ROT);
  }

  @Override
  public AdvancedAnimationBuilder<ILayeredAnimatorModel> setBounds(int x, int y,
                                                                   int width, int height) {
    this.canvasOffset = new Point(x, y);
    this.canvasWH = new WidthHeight(width, height);
    return this;
  }

  @Override
  public ILayeredAnimatorModel build() {
    // Before we forget, lets set bounds on every layer.
    if (canvasOffset == null || canvasOffset == null) {
      throw new IllegalStateException("Unable to successfully build model without canvas.");
    }
    for (ILayer layer: model.getLayers()) {
      layer.getModel().setCanvas(canvasOffset, canvasWH);
    }
    return this.model;
  }
}
