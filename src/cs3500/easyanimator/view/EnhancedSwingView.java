package cs3500.easyanimator.view;

import cs3500.easyanimator.model.IAnimatorModelViewOnly;

import javax.swing.*;

/**
 * Implementation of an EnhancedView that delegates base operations to a VisualView.
 */
public class EnhancedSwingView implements IEnhancedVisualView {

  private IVisualView delegateView;
  private JButton startButton;
  private JButton pauseButton;
  private JButton resumeButton;
  private JButton restartButton;



  /**
   * Constructor for an enhanced view, takes in a IVisualVew to delegate base operations to.
   * @param delegateView the view to delegate to.
   */
  public EnhancedSwingView(IVisualView delegateView) {
    this.delegateView = delegateView;
  }

  @Override
  public void refresh() {
    this.delegateView.refresh();
  }

  @Override
  public void makeVisible() {
    this.delegateView.makeVisible();
  }

  @Override
  public void setModel(IAnimatorModelViewOnly model) {
    this.delegateView.setModel(model);
  }

  @Override
  public void start() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void restart() {

  }

  @Override
  public void enableLooping() {

  }

  @Override
  public void disableLooping() {

  }

  @Override
  public void setSpeed(double speed) {

  }
}
