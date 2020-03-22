package cs3500.easyanimator.view;

/**
 * A view interface for an IAnimatorModel: Displays the animation.
 */
public interface IAnimatorView {
  /**
   * Refresh the view to reflect any changes in the game state.
   */
  void refresh();

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();
}
