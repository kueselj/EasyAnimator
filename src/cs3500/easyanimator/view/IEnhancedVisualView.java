package cs3500.easyanimator.view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interface for an enhanced view. Allows starting, pausing, resuming, disabling/enabling
 * of looping and increasing or decreasing the speed of the animation.
 */
public interface IEnhancedVisualView extends IVisualView {
  void setAvailableShapes(List<String> shapes);

  void setAvailableShapeTicks(List<String> shapeTicks);

  void addSelectShapeActionListener(ActionListener listener);

  void addSaveKeyFrameActionListener(ActionListener listener);

  String getShapeSelected();

  String getTickSelected();

  void addSelectTickActionListener(ActionListener listener);

  void setTextFields(List<String> fields);

  List<String> getTextFields();
}
