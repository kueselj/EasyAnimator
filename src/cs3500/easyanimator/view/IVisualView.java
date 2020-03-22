package cs3500.easyanimator.view;

/**
 * View interface that extends IAnimatorView, adds visual functionality to the interface.
 */
public interface IVisualView extends IAnimatorView {
  /**
   * Refresh the view to reflect any changes in the game state.
   */
  void refresh();

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();
}
