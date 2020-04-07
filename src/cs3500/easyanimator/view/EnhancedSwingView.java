package cs3500.easyanimator.view;

import cs3500.easyanimator.model.IAnimatorModelViewOnly;
import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.WidthHeight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of an EnhancedView that delegates base operations to a VisualView.
 */
public class EnhancedSwingView extends JFrame implements IEnhancedVisualView {

  private AnimationPanel mainPanel;
  private List<String> availableShapes;
  private List<String> availableShapeTicks;

  //TODO add a field for the shapes.
  private JPanel buttonPanel;

  private JButton startButton;
  private JButton pauseButton;
  private JButton resumeButton;
  private JButton restartButton;
  private JButton toggleLoopingButton;
  private JButton increaseSpeedButton;
  private JButton decreaseSpeedButton;

  private JPanel editorPanel;

  //TODO make this bring up a popup or something.
  private JButton addShapeButton;

  private JComboBox selectShape;
  private JComboBox selectTick;
  private JTextField keyFrameTick;
  private JTextField keyFrameX;
  private JTextField keyFrameY;
  private JTextField keyFrameWidth;
  private JTextField keyFrameHeight;
  private JTextField keyFrameR;
  private JTextField keyFrameG;
  private JTextField keyFrameB;
  private JButton saveModification;

  //only delete it if it is selected
  private JButton deleteKeyFrame;

  private JButton deleteShape;





  /**
   * Constructor for an enhanced view, takes in a IVisualVew to delegate base operations to.
   */
  public EnhancedSwingView() {
    super();
    this.setTitle("Your Animation");


    this.setResizable(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.setLayout(new BorderLayout());

    //PUT this in custom class perhaps. Def Actually./////////////////////////
    this.editorPanel = new JPanel();
    this.editorPanel.setLayout(new BoxLayout(editorPanel, BoxLayout.Y_AXIS));
    this.editorPanel.setSize(300, 500);
    this.editorPanel.setPreferredSize(new Dimension(300, 500));
    this.editorPanel.setMaximumSize(new Dimension(300, 500));
    this.editorPanel.setBackground(Color.DARK_GRAY);

    this.addShapeButton = new JButton("Add Shape");

    String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
    this.selectShape = new JComboBox(petStrings);
    String[] ticks = {"1","2","3","4","5"};
    this.selectTick = new JComboBox(ticks);

    Dimension textFieldDimension = new Dimension(100, 25);
    this.keyFrameTick = new JTextField();
    this.keyFrameTick.setMaximumSize(textFieldDimension);
    this.keyFrameX = new JTextField();
    this.keyFrameX.setMaximumSize(textFieldDimension);
    this.keyFrameY = new JTextField();
    this.keyFrameY.setMaximumSize(textFieldDimension);
    this.keyFrameWidth = new JTextField();
    this.keyFrameWidth.setMaximumSize(textFieldDimension);
    this.keyFrameHeight = new JTextField();
    this.keyFrameHeight.setMaximumSize(textFieldDimension);
    this.keyFrameR = new JTextField();
    this.keyFrameR.setMaximumSize(textFieldDimension);
    this.keyFrameG = new JTextField();
    this.keyFrameG.setMaximumSize(textFieldDimension);
    this.keyFrameB = new JTextField();
    this.keyFrameB.setMaximumSize(textFieldDimension);

    this.saveModification = new JButton("Save KeyFrame Modification");
    this.deleteKeyFrame = new JButton("Delete KeyFrame");
    this.deleteShape = new JButton("Delete Shape");

    this.editorPanel.add(addShapeButton, BorderLayout.CENTER);
    this.editorPanel.add(selectShape);
    this.editorPanel.add(selectTick);
    this.editorPanel.add(keyFrameTick);
    this.editorPanel.add(keyFrameX);
    this.editorPanel.add(keyFrameY);
    this.editorPanel.add(keyFrameWidth);
    this.editorPanel.add(keyFrameHeight);
    this.editorPanel.add(keyFrameR);
    this.editorPanel.add(keyFrameG);
    this.editorPanel.add(keyFrameB);
    this.editorPanel.add(saveModification);
    this.editorPanel.add(deleteKeyFrame);
    this.editorPanel.add(deleteShape);

    this.add(editorPanel, BorderLayout.LINE_END);
    //////////////////////////////////////////////////////////

    this.mainPanel = new AnimationPanel();
    //this.mainPanel.setLayout(new BorderLayout());
    this.add(this.mainPanel, BorderLayout.LINE_START);

    JScrollPane scrollPane = new JScrollPane(this.mainPanel);
    this.add(scrollPane, BorderLayout.CENTER);

    //Buttons

    //button panel
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout());
    buttonPanel.setBackground(new Color(106, 35, 38));
    this.add(buttonPanel, BorderLayout.PAGE_END);

    //start button.
    startButton = new JButton("Start");
    buttonPanel.add(startButton);
    //startButton.setOpaque(false);
    //startButton.setContentAreaFilled(false);
    //startButton.setBorderPainted(false);

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

    //this.setMinimumSize(frameDimension);
    //this.setPreferredSize(frameDimension);
    //this.setMaximumSize(frameDimension);
  }

  @Override
  public void setModel(IAnimatorModelViewOnly model) {
    //DO NOTHING TRY AND REMOVE
  }

  @Override
  public void setSpeed(double speed) {
    //DO NOTHING TRY AND REMOVE
  }

  @Override
  public void setAvailableShapes(List<String> shapes) {
    this.availableShapes = shapes;
    String[] shapeArray = shapes.toArray(new String[0]);
    DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(shapeArray);
    this.selectShape.setModel(comboBoxModel);

  }

  @Override
  public void setAvailableShapeTicks(List<String> shapeTicks) {

    this.availableShapeTicks = shapeTicks;
    String[] tickArray = shapeTicks.toArray(new String[0]);
    DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(tickArray);
    this.selectTick.setModel(comboBoxModel);

  }


  @Override
  public void setShapes(List<IShape> shapesAtTick) {
    this.mainPanel.setShapes(shapesAtTick);
  }

  @Override
  public void setTextFields(List<String> fields) {
    this.keyFrameTick.setText(fields.get(0));
    this.keyFrameX.setText(fields.get(1));
    this.keyFrameY.setText(fields.get(2));
    this.keyFrameWidth.setText(fields.get(3));
    this.keyFrameHeight.setText(fields.get(4));
    this.keyFrameR.setText(fields.get(5));
    this.keyFrameG.setText(fields.get(6));
    this.keyFrameB.setText(fields.get(7));


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

  @Override
  public String getShapeSelected() {
    return (String) this.selectShape.getSelectedItem();
  }

  @Override
  public String getTickSelected() {
    return (String) this.selectTick.getSelectedItem();
  }

  @Override
  public List<String> getTextFields() {
    List<String> fields = new ArrayList<String>();
    fields.add(this.keyFrameTick.getText());
    fields.add(this.keyFrameX.getText());
    fields.add(this.keyFrameY.getText());
    fields.add(this.keyFrameWidth.getText());
    fields.add(this.keyFrameHeight.getText());
    fields.add(this.keyFrameR.getText());
    fields.add(this.keyFrameG.getText());
    fields.add(this.keyFrameB.getText());
    return fields;
  }

  @Override
  public void addSelectShapeActionListener(ActionListener listener) {
    this.selectShape.addActionListener(listener);
  }

  @Override
  public void addSelectTickActionListener(ActionListener listener) {
    this.selectTick.addActionListener(listener);
  }

  @Override
  public void addSaveKeyFrameActionListener(ActionListener listener) {
    this.saveModification.addActionListener(listener);
  }
}
