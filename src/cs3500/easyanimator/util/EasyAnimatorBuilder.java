package cs3500.easyanimator.util;

import cs3500.easyanimator.model.IAnimatorModel;

/**
 * A class to take build instructions according to the animation builder interface and translate it
 * to the correct model construction method calls for an IAnimatorModel (we specifically do it with
 * an EasyAnimator).
 */
public class EasyAnimatorBuilder implements AnimationBuilder<IAnimatorModel> {

  @Override
  public IAnimatorModel build() {
    return null;
  }

  @Override
  public AnimationBuilder<IAnimatorModel> setBounds(int x, int y, int width, int height) {
    return null;
  }

  @Override
  public AnimationBuilder<IAnimatorModel> declareShape(String name, String type) {
    return null;
  }

  @Override
  public AnimationBuilder<IAnimatorModel> addMotion(String name, int t1, int x1, int y1, int w1, int h1, int r1, int g1, int b1, int t2, int x2, int y2, int w2, int h2, int r2, int g2, int b2) {
    return null;
  }

  @Override
  public AnimationBuilder<IAnimatorModel> addKeyframe(String name, int t, int x, int y, int w, int h, int r, int g, int b) {
    return null;
  }
}
