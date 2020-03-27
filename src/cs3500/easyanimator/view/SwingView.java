package cs3500.easyanimator.view;

import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.IAnimatorModelViewOnly;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.WidthHeight;

import javax.swing.*;
import java.awt.*;

/**
 * A visual animation view representing an IAnimatorModel
 */
public class SwingView extends JFrame implements IVisualView {
  private IAnimatorModelViewOnly model;
  private AnimationPanel mainPanel;
  private int tickRange;
  private int tick;

  /**
   * Basic constructor for the SwingView to use.
   */
  public SwingView() {
    super();
    this.setTitle("Your Animation");
    this.tick = 0;
    this.mainPanel = new AnimationPanel();
    this.add(this.mainPanel);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  @Override
  public void refresh() {
    //TODO create field for the max length of the animation by looking at the endTime.
    //System.out.println("Test");
    if (this.tick == 500) {
      this.tick = 0;
      this.mainPanel.setShapes(model.getShapesAtTick(this.tick));
      this.repaint();
    }
    else {
      this.tick++;
      this.mainPanel.setShapes(model.getShapesAtTick(this.tick));
      this.repaint();
    }
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void setModel(IAnimatorModelViewOnly model) {
    if (model == null) {
      throw new IllegalArgumentException("Cannot set the view to have a uninitialized model");
    }

    this.model = model;
    //TODO get the correctSize for your view.
    this.mainPanel.setPreferredSize(new Dimension(500, 500));
    this.pack();

  }
}
