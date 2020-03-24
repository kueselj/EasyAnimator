package cs3500.easyanimator.view;

/**
 * View interface that extends IAnimatorView, adds visual functionality to the interface.
 */
public interface IVisualView extends IAnimatorView {
  /**
   * Refresh the view to reflect any changes in the game state.
   * @throws IllegalStateException  If the model was not previously set.
   */
  void refresh();
}