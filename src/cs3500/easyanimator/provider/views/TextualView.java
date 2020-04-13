package cs3500.easyanimator.provider.views;

import cs3500.easyanimator.provider.model.AnimatorModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Represents a textual view of an animation. A child class of AAnimationPanel.
 */
public class TextualView extends AAnimationPanel {

  /**
   * A public constructor for an animation panel.
   *
   * @param model the model to use for the animation
   */
  public TextualView(AnimatorModel model) {
    super(model);
  }

  /**
   * Returns the animation described in the model in textual form.
   *
   * @return a String representing the animation.
   */
  public String toText() {
    return model.getTextualAnimationView();
  }

  @Override
  void customRender(Graphics2D g2d) {
    g2d.setColor(Color.BLACK);
    String toDraw = model.getTextualAnimationView();
    int fontSize = 12;
    g2d.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
    String[] drawStrings = toDraw.split("\n");
    int index = 1;
    for (String s : drawStrings) {
      g2d.drawString(s, 0, (fontSize * index));
      index++;
    }
  }

  @Override
  public String toString() {
    return toText();
  }
}
