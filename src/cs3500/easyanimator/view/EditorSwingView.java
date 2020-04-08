package cs3500.easyanimator.view;

import cs3500.easyanimator.controller.EditorListener;
import cs3500.easyanimator.model.IAnimatorModelViewOnly;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.ShapeNameVisitor;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.SortedMap;

/**
 * Implementation of an EnhancedView that delegates base operations to a VisualView.
 */
public class EditorSwingView implements IEnhancedView {
  private final EditorListener listener;
  private final Timer timer;
  private int tick;
  private boolean looping;

  private IAnimatorModelViewOnly model;

  @Override
  public void setModel(IAnimatorModelViewOnly model) {
    // When we get a new view we need to update several things.
    this.model = model;
    updateShapes();
  }

  // GRAPHICS
  private static final Dimension EDITOR_PANEL_SIZE = new Dimension(300, 500);
  private static final Color EDITOR_PANEL_BACKGROUND = Color.GRAY;

  /**
   * A small helper method to create the editor panel. This will return something that will stack
   * components vertically in our editor.
   * @return  The newly created panel.
   */
  private JPanel createEditorPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setMinimumSize(EDITOR_PANEL_SIZE);
    panel.setMaximumSize(EDITOR_PANEL_SIZE);
    panel.setBackground(EDITOR_PANEL_BACKGROUND);
    return panel;
  }

  private static Dimension MAX_FIELD_SIZE = new Dimension(200, 25);

  /**
   * Create a field coupled with a label and add it to the editor panel.
   * @param name  The named label of the field to use.
   * @param editorPanel The panel to add the field group to.
   * @return
   */
  private JTextField createField(String name, JPanel editorPanel) {
    JPanel group = new JPanel(new BorderLayout());
    JTextField field = new JTextField();
    field.setPreferredSize(MAX_FIELD_SIZE);
    JLabel label = new JLabel(name);
    group.add(label, BorderLayout.CENTER);
    group.add(field, BorderLayout.WEST);
    editorPanel.add(group);
    return field;
  }

  /**
   * Create a button and link it to the given action listener with the given action command.
   * Add to the given panel
   * @param name  The button name.
   * @param listener  The listener to use.
   * @param command The string to use in the action command.
   * @param panel The panel to add yourself to.
   */
  private JButton addButton(String name, ActionListener listener, String command, JPanel panel) {
    JButton button = new JButton(name);
    button.setActionCommand(command);
    button.addActionListener(listener);
    panel.add(button);
    return button;
  }

  /**
   * A PLAYBACK_ACTION is a set of actions to do when adjusting playback.
   */
  private enum PLAYBACK_ACTION { PLAY, PAUSE, RESTART, TOGGLELOOP, SPEEDUP, SPEEDDOWN,
    TICKUP, TICKDOWN }

  // We have the listener for those actions below.
  // See the justification for this style here, https://stackoverflow.com/a/5937586.

  // The time to change the delay by.
  // By default, changing the tps by 5.
  private static int TIMER_CHANGE = 20;

  /**
   * A PlaybackListener is a class to listen to playback commands.
   */
  private class PlaybackListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String ac = e.getActionCommand();
      // Since these enums are singleton instances we can actually do a pointer comparison.
      if (ac == PLAYBACK_ACTION.PLAY.name()) {
        if (model != null) {
          // Without a model, if we start there will be errors.
          timer.start();
        }
      }
      // PAUSE
      else if (ac == PLAYBACK_ACTION.PAUSE.name()) {
        timer.stop();
      }
      // RESTART
      else if (ac == PLAYBACK_ACTION.RESTART.name()) {
        timer.stop();
        tick = 0;
        refresh();
      }
      // SPEEDUP
      else if (ac == PLAYBACK_ACTION.SPEEDUP.name()) {
        // Max not necessary here, but here for good practice as a reminder.
        if (timer.getDelay() >= 1) {
          setSpeed(timer.getDelay() - 1);
        }
        System.out.println(timer.getDelay());
      }
      // SPEEDDOWN
      else if (ac == PLAYBACK_ACTION.SPEEDDOWN.name()) {
        setSpeed(timer.getDelay() + 1);

        System.out.println(timer.getDelay());
      }
      //TOGGLELOOP
      else if (ac == PLAYBACK_ACTION.TOGGLELOOP.name()) {
        looping = !looping;
      }
      //TICKUP
      else if (ac == PLAYBACK_ACTION.TICKUP.name()) {
        if (tick == model.getMaxTick()) {
          tick = 0;
        } else {
          tick = tick + 1;
        }
        mainPanel.setShapes(model.getShapesAtTick(tick));
        mainPanel.repaint();
        updateTickLabel();
      }
      //TICKDOWN
      else if (ac == PLAYBACK_ACTION.TICKDOWN.name()) {

        if (tick == 0) {
          tick = model.getMaxTick();
        }
        else tick = tick - 1;
        mainPanel.setShapes(model.getShapesAtTick(tick));
        mainPanel.repaint();
        updateTickLabel();
      }
      else {
        // We don't add events to this listener without setting the action command.
        throw new IllegalStateException("This branch should have been unreachable");
      }
    }
  }

  /**
   * A EDITING_ACTION is a set of actions having to do when changing shape details.
   */
  private enum EDITING_ACTION { SELECT_SHAPE, SAVE_SHAPE, DELETE_SHAPE,
    SELECT_KEYFRAME, SAVE_KEYFRAME, DELETE_KEYFRAME }


  private static Toolkit tk = Toolkit.getDefaultToolkit();

  /**
   * Rings the system bell if there was a failure of an operation from the controller.
   * @param success The boolean of success to use.
   */
  private static void bellOnFail(boolean success) {
    if (!success) {
      tk.beep();
    }
  }

  /**
   * An EditingListener is an action listener for all the commands the user may enter having to
   * do with editing.
   */
  private class EditingListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      String ac = e.getActionCommand();

      // Some useful variables.
      String selectedShapeString = (String) selectShape.getSelectedItem();
      if (selectedShapeString == null) {
        bellOnFail(false);
        return;
      } else {
        selectedShapeString = selectedShapeString.trim();
      }

      // I use ██████████ to show the different actions.
      // ██████████ SELECTING A SHAPE ██████████
      if (ac == EDITING_ACTION.SELECT_SHAPE.name()) {
        selectShape(selectedShapeString);

        // ██████████ OVERRIDING / CREATING A SHAPE ██████████
      } else if (ac == EDITING_ACTION.SAVE_SHAPE.name()) {
        String newShapeName = shapeName.getText().trim();
        String newShapeType = (String) shapeType.getSelectedItem();

        // Check if they tried to name a shape New Shape.
        if (newShapeName.equals(NEW_SHAPE)) {
          // Naughty user.
          bellOnFail(false);
          return;
        }
        // Are they trying to rename?
        if (!selectedShapeString.equals(NEW_SHAPE) && !selectedShapeString.equals(newShapeName)) {
          // Override.
          bellOnFail(listener.renameShape(selectedShapeString, newShapeName, newShapeType));
        } else {
          // Adding a new shape.
          bellOnFail(listener.addShape(newShapeName, newShapeType));
        }
        // No matter what happened, let's update.
        updateShapes();

        // ██████████ DELETING A SHAPE ██████████
      } else if (ac == EDITING_ACTION.DELETE_SHAPE.name()) {
        bellOnFail(listener.removeShape(selectedShapeString));
        updateShapes();

        // ██████████ SELECTING A KEYFRAME ██████████
      } else if (ac == EDITING_ACTION.SELECT_KEYFRAME.name()) {
        selectKeyframe(selectedShapeString,
                getTickSelected());

        // ██████████ SAVING A KEYFRAME ██████████
      } else if (ac == EDITING_ACTION.SAVE_KEYFRAME.name()) {
        // This is very similar to save shape.
        Integer tickSelected = getTickSelected();
        Integer tickSet;
        try {
          tickSet = Integer.parseInt(keyFrameTick.getText());
        } catch (NumberFormatException nfe) {
          bellOnFail(false);
          return;
        }

        if (!tickSet.equals(tickSelected) && tickSelected != null) {
          // We need to move the keyframe before overriding it..
          bellOnFail(listener.removeKeyframe(selectedShapeString, tickSelected));
        }
        // Overriding and moving.
        bellOnFail(listener.setKeyframe(selectedShapeString,
                tickSet,
                keyFrameWidth.getText(), keyFrameHeight.getText(),
                keyFrameX.getText(), keyFrameY.getText(),
                keyFrameR.getText(), keyFrameG.getText(), keyFrameB.getText()));

        updateKeyframeSelector(selectedShapeString);

        // ██████████ DELETING A KEYFRAME ██████████
      } else if (ac == EDITING_ACTION.DELETE_KEYFRAME.name()) {
        bellOnFail(listener.removeKeyframe(selectedShapeString, getTickSelected()));
        updateKeyframeSelector(selectedShapeString);
      } else {
        throw new IllegalStateException("This branch of the if statement " +
                "should have been impossible to reach.");
      }
    }
  }

  /**
   * Class for handling a key pressed, will pass off to the controller.
   */
  private class SaveListener implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
      //do nothing.
    }

    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyChar() == 's') {
        listener.saveModel();
      }
      else if (e.getKeyChar() == 'l') {
        listener.loadModel();
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      //do nothing.
    }
  }

  private static String[] DEFAULT_SHAPES = new String[]{"rectangle", "oval"};
  private static Dimension MINIMUM_WINDOW_SIZE = new Dimension(800, 600);
  private static Dimension MAX_PLAYBACK_BUTTONS_SIZE = new Dimension(Integer.MAX_VALUE, 50);

  /**
   * Create a new editor window linking it to a listener for model and other program actions.
   * @param listener  A class to listener and respond to different macro user events as they happen
   *                  in the editor.
   */
  public EditorSwingView(EditorListener listener) {
    super();

    // Editor stuff that is not graphics.
    this.listener = listener;
    //this.saveListener = listener;
    this.timer = new Timer(30, e -> this.refresh());
    this.tick = 0;
    this.looping = true;

    frame = new JFrame("EasyAnimator");

    // WINDOW SETTINGS
    frame.setTitle("EasyAnimator");
    frame.setMinimumSize(MINIMUM_WINDOW_SIZE);
    frame.setResizable(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // ADDING COMPONENTS
    frame.setLayout(new BorderLayout());
    frame.addKeyListener(saveKeyListener);


    playbackListener = new PlaybackListener();
    // I was forced to put this listener not as a field ...
    // but a local variable by the dreaded style checker.
    ActionListener editingListener = new EditingListener();
    saveKeyListener = new SaveListener();

    // THE EDITOR ON THE RIGHT
    // Next, we want the basic layout that everything is going to sit on.
    JPanel editorPanel = createEditorPanel();
    // SHAPE CONTROLS, we don't yet have a model so it will be blank.
    this.selectShape = new JComboBox<>();
    selectShape.setActionCommand(EDITING_ACTION.SELECT_SHAPE.name());
    selectShape.addActionListener(editingListener);
    selectShape.addKeyListener(saveKeyListener);
    editorPanel.add(selectShape);

    this.shapeName = createField("Shape Name", editorPanel);
    this.shapeType = new JComboBox<>(DEFAULT_SHAPES);
    editorPanel.add(shapeType);
    this.shapeType.addKeyListener(saveKeyListener);
    // I was made to make these local variables by the style checker.
    JButton saveShape = addButton("Save Shape",
            editingListener, EDITING_ACTION.SAVE_SHAPE.name(),
            editorPanel);
    JButton deleteShape = addButton("Delete Shape",
            editingListener, EDITING_ACTION.DELETE_SHAPE.name(),
            editorPanel);

    // KEYFRAME CONTROLS, we don't yet have the model so this too will be blank.
    selectTick = new JComboBox<>();
    selectTick.setActionCommand(EDITING_ACTION.SELECT_KEYFRAME.name());
    selectTick.addActionListener(editingListener);
    selectTick.addKeyListener(saveKeyListener);
    editorPanel.add(selectTick);
    editorPanel.addKeyListener(saveKeyListener);


    // createField adds it to the panel for us, how generous.
    this.keyFrameTick = createField("Keyframe Tick", editorPanel);
    this.keyFrameX = createField("X", editorPanel);
    this.keyFrameY = createField("Y", editorPanel);
    this.keyFrameWidth = createField("Width", editorPanel);
    this.keyFrameHeight = createField("Height", editorPanel);
    this.keyFrameR = createField("Red", editorPanel);
    this.keyFrameG = createField("Green", editorPanel);
    this.keyFrameB = createField("Blue", editorPanel);

    // Buttons at the bottom to finalize changes.
    JButton saveModification = addButton("Save Keyframe (Change)",
            editingListener, EDITING_ACTION.SAVE_KEYFRAME.name(),
            editorPanel);

    JButton deleteKeyFrame = addButton("Delete Keyframe (Change)",
            editingListener, EDITING_ACTION.DELETE_KEYFRAME.name(),
            editorPanel);

    frame.add(editorPanel, BorderLayout.WEST);

    // ONTO THE OTHER COMPONENTS
    // We'll add the player at the top of this panel, then the playback buttons.
    JPanel playbackPanel = new JPanel();
    playbackPanel.setLayout(new BoxLayout(playbackPanel, BoxLayout.Y_AXIS));

    this.mainPanel = new AnimationPanel();
    this.mainPanel.addKeyListener(saveKeyListener);
    JScrollPane scrollPane = new JScrollPane(this.mainPanel);

    playbackPanel.add(scrollPane);

    // Now we add a button panel full of playback controls.
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout());
    // We give the playback controls a tint.
    buttonPanel.setBackground(new Color(106, 35, 38));
    buttonPanel.setMaximumSize(MAX_PLAYBACK_BUTTONS_SIZE);
    playbackPanel.add(buttonPanel);

    // Onto buttons.
    JButton startButton = addButton("Play",
            playbackListener, PLAYBACK_ACTION.PLAY.name(),
            buttonPanel);

    JButton pauseButton = addButton("Pause",
            playbackListener, PLAYBACK_ACTION.PAUSE.name(),
            buttonPanel);

    JButton restartButton = addButton("Restart",
            playbackListener, PLAYBACK_ACTION.RESTART.name(),
            buttonPanel);

    JButton toggleLoop = addButton("Toggle Loop",
            playbackListener, PLAYBACK_ACTION.TOGGLELOOP.name(),
            buttonPanel);

    JButton increaseSpeedButton = addButton("^ Speed",
            playbackListener, PLAYBACK_ACTION.SPEEDUP.name(),
            buttonPanel);

    JButton decreaseSpeedButton = addButton("v Speed",
            playbackListener, PLAYBACK_ACTION.SPEEDDOWN.name(),
            buttonPanel);

    JButton tickUp = addButton("^ Tick",
            playbackListener, PLAYBACK_ACTION.TICKUP.name(),
            buttonPanel);

    JButton tickDown = addButton("v Tick",
            playbackListener, PLAYBACK_ACTION.TICKDOWN.name(),
            buttonPanel);

    this.tickLabel = new JLabel();


    buttonPanel.add(tickLabel);

    this.frame.add(playbackPanel);
    this.frame.addKeyListener(saveKeyListener);
  }



  // All the graphics fields we use.
  // We interact with these in the code below.
  private JFrame frame;
  private JLabel tickLabel;

  // SHAPE SELECTOR
  private KeyListener saveKeyListener;
  private JComboBox selectShape;
  private JTextField shapeName;
  private JComboBox<String> shapeType;

  // KEYFRAME EDITING
  private JComboBox selectTick;
  private JTextField keyFrameTick;
  private JTextField keyFrameX;
  private JTextField keyFrameY;
  private JTextField keyFrameWidth;
  private JTextField keyFrameHeight;
  private JTextField keyFrameR;
  private JTextField keyFrameG;
  private JTextField keyFrameB;

  // PLAYBACK
  private AnimationPanel mainPanel;
  private ActionListener playbackListener;


  @Override
  public void makeVisible() {
    this.frame.setVisible(true);
  }

  /**
   * A custom method to use when refreshing playback.
   */
  private void refresh() {
    if (this.model.getMaxTick() == 0) {
      // A special rule for if the model is in a particularly weird state.
      this.tick = 0;
      this.timer.stop();
    } else if (this.looping) {
      this.tick = (this.tick + 1) % model.getMaxTick();
    } else {
      if (this.tick == model.getMaxTick()) {
        this.timer.stop();
        this.tick = 0;
      } else {
        this.tick += 1;
      }
    }
    this.mainPanel.setShapes(model.getShapesAtTick(this.tick));
    this.mainPanel.repaint();
    this.updateTickLabel();
  }

  /**
   * Simply updates the tick label to the current tick.
   */
  private void updateTickLabel() {
    this.tickLabel.setText("   " + this.tick);
  }

  @Override
  public void setSpeed(double speed) {
    this.timer.setDelay((int) speed);
  }

  private static String NEW_KEYFRAME = "New Keyframe";
  private static String NEW_SHAPE = "New Shape";

  /**
   * A helper method to get the current keyframe tick selected if applicable.
   * @return  The current keyframe index or null if not a number.
   */
  private Integer getTickSelected() {
    String selectedItem = (String) this.selectTick.getSelectedItem();
    if (!NEW_KEYFRAME.equals(selectedItem)) {
      if (selectedItem != null) {
        return Integer.parseInt(selectedItem);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * A helper method to update the shape selector.
   */
  private void updateShapes() {
    this.selectShape.removeAllItems();
    for (String name: model.getShapeNames()) {
      this.selectShape.addItem(name);
    }
    this.selectShape.addItem(NEW_SHAPE);
  }

  private IShapeVisitor<String> getName = new ShapeNameVisitor();

  /**
   * A helper method to update fields below when selecting a shape of the given name.
   * @param name  The name of the shape to update fields with.
   */
  private void selectShape(String name) {
    // If the shape doesn't exist then we don't bother updating fields.
    if (!model.getShapes().containsKey(name)) {
      return;
    }
    // Shape fields.
    shapeName.setText(name);
    shapeType.setSelectedItem(model.getShapes().get(name).accept(getName));
    // Keyframe fields.
    updateKeyframeSelector(name);
    if (model.getKeyframes(name).size() > 0) {
      selectKeyframe(name, model.getKeyframes(name).firstKey());
    }
    // We don't select a keyframe if there are no keyframes.
    // This will leave the keyframes fields on whatever they were previously.
    // I'm okay with this.
  }

  /**
   * A helper method to update the keyframe selector.
   * @param name  The name of the shape to use to lookup keyframes.
   */
  private void updateKeyframeSelector(String name) {
    selectTick.removeAllItems();
    for (Integer tick: model.getKeyframes(name).keySet()) {
      selectTick.addItem(Integer.toString(tick));
    }
    selectTick.addItem(NEW_KEYFRAME);
  }

  // We use this number as the default for everything.
  private static String DEFAULT = "100";


  /**
   * A helper method to update fields below when selecting a keyframe of the given integer.
   * If the tick is between two keyframes we get the mid point with getShapeAtTick.
   * Otherwise we use the closest keyframe values.
   * Or we set them to the defaults.
   * @param name    The name of the shape to use when selecting a keyframe.
   * @param tick    The tick of the keyframe we have selected.
   */
  private void selectKeyframe(String name, Integer tick) {
    String w = DEFAULT;
    String h = DEFAULT;
    String x = DEFAULT;
    String y = DEFAULT;
    String r = DEFAULT;
    String g = DEFAULT;
    String b = DEFAULT;
    // If we have actually selected the New Keyframe we just want to use the playback tick.
    if (tick == null) {
      tick = this.tick;
    }
    if (name == null || !model.getShapeNames().contains(name)) {
      return;
    }
    SortedMap<Integer, IShape> keyframes = model.getKeyframes(name);
    if (keyframes.size() > 0) {
      IShape state;
      SortedMap<Integer, IShape> tailMap = keyframes.tailMap(tick);
      SortedMap<Integer, IShape> headMap = keyframes.headMap(tick);
      if (keyframes.containsKey(tick)) {
        state = keyframes.get(tick);
        // I have this extra from the bottom since I think getShapeAtTick may have rounding errors.
      } else if (tailMap.size() > 0 && headMap.size() > 0) {
        state = model.getShapeAtTick(name, tick);
      } else if (tailMap.size() > 0) {
        state = keyframes.get(tailMap.firstKey());
      } else {
        state = keyframes.get(headMap.firstKey());
      }
      w = Integer.toString(state.getSize().getWidth());
      h = Integer.toString(state.getSize().getHeight());
      x = Integer.toString(state.getPosition().getX());
      y = Integer.toString(state.getPosition().getY());
      r = Integer.toString(state.getColor().getRed());
      g = Integer.toString(state.getColor().getGreen());
      b = Integer.toString(state.getColor().getBlue());
    }
    // We do NOT set the selected tick. That must be done somewhere else.
    this.keyFrameTick.setText(Integer.toString(tick));
    this.keyFrameWidth.setText(w);
    this.keyFrameHeight.setText(h);
    this.keyFrameX.setText(x);
    this.keyFrameY.setText(y);
    this.keyFrameR.setText(r);
    this.keyFrameG.setText(g);
    this.keyFrameB.setText(b);
  }
}
