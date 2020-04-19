package cs3500.easyanimator.layersimplementation.view.panels;

import cs3500.easyanimator.layersimplementation.controller.EditorControls;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * JPanel that holds all the editor components.
 */
public class EditorPanel extends JPanel {

  //Shape Selection.
  private JComboBox selectShape;
  private JTextField shapeName;
  private JComboBox<String> shapeType;

  private JButton saveShape;
  private JButton deleteShape;

  //Keyframe Selection
  private JComboBox selectTick;
  private JTextField keyFrameTick;
  private JTextField keyFrameX;
  private JTextField keyFrameY;
  private JTextField keyFrameWidth;
  private JTextField keyFrameHeight;
  private JTextField keyFrameR;
  private JTextField keyFrameG;
  private JTextField keyFrameB;

  private JButton saveKeyframe;
  private JButton deleteKeyframe;

  /**
   * Basic editor panel constructor.
   */
  public EditorPanel() {

  }

  /**
   * The editor features that the panel can use.
   * @param editorControls
   */
  public void addEditorControls(EditorControls editorControls) {

  }
}
