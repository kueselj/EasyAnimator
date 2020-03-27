package testmodel.testshape;

import cs3500.easyanimator.model.shapes.BasicShapeFactory;
import cs3500.easyanimator.model.shapes.IShapeFactory;

/**
 * Apply the tests for shape factories to our basic implementation.
 */
public class TestBasicShapeFactory extends IShapeFactoryTest {

  @Override
  IShapeFactory getInstance() {
    return new BasicShapeFactory();
  }
}
