package testmodel.testlayers;

import org.junit.Test;

import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.layers.BasicLayer;
import cs3500.easyanimator.model.layers.ILayer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * A small class to test ILayer's basic methods. This is written in terms of BasicLayer because of
 * the expectation of not having any more implementations. We use EasyAnimators for the models.
 */
public class TestILayer {
  private static IAnimatorModel MODEL = new EasyAnimator();
  private static String NAME = "Here's my name!";
  private static boolean VISIBILITY = true;
  private static ILayer LAYER = new BasicLayer(NAME, VISIBILITY, MODEL);

  /**
   * A small rule to check getName.
   */
  @Test
  public void testGetName() {
    assertEquals("Expected to get layer name.",
            NAME, LAYER.getName());
  }

  /**
   * A small rule to check getVisibility.
   */
  @Test
  public void testGetVisibility() {
    assertEquals("Expected visibility to be true, since we set that.",
            VISIBILITY, LAYER.getVisibility());
  }

  /**
   * A small rule to check getModel.
   */
  @Test
  public void testGetModel() {
    assertEquals("Expected model to match by reference.",
            MODEL, LAYER.getModel());
  }

  /**
   * A small rule to check setVisibility.
   */
  @Test
  public void testSetVisibility() {
    ILayer newLayer = LAYER.setVisibility(false);
    assertFalse("Expected new layer visibility to match what we set.",
            newLayer.getVisibility());
    assertEquals("Expected new layer name to match the old one.",
            NAME, newLayer.getName());
    assertEquals("Expected new layer model to match the old one.",
            MODEL, newLayer.getModel());
  }

  /**
   * A small rule to check setName.
   */
  @Test
  public void testSetName() {
    String newName = "Heyo, this is a new name.";
    ILayer newLayer = LAYER.setName(newName);
    assertEquals("Expected new layer visibility to match what we set.",
            newName, newLayer.getName());
    assertEquals("Expected new layer visibility to match the old one.",
            VISIBILITY, newLayer.getVisibility());
    assertEquals("Expected new layer model to match the old one.",
            MODEL, newLayer.getModel());
  }
}
