package cs3500.easyanimator.model.layers;

import cs3500.easyanimator.model.IAnimatorModel;

/**
 * A BasicLayer implements ILayer in the smallest amount of code as possible. It basically holds the
 * fields and does nothing interesting with them.
 */
public class BasicLayer implements ILayer {
  private final String name;
  private final boolean visibility;
  private final IAnimatorModel model;

  /**
   * Create a new BasicLayer with the given name, visibility, and model.
   */
  public BasicLayer(String name, boolean visibility, IAnimatorModel model) {
    this.name = name;
    this.visibility = visibility;
    this.model = model; // We don't copy, because we are open to this model changing.
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean getVisibility() {
    return this.visibility;
  }

  @Override
  public ILayer setVisibility(boolean visibility) {
    return new BasicLayer(this.name, visibility, this.model);
  }

  @Override
  public ILayer setName(String name) {
    return new BasicLayer(name, this.visibility, this.model);
  }

  @Override
  public IAnimatorModel getModel() {
    return this.model;
  }
}
