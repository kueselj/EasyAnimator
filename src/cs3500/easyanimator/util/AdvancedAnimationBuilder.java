package cs3500.easyanimator.util;

/**
 * An AnimationBuilder is an interface that we need to fill in order to connect instructors
 * animation reader to our model code. It is backwards compatible with AnimationReader assuming
 * defaults like a base layer for shapes without a specified layer and default rotation of 0deg
 * for motions and keyframes without a rotation specified.
 * Finally, we do not support bounds for each individual layer (though that is theoretically
 * possible) in order to simplify input. The bounds set are applied across all layers.
 * Layers are visible by default.
 * @param <Doc> A generic for the interface of our model. Since we need to return it at the end.
 */
public interface AdvancedAnimationBuilder<Doc> extends AnimationBuilder<Doc> {
  /**
   * Adds a new layer to the growing document.
   *
   * @param name The unique name of the layer to be added.
   *             No layer with this name should already exist.
   * @param visibility  If the layer is visible or not.
   * @return This {@link AdvancedAnimationBuilder}
   */
  AdvancedAnimationBuilder<Doc> declareLayer(String name, boolean visibility);

  /**
   * Adds a new shape to the growing document.
   *
   * @param name The unique name of the shape to be added.  
   *             No shape with this name should already exist.
   * @param type The type of shape (e.g. "ellipse", "rectangle") to be added.  
   *             The set of supported shapes is unspecified, but should 
   *             include "ellipse" and "rectangle" as a minimum.
   * @param layer The name of the layer to which this shape should be added to.
   * @return This {@link AdvancedAnimationBuilder}
   */
  AdvancedAnimationBuilder<Doc> declareShape(String name, String type, String layer);

  /**
   * Adds a transformation to the growing document.
   * 
   * @param name The name of the shape (added with {@link AdvancedAnimationBuilder#declareShape})
   * @param t1   The start time of this transformation
   * @param x1   The initial x-position of the shape
   * @param y1   The initial y-position of the shape
   * @param w1   The initial width of the shape
   * @param h1   The initial height of the shape
   * @param r1   The initial red color-value of the shape
   * @param g1   The initial green color-value of the shape
   * @param b1   The initial blue color-value of the shape
   * @param rot1 The initial rotation of the shape in degrees.
   * @param t2   The end time of this transformation
   * @param x2   The final x-position of the shape
   * @param y2   The final y-position of the shape
   * @param w2   The final width of the shape
   * @param h2   The final height of the shape
   * @param r2   The final red color-value of the shape
   * @param g2   The final green color-value of the shape
   * @param b2   The final blue color-value of the shape
   * @param rot2 The final rotation of the shape in degrees.
   * @return This {@link AdvancedAnimationBuilder}
   */
  AdvancedAnimationBuilder<Doc> addMotion(String name,
                                          int t1,
                                          int x1, int y1, int w1, int h1,
                                          int r1, int g1, int b1,
                                          int rot1,
                                          int t2,
                                          int x2, int y2, int w2, int h2,
                                          int r2, int g2, int b2,
                                          int rot2);

  /**
   * Adds an individual keyframe to the growing document.
   * @param name The name of the shape (added with {@link AdvancedAnimationBuilder#declareShape})
   * @param t    The time for this keyframe
   * @param x    The x-position of the shape
   * @param y    The y-position of the shape
   * @param w    The width of the shape
   * @param h    The height of the shape
   * @param r    The red color-value of the shape
   * @param g    The green color-value of the shape
   * @param b    The blue color-value of the shape
   * @param rot  The rotation of the shape.
   * @return This {@link AdvancedAnimationBuilder}
   */
  AdvancedAnimationBuilder<Doc> addKeyframe(String name,
                                            int t, int x, int y, int w, int h, int r, int g, int b,
                                            int rot);
}
