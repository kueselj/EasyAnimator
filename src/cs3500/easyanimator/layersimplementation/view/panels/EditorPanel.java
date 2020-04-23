package cs3500.easyanimator.layersimplementation.view.panels;

import cs3500.easyanimator.layersimplementation.controller.EditorControls;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * JPanel that holds all the editor components.
 */
public class EditorPanel extends JPanel {

  private static String NEW_KEYFRAME = "New Keyframe";
  private static String NEW_SHAPE = "New Shape";
  private static String[] DEFAULT_SHAPES = new String[]{"rectangle", "oval"};
  private static final Dimension EDITOR_PANEL_SIZE = new Dimension(300, 500);
  private static final Color EDITOR_PANEL_BACKGROUND = new Color(51, 107, 135);
  private static final Color BUTTON_COLOR = new Color(188, 223, 255);
  private static Dimension MAX_FIELD_SIZE = new Dimension(200, 25);
  private static final Font TEXT_FONT = new Font("Courier", Font.PLAIN, 20);

  //Shape Selection.
  private JComboBox<String> selectShape;
  private JTextField shapeName;
  private JComboBox<String> selectShapeType;

  private JButton saveShape;
  private JButton deleteShape;

  //Keyframe Selection
  private JComboBox<String> selectTick;
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
    super();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setMinimumSize(EDITOR_PANEL_SIZE);
    setMaximumSize(EDITOR_PANEL_SIZE);
    setBackground(EDITOR_PANEL_BACKGROUND);

    selectShape = new JComboBox<String>();
    selectShape.setFont(TEXT_FONT);
    add(selectShape);

    shapeName = createField("Shape Name");

    selectShapeType = new JComboBox<>(DEFAULT_SHAPES);
    selectShapeType.setFont(TEXT_FONT);
    add(selectShapeType);

    saveShape = new JButton("Save Shape");
    saveShape.setBackground(BUTTON_COLOR);


    add(saveShape);

    deleteShape = new JButton("Delete Shape");
    add(deleteShape);

    selectTick = new JComboBox<String>();
    selectTick.setFont(TEXT_FONT);
    add(selectTick);

    keyFrameTick = createField("Tick");

    keyFrameX = createField("X");

    keyFrameY = createField("Y");

    keyFrameWidth = createField("Width");

    keyFrameHeight = createField("Height");

    keyFrameR = createField("R");

    keyFrameG = createField("G");

    keyFrameB = createField("B");

    saveKeyframe = new JButton("Save Keyframe");
    add(saveKeyframe);

    deleteKeyframe = new JButton("Delete Keyframe");
    add(deleteKeyframe);
  }

  /**
   * Returns the integer representation of the string OR null.
   * @param s The string to parse into an integer.
   * @return  The parsed integer or null if the string was null.
   */
  private static Integer integerOrNull(String s) {
    if (s == null || s == NEW_KEYFRAME) {
      return null;
    } else {
      return Integer.parseInt(s);
    }
  }

  /**
   * Add editor controls to this panel.
   * @param editorControls  The listener for the edit events from this editor panel.
   */
  public void addEditorControls(EditorControls editorControls) {
    selectShape.addActionListener(evt -> editorControls.selectShape(
            (String) selectShape.getSelectedItem()));

    selectTick.addActionListener(evt -> editorControls.selectTick(
            selectShape.getSelectedItem().toString(),
            integerOrNull((String) selectTick.getSelectedItem())));

    addSaveShapeListener(editorControls);

    deleteShape.addActionListener(evt -> editorControls.deleteShape(
            selectShape.getSelectedItem().toString()));


    saveKeyframe.addActionListener(evt -> editorControls.saveKeyFrame(
            selectShape.getSelectedItem().toString(),
            keyFrameTick.getText(),
            keyFrameX.getText(),
            keyFrameY.getText(),
            keyFrameWidth.getText(),
            keyFrameHeight.getText(),
            keyFrameR.getText(),
            keyFrameG.getText(),
            keyFrameB.getText()));


    deleteKeyframe.addActionListener(evt -> editorControls.deleteKeyFrame(
            (String) selectShape.getSelectedItem(), (String) selectTick.getSelectedItem()));

  }

  /**
   * Listener for the saveShape button. This is here because save shape actually could correspond
   * to two different actions.
   * @param editorControls the controls it can use.
   */
  private void addSaveShapeListener(EditorControls editorControls) {
    saveShape.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (NEW_SHAPE.equals(selectShape.getSelectedItem())) {
          // We are adding a new shape.
          editorControls.addShape(shapeName.getText(),
                  (String) selectShapeType.getSelectedItem());
        }
        // Rename a Shape
        else {
          // Otherwise, we are renaming (if the text is different,
          // that's for the controller to figure out).
          editorControls.renameShape(
                  (String) selectShape.getSelectedItem(),
                  shapeName.getText(),
                  (String) selectShapeType.getSelectedItem());
        }
      }
    });
  }

  /**
   * Create a field coupled with a label and add it to the editor panel.
   * @param name  The named label of the field to use.
   * @return
   */
  private JTextField createField(String name) {
    JPanel group = new JPanel(new BorderLayout());
    group.setBackground(EDITOR_PANEL_BACKGROUND);
    JTextField field = new JTextField();
    field.setBackground(BUTTON_COLOR);
    field.setFont(TEXT_FONT);
    field.setPreferredSize(MAX_FIELD_SIZE);
    JLabel label = new JLabel(name);
    group.add(label, BorderLayout.CENTER);
    group.add(field, BorderLayout.WEST);
    add(group);
    return field;
  }

  /**
   * Set the available shapes to edit in this panel.
   * @param shapes  The shapes to list as editable.
   */
  public void setAvailableShapes(List<String> shapes) {
    selectShape.removeAllItems();
    for (String s: shapes) {
      selectShape.addItem(s);
    }
    this.selectShape.addItem(NEW_SHAPE);
  }

  /**
   * Set the current shape that is being edited in this panel.
   * @param shape The shape that is being edited.
   */
  public void setCurrentShape(String shape) {
    shapeName.setText(shape);
  }

  /**
   * Set the type of the shape that is currently being edited.
   * @param shapeType The type of the shape that is being edited.
   */
  public void setShapeType(String shapeType) {
    if (shapeType.equals("rectangle")) {
      selectShapeType.setSelectedIndex(0);
    }
    else if (shapeType.equals("oval")) {
      selectShapeType.setSelectedIndex(1);
    }
  }

  /**
   * Set the available keyframe tick times.
   * @param ticks A list of the available tick times.
   */
  public void setAvailableTicks(List<String> ticks) {
    selectTick.removeAllItems();
    for (String s: ticks) {
      selectTick.addItem(s);
    }
    this.selectTick.addItem(NEW_KEYFRAME);
  }

  /**
   * Set the text field values that are available on this view.
   * @param textFields  The field properties that should be set as strings.
   */
  public void setTextFields(List<String> textFields) {
    if (textFields.size() > 8) {
      throw new IllegalArgumentException("Too many components!");
    }

    keyFrameTick.setText(textFields.get(0));
    keyFrameX.setText(textFields.get(1));
    keyFrameY.setText(textFields.get(2));
    keyFrameWidth.setText(textFields.get(3));
    keyFrameHeight.setText(textFields.get(4));
    keyFrameR.setText(textFields.get(5));
    keyFrameG.setText(textFields.get(6));
    keyFrameB.setText(textFields.get(7));
  }
}
