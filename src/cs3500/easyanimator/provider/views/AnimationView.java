package cs3500.easyanimator.provider.views;

import cs3500.easyanimator.provider.model.AnimatorModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * A View class as part of the MVC model. Creates a panel on which the animations take place. A
 * child class of JFrame.
 */
public class AnimationView extends JFrame implements IAnimationViewer {

  private AAnimationPanel panel;

  /**
   * A public constructor for a view. Creates the panel on which the animation takes place.
   *
   * @param model    the model to use for the view
   * @param viewType the type of view (text, svg or visual)
   */
  public AnimationView(AnimatorModel model, String viewType) {
    super();

    this.setLayout(new BorderLayout());
    this.setResizable(true);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setTitle("CS3500!");

    if (viewType == null) {
      throw new IllegalArgumentException("View type cannot be null");
    }

    if (viewType.equalsIgnoreCase("text")) {
      this.panel = new TextualView(model);
    } else if (viewType.equalsIgnoreCase("svg")) {
      this.panel = new SVGView(model);
    } else if (viewType.equalsIgnoreCase("visual")) {
      this.panel = new VisualView(model);
    } else {
      throw new IllegalArgumentException("This view type does not exist!");
    }

    JScrollPane pane = new JScrollPane(panel);
    pane.setPreferredSize(new Dimension(model.canvasX, model.canvasY));
    pane.getViewport().setViewPosition(new Point(model.xOffset, model.yOffset));

    this.getContentPane().add(pane);
    this.pack();
    this.setSize(new Dimension(model.canvasX, model.canvasY));
    this.setVisible(true);
  }

  @Override
  public void draw() {
    this.panel.repaint();
  }

  @Override
  public String toString() {
    return this.panel.toString();
  }
}
