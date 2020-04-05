package testmodel.testeasyanimator;

import org.junit.Test;

import java.io.StringReader;

import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.util.AnimationBuilder;
import cs3500.easyanimator.util.AnimationReader;
import cs3500.easyanimator.util.EasyAnimatorModelBuilder;

/**
 * A test suite dedicated to ensuring that our model is compatible with the input files provided
 * in class by mocking the inputs that we will receive. This is an abstract series of tests in
 * anticipation of having two different implementations of models.
 */
public abstract class TestClarkInputFiles {
  /**
   * Get the builder optimized for this implementation of model.
   * @return  A new instance of a builder to use to build out a model.
   */
  abstract AnimationBuilder<IAnimatorModel> getBuilder();

  public static class EasyAnimatorTest extends TestClarkInputFiles {
    @Override
    AnimationBuilder<IAnimatorModel> getBuilder() {
      return new EasyAnimatorModelBuilder();
    }
  }

  private static String SMALLDEMO = "" +
          "# initializes the canvas, with top-left corner (200,70) and\n" +
          "# dimensions 360x360\n" +
          "canvas 200 70 360 360\n" +
          "# declares a rectangle shape named R\n" +
          "shape R rectangle\n" +
          "# describes the motions of shape R, between two moments of animation:\n" +
          "# t == tick\n" +
          "# (x,y) == position\n" +
          "# (w,h) == dimensions\n" +
          "# (r,g,b) == color (with values between 0 and 255)\n" +
          "#                  start                           end\n" +
          "#        --------------------------    ----------------------------\n" +
          "#        t  x   y   w  h   r   g  b    t   x   y   w  h   r   g  b\n" +
          "motion R 1  200 200 50 100 255 0  0    10  200 200 50 100 255 0  0\n" +
          "motion R 10 200 200 50 100 255 0  0    50  300 300 50 100 255 0  0\n" +
          "motion R 50 300 300 50 100 255 0  0    51  300 300 50 100 255 0  0\n" +
          "motion R 51 300 300 50 100 255 0  0    70  300 300 25 100 255 0  0\n" +
          "motion R 70 300 300 25 100 255 0  0    100 200 200 25 100 255 0  0\n" +
          "\n" +
          "shape C ellipse\n" +
          "motion C 6  440 70 120 60 0 0 255 # start state\n" +
          "         20 440 70 120 60 0 0 255 # end state\n" +
          "motion C 20 440 70 120 60 0 0 255      50 440 250 120 60 0 0 255\n" +
          "motion C 50 440 250 120 60 0 0 255     70 440 370 120 60 0 170 85\n" +
          "motion C 70 440 370 120 60 0 170 85    80 440 370 120 60 0 255 0\n" +
          "motion C 80 440 370 120 60 0 255 0     100 440 370 120 60 0 255 0\n";

