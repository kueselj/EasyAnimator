package cs3500.easyanimator.layersimplementation.controller;

public interface ILayerMVCController {

  /**
   * Will start the animation with the given model and view.
   * @throws IllegalArgumentException if the model or view is null, or the speed has not been set.
   */
  void go();

  /**
   * Refreshes the controller, controller will request all needed information from the model
   * and pass it to the respective views.
   */
  void refreshTick();

  /**
   * Requests shapes from model and passes them to view for the current tick.
   */
  void refreshDrawing();

  /**
   * Sets the tick speed to the given speed.
   * @param tps the tick speed to use.
   */
  void setSpeed(double tps);
}
