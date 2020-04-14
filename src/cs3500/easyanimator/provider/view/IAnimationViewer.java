package cs3500.easyanimator.provider.view;

/**
 * Represents a visual animation view. Has a draw method to draw the panel and turn the animation
 * into a String.
 */
public interface IAnimationViewer {
  /**
   * Draws the latest frame onto the panel.
   */
  void draw();

  /**
   * Returns a string representing the view.
   *
   * @return a String of the state of the view panel
   */
  String toString();
}
