package testexcellence;

import cs3500.easyanimator.util.BasicApplicationBuilder;
import cs3500.easyanimator.util.IApplicationBuilder;

/**
 * Enable testing for our implementation of the IApplicationBuilder.
 */
public class TestBasicApplicationBuilder extends TestIApplicationBuilder {

  @Override
  IApplicationBuilder getInstance() {
    return new BasicApplicationBuilder();
  }
}
