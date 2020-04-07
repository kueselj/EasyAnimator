package cs3500.easyanimator.util;

import java.util.HashMap;
import java.util.Map;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.motions.BasicMotion;
import cs3500.easyanimator.model.shapes.BasicShapeFactory;
import cs3500.easyanimator.model.shapes.IShapeFactory;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * A class to take build instructions according to the animation builder interface and translate it
 * to the correct model construction method calls for an IAnimatorModel (we specifically do it with
 * an EasyAnimator).
 */
public class EasyAnimatorModelBuilder implements AnimationBuilder<IAnimatorModel> {
  private IAnimatorModel model;

  // A utility to use.
  private static IShapeFactory shapeFactory = new BasicShapeFactory();

  // We will use these defaults when creating shapes because our model mismatches the text
  // description a little bit in that they don't work without motions.
  private static WidthHeight defaultSize = new WidthHeight(100, 100);
  private static Color defaultColor = new Color(100, 100, 100);
  private static Point defaultPosition = new Point(-100, -100);
  private Map<String, String> shapes;

  /**
   * Construct a new EasyAnimatorBuilder. This initializes all the things we will need.
   */
  public EasyAnimatorModelBuilder() {
    this.model = new EasyAnimator();
    this.shapes = new HashMap<>();
  }

  @Override
  public IAnimatorModel build() {
    return this.model;
  }

  @Override
  public AnimationBuilder<IAnimatorModel> setBounds(int x, int y, int width, int height) {
    this.model.setCanvas(new Point(x, y), new WidthHeight(width, height));
    return this;
  }

  @Override
  public AnimationBuilder<IAnimatorModel> declareShape(String name, String type) {
    this.model.addShape(name, shapeFactory.getShape(type,
            defaultSize, defaultPosition, defaultColor));
    return this;
  }

  @Override
  public AnimationBuilder<IAnimatorModel> addMotion(String name, int t1,
                                                    int x1, int y1,
                                                    int w1, int h1,
                                                    int r1, int g1, int b1,
                                                    int t2,
                                                    int x2, int y2,
                                                    int w2, int h2,
                                                    int r2, int g2, int b2) {
    if (t1 == t2) {
      // This is malformed, so we reject it.
      this.addKeyframe(name, t1, x1, y1, w1, h1, r1, g1, b1);
      return this;
    }
    this.model.addMotion(name, new BasicMotion(t1, t2,
            new WidthHeight(w1, h1), new WidthHeight(w2, h2),
            new Point(x1, y1), new Point(x2, y2),
            new Color(r1, g1, b1), new Color(r2, g2, b2)));
    return this;
  }

  @Override
  public AnimationBuilder<IAnimatorModel> addKeyframe(String name, int t,
                                                      int x, int y,
                                                      int w, int h,
                                                      int r, int g, int b) {
    if (shapes.containsKey(name)) {
      model.addKeyframe(name,
              shapeFactory.getShape(shapes.get(name),
                      new WidthHeight(w, h),
                      new Point(x, y),
                      new Color(r, g, b)),
              t);
    }
    return this;
  }
}