  private static String TOH3TXT = "" +
          "canvas 145 50 410 220\n" +
          "shape disk1 rectangle\n" +
          "shape disk2 rectangle\n" +
          "shape disk3 rectangle\n" +
          "motion disk1 1 190 180 20 30 0 49 90  1 190 180 20 30 0 49 90\n" +
          "motion disk1 1 190 180 20 30 0 49 90  25 190 180 20 30 0 49 90\n" +
          "motion disk2 1 167 210 65 30 6 247 41  1 167 210 65 30 6 247 41\n" +
          "motion disk2 1 167 210 65 30 6 247 41  57 167 210 65 30 6 247 41\n" +
          "motion disk3 1 145 240 110 30 11 45 175  1 145 240 110 30 11 45 175\n" +
          "motion disk3 1 145 240 110 30 11 45 175  121 145 240 110 30 11 45 175\n" +
          "motion disk1 25 190 180 20 30 0 49 90  35 190 50 20 30 0 49 90\n" +
          "motion disk1 35 190 50 20 30 0 49 90  36 190 50 20 30 0 49 90\n" +
          "motion disk1 36 190 50 20 30 0 49 90  46 490 50 20 30 0 49 90\n" +
          "motion disk1 46 490 50 20 30 0 49 90  47 490 50 20 30 0 49 90\n" +
          "motion disk1 47 490 50 20 30 0 49 90  57 490 240 20 30 0 49 90\n" +
          "motion disk1 57 490 240 20 30 0 49 90  89 490 240 20 30 0 49 90\n" +
          "motion disk2 57 167 210 65 30 6 247 41  67 167 50 65 30 6 247 41\n" +
          "motion disk2 67 167 50 65 30 6 247 41  68 167 50 65 30 6 247 41\n" +
          "motion disk2 68 167 50 65 30 6 247 41  78 317 50 65 30 6 247 41\n" +
          "motion disk2 78 317 50 65 30 6 247 41  79 317 50 65 30 6 247 41\n" +
          "motion disk2 79 317 50 65 30 6 247 41  89 317 240 65 30 6 247 41\n" +
          "motion disk1 89 490 240 20 30 0 49 90  99 490 50 20 30 0 49 90\n" +
          "motion disk2 89 317 240 65 30 6 247 41  185 317 240 65 30 6 247 41\n" +
          "motion disk1 99 490 50 20 30 0 49 90  100 490 50 20 30 0 49 90\n" +
          "motion disk1 100 490 50 20 30 0 49 90  110 340 50 20 30 0 49 90\n" +
          "motion disk1 110 340 50 20 30 0 49 90  111 340 50 20 30 0 49 90\n" +
          "motion disk1 111 340 50 20 30 0 49 90  121 340 210 20 30 0 49 90\n" +
          "motion disk1 121 340 210 20 30 0 49 90  153 340 210 20 30 0 49 90\n" +
          "motion disk3 121 145 240 110 30 11 45 175  131 145 50 110 30 11 45 175\n" +
          "motion disk3 131 145 50 110 30 11 45 175  132 145 50 110 30 11 45 175\n" +
          "motion disk3 132 145 50 110 30 11 45 175  142 445 50 110 30 11 45 175\n" +
          "motion disk3 142 445 50 110 30 11 45 175  143 445 50 110 30 11 45 175\n" +
          "motion disk3 143 445 50 110 30 11 45 175  153 445 240 110 30 11 45 175\n" +
          "motion disk1 153 340 210 20 30 0 49 90  163 340 50 20 30 0 49 90\n" +
          "motion disk3 153 445 240 110 30 11 45 175  161 445 240 110 30 0 255 0\n" +
          "motion disk3 161 445 240 110 30 0 255 0  302 445 240 110 30 0 255 0\n" +
          "motion disk1 163 340 50 20 30 0 49 90  164 340 50 20 30 0 49 90\n" +
          "motion disk1 164 340 50 20 30 0 49 90  174 190 50 20 30 0 49 90\n" +
          "motion disk1 174 190 50 20 30 0 49 90  175 190 50 20 30 0 49 90\n" +
          "motion disk1 175 190 50 20 30 0 49 90  185 190 240 20 30 0 49 90\n" +
          "motion disk1 185 190 240 20 30 0 49 90  217 190 240 20 30 0 49 90\n" +
          "motion disk2 185 317 240 65 30 6 247 41  195 317 50 65 30 6 247 41\n" +
          "motion disk2 195 317 50 65 30 6 247 41  196 317 50 65 30 6 247 41\n" +
          "motion disk2 196 317 50 65 30 6 247 41  206 467 50 65 30 6 247 41\n" +
          "motion disk2 206 467 50 65 30 6 247 41  207 467 50 65 30 6 247 41\n" +
          "motion disk2 207 467 50 65 30 6 247 41  217 467 210 65 30 6 247 41\n" +
          "motion disk1 217 190 240 20 30 0 49 90  227 190 50 20 30 0 49 90\n" +
          "motion disk2 217 467 210 65 30 6 247 41  225 467 210 65 30 0 255 0\n" +
          "motion disk2 225 467 210 65 30 0 255 0  302 467 210 65 30 0 255 0\n" +
          "motion disk1 227 190 50 20 30 0 49 90  228 190 50 20 30 0 49 90\n" +
          "motion disk1 228 190 50 20 30 0 49 90  238 490 50 20 30 0 49 90\n" +
          "motion disk1 238 490 50 20 30 0 49 90  239 490 50 20 30 0 49 90\n" +
          "motion disk1 239 490 50 20 30 0 49 90  249 490 180 20 30 0 49 90\n" +
          "motion disk1 249 490 180 20 30 0 49 90  257 490 180 20 30 0 255 0\n" +
          "motion disk1 257 490 180 20 30 0 255 0  302 490 180 20 30 0 255 0\n";

  /**
   * A private helper to test a string input with our builder and model.
   * @param str The string to test.
   */
  private void testString(String str) {
    new AnimationReader().parseFile(new StringReader(str), getBuilder());
  }

  /**
   * Test the animation described by toh-3.txt in assignment 6. We famously failed to parse this
   * before submitting.
   */
  @Test
  public void testTOH3() {
    testString(TOH3TXT);
  }

  /**
   * Test the animation described by smalldemo.txt in assignment 6 works.
   */
  @Test
  public void testSMALLDEMO() {
    testString(SMALLDEMO);
  }
}
