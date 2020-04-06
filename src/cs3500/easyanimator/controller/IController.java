package cs3500.easyanimator.controller;

public interface IController {

  /**
   * Start the program, i.e. give control to the controller
   */
  void go();

  /**
   * Refreshes the controller.
   */
  void refresh();
}
