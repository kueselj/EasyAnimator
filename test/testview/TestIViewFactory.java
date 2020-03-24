package testview;

import org.junit.Test;

import cs3500.easyanimator.view.IAnimatorView;
import cs3500.easyanimator.view.IViewFactory;
import cs3500.easyanimator.view.SVGAnimationView;
import cs3500.easyanimator.view.SwingView;
import cs3500.easyanimator.view.TextualView;

import static org.junit.Assert.assertTrue;

/**
 * A collection of tests for any factory of views.
 */
public abstract class TestIViewFactory {
  /**
   * Return an instance of this particular factory's view factory for testing.
   * @return  The instance of the factory.
   */
  abstract IViewFactory getInstance();

  /**
   * Test when giving a string that doesn't match any view to throw an IllegalArgumentException.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNonexistentView() {
    // We assume there is no view with the given name below.
    getInstance().getView("There's no way this is the name of a view!", System.out);
  }

  /**
   * Test when giving the string "text", "svg", and "visual" that we get an instance of the right
   * view back.
   */
  @Test
  public void testExistentView() {
    IViewFactory factory = getInstance();
    IAnimatorView text = factory.getView("text", System.out);
    IAnimatorView svg = factory.getView("svg", System.out);
    IAnimatorView visual = factory.getView("visual", System.out);
    assertTrue("Expected text view to be an instance of our TextualView.",
            text instanceof TextualView);
    assertTrue("Expected svg view to be an instance of our SVGAnimationView.",
            svg instanceof SVGAnimationView);
    // If we ever implement a visual view outside swing's libraries this will be a weird test.
    // A better name would be swing, but that's a little weird for the user.
    assertTrue("Expected visual view to be an instance of our SwingView.",
            visual instanceof SwingView);
  }
}
