package cs3500.easyanimator.view;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Dimension;

import cs3500.easyanimator.model.IAnimatorModelViewOnly;
import cs3500.easyanimator.model.shapes.WidthHeight;

/**
 * A visual animation view representing an IAnimatorModel. Uses swing to animate a set model.
 */
public class SwingView extends JFrame implements IAnimatorView {
  private IAnimatorModelViewOnly model;
  private AnimationPanel mainPanel;
  private Timer timer;
  private int tick;

  /**
   * Basic constructor for the SwingView to use.
   */
  public SwingView() {
    super("Your animation");
    // A good default size.
    this.setSize(new Dimension(800, 600));
    this.tick = 0;
    this.mainPanel = new AnimationPanel();
    JScrollPane scrollPane = new JScrollPane(mainPanel);
    this.add(scrollPane, BorderLayout.CENTER);
    this.setResizable(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.timer = new Timer(30, e -> this.refresh());
    // We don't need to start this timer until we have a model.
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void setModel(IAnimatorModelViewOnly model) {
    if (model == null) {
      throw new IllegalArgumentException("Cannot set the view to have a uninitialized model");
    }
    this.model = model;
    // We restart the view.
    timer.stop();
    this.tick = 0;
    // We need to make our panel fit the canvas size.
    WidthHeight canvasSize = model.getCanvasSize();
    Dimension canvasDimension = new Dimension(canvasSize.getWidth(), canvasSize.getHeight());
    this.mainPanel.setPreferredSize(canvasDimension);
    this.mainPanel.setMaximumSize(canvasDimension);
    // Repack.
    pack();
    timer.start();
  }

  @Override
  public void setSpeed(double tps) {
    if (tps <= 0) {
      throw new IllegalArgumentException("Speed must be integer greater than 0.");
    }
    timer.setDelay(((int) (1.0 / tps * 1000)));
  }

  /**
   * Do whatever methods are necessary to refresh all the components in this view.
   */
  private void refresh() {
    this.tick = (this.tick + 1) % model.getMaxTick();
    mainPanel.setShapes(model.getShapesAtTick(tick));
    mainPanel.repaint();
  }
}
