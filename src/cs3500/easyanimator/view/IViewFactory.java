package cs3500.easyanimator.view;

/**
 * An IViewFactory helps us build a factory from a string description. It is a way of letting the
 * user pick what view they are using with our application without tying our launcher code down
 * to the specific implementations.
 */
public interface IViewFactory {
  /**
   * Give an instance of a view with the given name.
   * @param name  The name of the view we want to use.
   *              See implementation for options. Though by default text, visual, and svg.
   * @param out   An appendable location for output. This is necessary for text, and svg views.
   * @return      A newly initialized view. The model has yet to be set.
   */
  IAnimatorView getView(String name, Appendable out);
}
