package testview;

import cs3500.easyanimator.view.BasicViewFactory;
import cs3500.easyanimator.view.IViewFactory;

/**
 * Run the view factory test suite for the basic view factory implementation.
 */
public class TestBasicViewFactory extends TestIViewFactory {

  @Override
  IViewFactory getInstance() {
    return new BasicViewFactory();
  }


}
