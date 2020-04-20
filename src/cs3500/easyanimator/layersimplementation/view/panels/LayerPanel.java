package cs3500.easyanimator.layersimplementation.view.panels;

import cs3500.easyanimator.layersimplementation.controller.LayerControls;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * JPanel that holds all the layer components.
 */
public class LayerPanel extends JPanel {

  private static final Dimension EDITOR_PANEL_SIZE = new Dimension(300, 500);
  private static final Color EDITOR_PANEL_BACKGROUND = Color.BLACK;
  private static Dimension MAX_FIELD_SIZE = new Dimension(200, 25);
  private JComboBox<String> selectShape;

  private JTextField layerPosition;

  /**
   * Basic constructor for a layer panel.
   */
  public LayerPanel() {
    super();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setMinimumSize(EDITOR_PANEL_SIZE);
    setMaximumSize(EDITOR_PANEL_SIZE);
    setBackground(EDITOR_PANEL_BACKGROUND);

    selectShape = new JComboBox<String>();
    add(selectShape);

    layerPosition = createField("Layer Position");

  }

  /**
   * Passes the necessary information to the controller when certain actions are performed.
   * @param layerControls
   */
  public void addLayerControls(LayerControls layerControls) {
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


  public void setLayers(List<Integer> layers) {
  }
}
