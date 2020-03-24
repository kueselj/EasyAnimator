package cs3500.easyanimator.view;

/**
 * A BasicViewFactory is an IViewFactory that can construct the basic set of views: text, svg, and
 * visual.
 */
public class BasicViewFactory implements IViewFactory {
  @Override
  public IAnimatorView getView(String name, Appendable out) {
    switch (name) {
      case "text":
        return new TextualView();
      case "svg":
        return new SVGAnimationView();
      case "visual":
        return new SwingView();
      default:
        throw new IllegalArgumentException("Invalid view type given.");
    }
  }
}
