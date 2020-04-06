package cs3500.easyanimator.view;

import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.WidthHeight;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * View interface that extends IAnimatorView, adds visual functionality to the interface.
 */
public interface IVisualView extends IAnimatorView {
  /**
   * Refresh the view to reflect any changes in the game state.
   *
   * @throws IllegalStateException If the model was not previously set.
   */
  void refresh();

  /**
   * Sets the size of the view to the given WidthHeight.
   * @param wH the WidthHeight to use.
   */
  void setViewSize(WidthHeight wH);

  /**
   * Sets the shapes of the view to display.
   * @param shapesAtTick the shapes to display at a given tick.
   */
  void setShapes(List<IShape> shapesAtTick);

  void addActionListeners(ActionListener... listeners);

}
