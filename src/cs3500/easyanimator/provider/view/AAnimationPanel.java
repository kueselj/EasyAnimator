package cs3500.easyanimator.provider.view;

import cs3500.easyanimator.provider.model.AnimatorModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * An abstract class that represents a panel on which an animation is drawn. It is used to display
 * the animation in the view. A child class of JPanel.
 */
public abstract class AAnimationPanel extends JPanel {

  protected AnimatorModel model;

  /**
   * A public constructor for an animation panel.
   *
   * @param model the model to use for the animation
   */
  public AAnimationPanel(AnimatorModel model) {
    if (model == null) {
      throw new IllegalArgumentException("The model cannot be null.");
    }
    if (model.canvasX <= 0 || model.canvasY <= 0) {
      throw new IllegalArgumentException("The dimensions of the canvas must be greater than 0.");
    }
    this.setBackground(Color.WHITE);
    this.model = model;

    // Add a multiplier to make the animation space larger than the viewport.
    this.setPreferredSize(new Dimension(model.canvasX * 6, model.canvasY * 6));
  }

  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.clearRect(0, 0, model.canvasX * 6, model.canvasY * 6);
    customRender(g2d);
  }

  /**
   * Renders the given animation onto the panel.
   *
   * @param g2d the graphics renderer to use
   */
  abstract void customRender(Graphics2D g2d);

  /**
   * Overriding the toString method of the view object.
   *
   * @return - The string representing it's state.
   */
  public abstract String toString();
}
