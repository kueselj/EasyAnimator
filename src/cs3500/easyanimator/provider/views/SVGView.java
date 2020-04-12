package cs3500.easyanimator.provider.views;

import cs3500.animator.AnimatorModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Represents an SVG (Scalable Vector Graphic) view of an animation. A child class of
 * AAnimationPanel.
 */
public class SVGView extends AAnimationPanel {

  public SVGView(AnimatorModel model) {
    super(model);
  }

  /**
   * Returns the animation described in the model in textual form.
   *
   * @return a String representing the animation.
   */
  public String toSVG() {
    return model.toSVG();
  }

  @Override
  public void customRender(Graphics2D g2d) {
    g2d.setColor(Color.BLACK);
    int fontSize = 12;
    int tab = 20;
    g2d.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));

    String toDraw = model.toSVG();
    String[] drawStrings = toDraw.split("\n");
    int index = 1;
    for (String s : drawStrings) {
      if (s.startsWith("\t")) {
        g2d.drawString(s, tab, (fontSize * index));
      } else {
        g2d.drawString(s, 0, (fontSize * index));
      }
      index++;
    }
  }

  @Override
  public String toString() {
    return this.toSVG();
  }
}
