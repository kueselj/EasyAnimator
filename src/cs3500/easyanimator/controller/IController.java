package cs3500.easyanimator.controller;

import cs3500.easyanimator.view.IAnimatorView;

/**
 * An IController is an interface to describe a general controller for our program.
 */
public interface IController {

  /**
   * Set the view of the controller. This needs to happen after view construction.
   * @param view  The view to couple to the controller.
   * @param speed The speed to set on the view.
   */
  void setView(IAnimatorView view, double speed);

  /**
   * Start the program, i.e. give control to the controller
   */
  void start();
}
