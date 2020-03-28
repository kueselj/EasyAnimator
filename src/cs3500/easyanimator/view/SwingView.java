package cs3500.easyanimator.view;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.List;

import cs3500.easyanimator.model.IAnimatorModelViewOnly;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * A visual animation view representing an IAnimatorModel. Uses swing to animate a set model.
 */
public class SwingView extends JFrame implements IVisualView {
  private IAnimatorModelViewOnly model;
  private AnimationPanel mainPanel;
  private JScrollPane scrollPane;
  private Timer timer;
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

    this.scrollPane = new JScrollPane(this.mainPanel);
    this.add(this.scrollPane, BorderLayout.CENTER);

    this.setResizable(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }

  @Override
  public void refresh() {
    //TODO create field for the max length of the animation by looking at the endTime.
    if (this.tick == tickRange) {
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
    this.tick = 0;
    this.tickRange = this.getTickRange();

    //get the correctSize for your view.
    WidthHeight wH = this.model.getCanvasSize();
    Dimension canvasDimension = new Dimension(wH.getWidth(), wH.getHeight());

    this.mainPanel.setPreferredSize(canvasDimension);
    this.setMaximumSize(canvasDimension);
    this.mainPanel.setMaximumSize(canvasDimension);

    this.timer = new Timer(5, e -> this.refresh());
    this.timer.start();

    this.pack();
  }

  @Override
  public void setSpeed(int speed) {

    if (speed <= 0) {
      throw new IllegalArgumentException("Speed must be integer greater than 0");
    }

    this.timer = new Timer(speed, e -> this.refresh());
    this.timer.start();
  }

  //Gets the max tick that the model has.
  private int getTickRange() {

    Map<String, List<IMotion>> motions = this.model.getMotions();

    int maxTick = 0;

    //iterate through each shape or entry in
    for (Map.Entry<String, List<IMotion>> entry: motions.entrySet()) {

      Collections.sort(entry.getValue(), Comparator.comparingInt(IMotion::getEndTime));

      List<IMotion> entryList =  motions.get(entry.getKey());

      if (entryList.get(entryList.size() - 1).getEndTime() > maxTick) {
        maxTick = entryList.get(entryList.size() - 1).getEndTime();
      }
    }

    return maxTick;
  }

  @Override
  public void setSpeed(double speed) {

  }
}
