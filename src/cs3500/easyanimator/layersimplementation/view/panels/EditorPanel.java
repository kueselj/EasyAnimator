package cs3500.easyanimator.layersimplementation.view.panels;

import cs3500.easyanimator.layersimplementation.controller.EditorControls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * JPanel that holds all the editor components.
 */
public class EditorPanel extends JPanel {

  private static String[] DEFAULT_SHAPES = new String[]{"rectangle", "oval"};
  private static final Dimension EDITOR_PANEL_SIZE = new Dimension(300, 500);
  private static final Color EDITOR_PANEL_BACKGROUND = Color.GRAY;
  private static Dimension MAX_FIELD_SIZE = new Dimension(200, 25);

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
  private JTextField keyFrameRotation;

  private JButton saveKeyframe;
  private JButton deleteKeyframe;

  /**
   * Basic editor panel constructor.
   */
  public EditorPanel() {
    super();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setMinimumSize(EDITOR_PANEL_SIZE);
    setMaximumSize(EDITOR_PANEL_SIZE);
    setBackground(EDITOR_PANEL_BACKGROUND);

    selectShape = new JComboBox<String>();
    add(selectShape);

    shapeName = createField("Shape Name");

    shapeType = new JComboBox<>(DEFAULT_SHAPES);
    add(shapeType);

    saveShape = new JButton("Save Shape");
    add(saveShape);

    deleteShape = new JButton("Delete Shape");
    add(deleteShape);

    selectTick = new JComboBox<String>();
    add(selectTick);

    keyFrameTick = createField("Tick");

    keyFrameX = createField("X");

    keyFrameY = createField("Y");

    keyFrameWidth = createField("Width");

    keyFrameHeight = createField("Height");

    keyFrameR = createField("R");

    keyFrameG = createField("G");

    keyFrameB = createField("B");

    keyFrameRotation = createField("Degrees Rotated");

    saveKeyframe = new JButton("Save Keyframe");
    add(saveKeyframe);

    deleteKeyframe = new JButton("Delete Keyframe");
    add(deleteKeyframe);
  }

  /**
   * The editor features that the panel can use.
   * @param editorControls
   */
  public void addEditorControls(EditorControls editorControls) {


  }

  /**
   * Create a field coupled with a label and add it to the editor panel.
   * @param name  The named label of the field to use.
   * @return
   */
  private JTextField createField(String name) {
    JPanel group = new JPanel(new BorderLayout());
    JTextField field = new JTextField();
    field.setPreferredSize(MAX_FIELD_SIZE);
    JLabel label = new JLabel(name);
    group.add(label, BorderLayout.CENTER);
    group.add(field, BorderLayout.WEST);
    add(group);
    return field;
  }
}
