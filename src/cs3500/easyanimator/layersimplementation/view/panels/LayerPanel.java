package cs3500.easyanimator.layersimplementation.view.panels;

import cs3500.easyanimator.layersimplementation.controller.LayerControls;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import java.awt.*;

import java.util.List;

/**
 * JPanel that holds all the layer components.
 */
public class LayerPanel extends JPanel {

  private static final Dimension EDITOR_PANEL_SIZE = new Dimension(300, 500);
  private static final Color EDITOR_PANEL_BACKGROUND = new Color(51, 107, 135);
  private static final Color BUTTON_COLOR = new Color(188, 223, 255);
  private static Dimension MAX_FIELD_SIZE = new Dimension(200, 25);


  private static final Font TEXT_FONT = new Font("Courier", Font.PLAIN, 20);
  private static final Font BUTTON_FONT = new Font("Impact", Font.PLAIN, 12);

  private final JComboBox<String> selectedLayer;
  private final JTextField layerName;
  private final JButton saveLayer;
  private final JButton deleteLayer;
  private final JButton moveLayerUp;
  private final JButton moveLayerDown;

  /**
   * Basic constructor for a layer panel.
   */
  public LayerPanel() {
    super();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setMinimumSize(EDITOR_PANEL_SIZE);
    setMaximumSize(EDITOR_PANEL_SIZE);
    setBackground(EDITOR_PANEL_BACKGROUND);


    JPanel dummyPanel1 = new JPanel();
    dummyPanel1.setPreferredSize(new Dimension(200, 250));
    dummyPanel1.setBackground(EDITOR_PANEL_BACKGROUND);
    add(dummyPanel1);
    selectedLayer = new JComboBox<String>();
    selectedLayer.setFont(TEXT_FONT);
    add(selectedLayer);
    layerName = createField("Layer Name");

    moveLayerUp = new JButton("Move Layer Up");
    //moveLayerUp.setFont(BUTTON_FONT);
    add(moveLayerUp);
    moveLayerDown = new JButton("Move Layer Down");
    //moveLayerDown.setFont(BUTTON_FONT);
    add(moveLayerDown);
    saveLayer = new JButton("Save Layer");
    //saveLayer.setFont(BUTTON_FONT);
    add(saveLayer);
    deleteLayer = new JButton("Delete Layer");
    //deleteLayer.setFont(BUTTON_FONT);
    add(deleteLayer);
    JPanel dummyPanel2 = new JPanel();
    dummyPanel2.setPreferredSize(new Dimension(200, 250));
    dummyPanel2.setBackground(EDITOR_PANEL_BACKGROUND);
    add(dummyPanel2);
  }

  /**
   * Passes the necessary information to the controller when certain actions are performed.
   * @param layerControls The instance of the controller to query with view requests.
   */
  public void addLayerControls(LayerControls layerControls) {
    selectedLayer.addActionListener(e -> {
      layerControls.selectLayer((String) selectedLayer.getSelectedItem());
      layerName.setText((String) selectedLayer.getSelectedItem());
    });
    saveLayer.addActionListener(e ->
            layerControls.saveLayer((String) selectedLayer.getSelectedItem(),
                    layerName.getText()));
    deleteLayer.addActionListener(e ->
            layerControls.deleteLayer((String) selectedLayer.getSelectedItem()));
    // Up to the user means closer to being visible, that means they want a greater index.
    moveLayerUp.addActionListener(e ->
            layerControls.moveLayer((String) selectedLayer.getSelectedItem(), 1));
    moveLayerDown.addActionListener(e ->
            layerControls.moveLayer((String) selectedLayer.getSelectedItem(), -1));

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
   * Set the layers of this view (ordered by layer order).
   * @param layers  The list of layer names in this view.
   */
  public void setLayers(List<String> layers) {
    selectedLayer.removeAllItems();
    for (String s: layers) {
      selectedLayer.addItem(s);
    }
    selectedLayer.addItem("New Layer");
  }
}
