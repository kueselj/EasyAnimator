package cs3500.easyanimator.provider.model;

/**
 * An interface for the controller to interact with our adapted model. This is a necessity born out
 * of how the adapter model controls the current time as part of it's state. This means that state
 * can't be tracked in the controller, it must be in the model.
 */
public interface AdapterInterface {
  /**
   * Returns the given time in ticks of the model.
   * @return  The current time in ticks of the model.
   */
  int getTick();
}
