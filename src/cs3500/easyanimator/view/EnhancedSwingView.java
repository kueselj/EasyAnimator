package cs3500.easyanimator.view;

import cs3500.easyanimator.model.IAnimatorModelViewOnly;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.WidthHeight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Implementation of an EnhancedView that delegates base operations to a VisualView.
 */
public class EnhancedSwingView extends JFrame implements IEnhancedVisualView {



  private AnimationPanel mainPanel;

  private JPanel buttonPanel;

  private JButton startButton;
  private JButton pauseButton;
  private JButton resumeButton;
  private JButton restartButton;
  private JButton toggleLoopingButton;
  private JButton increaseSpeedButton;
  private JButton decreaseSpeedButton;


  /**
   * Constructor for an enhanced view, takes in a IVisualVew to delegate base operations to.
   */
  public EnhancedSwingView() {
    super();
    this.setTitle("Your Animation");
    this.mainPanel = new AnimationPanel();

    this.setResizable(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.setLayout(new BorderLayout());
    this.add(this.mainPanel);

    JScrollPane scrollPane = new JScrollPane(this.mainPanel);
    this.add(scrollPane, BorderLayout.CENTER);

    //Buttons

    //button panel
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout());
    buttonPanel.setBackground(new Color(200, 200, 255));
    this.add(buttonPanel, BorderLayout.SOUTH);

    //start button.
    startButton = new JButton("Start");
    buttonPanel.add(startButton);

    //pause button.
    pauseButton = new JButton("Pause");
    buttonPanel.add(pauseButton);

    //resume button.
    resumeButton = new JButton("Resume");
    buttonPanel.add(resumeButton);

    //restart button.
    restartButton = new JButton("Restart");
    buttonPanel.add(restartButton);

    //toggle looping.
    toggleLoopingButton = new JButton("Toggle Looping");
    buttonPanel.add(toggleLoopingButton);

    //increase speed button.
    increaseSpeedButton = new JButton("Increase Speed");
    buttonPanel.add(increaseSpeedButton);


    //decrease  speed button.
    decreaseSpeedButton = new JButton("Decrease Speed");
    buttonPanel.add(decreaseSpeedButton);

  }

  @Override
  public void refresh() {
    this.repaint();
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public void setViewSize(WidthHeight wH) {
    Dimension canvasDimension = new Dimension(wH.getWidth(), wH.getHeight());
    Dimension frameDimension = new Dimension(wH.getWidth() + 20, wH.getHeight() + 60);

    this.mainPanel.setPreferredSize(canvasDimension);
    this.mainPanel.setMaximumSize(canvasDimension);
    this.setMinimumSize(frameDimension);
    this.setPreferredSize(frameDimension);
    this.setMaximumSize(frameDimension);
  }

  @Override
  public void setModel(IAnimatorModelViewOnly model) {
    //DO NOTHING TRY AND REMOVE
  }

  @Override
  public void start() {
    //
  }

  @Override
  public void pause() {
    //
  }

  @Override
  public void resume() {
    //
  }

  @Override
  public void restart() {
    //
  }

  @Override
  public void enableLooping() {
    //
  }

  @Override
  public void disableLooping() {
    //
  }

  @Override
  public void setSpeed(double speed) {
    if (speed <= 0.0) {
      throw new IllegalArgumentException("Speed must be integer greater than 0");
    }

    //this.timer = new Timer((int) (1.0 / 100000), e -> this.refresh());
  }

  @Override
  public void setShapes(java.util.List<IShape> shapesAtTick) {
    this.mainPanel.setShapes(shapesAtTick);
  }

  @Override
  public void addActionListeners(ActionListener... listeners) {
    this.startButton.addActionListener(listeners[0]);
    this.pauseButton.addActionListener(listeners[1]);
    this.resumeButton.addActionListener(listeners[2]);
    this.restartButton.addActionListener(listeners[3]);
    this.toggleLoopingButton.addActionListener(listeners[4]);
    this.increaseSpeedButton.addActionListener(listeners[5]);
    this.decreaseSpeedButton.addActionListener(listeners[6]);


  }
}
