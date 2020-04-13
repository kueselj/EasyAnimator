package cs3500.easyanimator.provider.views;

import cs3500.easyanimator.provider.model.AShape;
import cs3500.easyanimator.provider.model.AnimatorModel;
import java.awt.Graphics2D;

/**
 * Represents a visual view of an animation. A child class of AAnimationPanel.
 */
public class VisualView extends AAnimationPanel {

  /**
   * A public constructor for an animation panel.
   *
   * @param model   the model to use for the animation
   */
  public VisualView(AnimatorModel model) {
    super(model);
  }

  @Override
  public void customRender(Graphics2D g2d) {
    for (AShape s : model.getShapes()) {
      s.render(g2d, model.xOffset, model.yOffset);
    }
  }

  @Override
  public String toString() {
    return "This is a visual view.";
  }
}
