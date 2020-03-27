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
  private Timer timer;
  private int tick;

  /**
   * Basic constructor for the SwingView to use.
   */
  public SwingView(IAnimatorModel model) {
    super();
    this.model = model;
    this.tick = 0;
    this.setTitle("Your Animation");
    //TODO get the correctSize for your view.
    this.setSize(500, 500);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.mainPanel = new AnimationPanel();
    this.mainPanel.setPreferredSize(new Dimension(500, 500));
    this.add(this.mainPanel);


    this.timer = new Timer(50, e -> this.refresh());
    this.timer.start();

    this.pack();
  }

  @Override
  public void refresh() {
    //TODO create field for the max length of the animation by looking at the endTime.
    //System.out.println("Test");
    if (this.tick == 100) {
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
    //make sure you set the model to have the things
    this.model = model;

  }
}
