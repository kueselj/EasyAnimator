package cs3500.easyanimator.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
public class AnimatorModelBuilder implements AnimationBuilder<IAnimatorModel> {

  /**
   * A State is a collection of properties of a shape but without a method or constructor or
   * anything. We only use it like a tuple.
   */
  private class State {
    private Color color;
    private Point position;
    private WidthHeight size;
  }

  private IAnimatorModel model;
  private Map<String, Map<Integer, State>> keyframes; // Really hacky stuff incoming!

  // A utility to use.
  private static IShapeFactory shapeFactory = new BasicShapeFactory();

  // We will use these defaults when creating shapes because our model mismatches the text
  // description a little bit in that they don't work without motions.
  private static WidthHeight defaultSize = new WidthHeight(100, 100);
  private static Color defaultColor = new Color(100, 100, 100);
  private static Point defaultPosition = new Point(-100, -100);

  /**
   * Construct a new EasyAnimatorBuilder. This initializes all the things we will need.
   */
  public AnimatorModelBuilder() {
    this.model = new EasyAnimator();
    this.keyframes = new HashMap<>();
  }

  @Override
  public IAnimatorModel build() {
    // First we need to translate all the keyframes we got into motions.
    // This is kind of hacky so turn away kids.

    // TODO: This assumes we don't mix motions and keyframes. So I hope that doesn't happen.
    // That would require a lot of extra calculation to make work with our model.

    for (Map.Entry<String, Map<Integer, State>> entry: this.keyframes.entrySet()) {
      String name = entry.getKey();
      List<Integer> keyframeIndices = new ArrayList<>(entry.getValue().keySet());
      Collections.sort(keyframeIndices); // We put our indices into ascending order.
      for (int i = 0; i < keyframeIndices.size() - 1; i++) {
        int currentIndex = keyframeIndices.get(i);
        int nextIndex = keyframeIndices.get(i + 1);
        State currentState = entry.getValue().get(currentIndex);
        State nextState = entry.getValue().get(nextIndex);

        model.addMotion(name, new BasicMotion(currentIndex, nextIndex,
                currentState.size, nextState.size,
                currentState.position, nextState.position,
                currentState.color, nextState.color));
      }
    }

    // Now we can return the model we are done with it.
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
            defaultSize, defaultColor, defaultPosition));
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
    // We use the Oval as a collection of all these properties. I'm sorry.
    if (!this.keyframes.containsKey(name)) {
      this.keyframes.put(name, new HashMap<>());
    }
    State state = new State();
    state.position = new Point(x, y);
    state.size = new WidthHeight(w, h);
    state.color = new Color(r, g, b);
    this.keyframes.get(name).put(t, state);
    return this;
  }
}
