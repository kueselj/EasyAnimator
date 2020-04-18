package cs3500.easyanimator.layersimplementation.controller;

import cs3500.easyanimator.layersimplementation.view.ILayerView;
import cs3500.easyanimator.model.IAnimatorModel;

import javax.swing.*;

/**
 * Controller specifically for the layer implementation of a model.
 */
public class LayerMVCController implements ILayerMVCController {

  private Timer timer;
  private int tick;
  private double speed;
  private boolean looping;

  //TODO should really take in a layer implementation of it, this should be changed I would suggest
  //TODO adding an extra interface that extends IAnimatorModel that the new layer model implements.
  //TODO that new interface can define all new methods we need.
  private IAnimatorModel model;
  private ILayerView view;

  /**
   * Basic constructor that takes in a model and a view for the controller to use.
   * @param model
   * @param view
   */
  public LayerMVCController(IAnimatorModel model, ILayerView view) {
    this.model = model;
    this.view = view;
    this.tick = 0;
    this.looping = false;
    this.speed = -1.0;

  }

  @Override
  public void refresh() {
    //TODO check if the tick is too high and needs to be reset to zero.
    //TODO stop if looping is not on.
    //TODO grab all shapes from model at given tick and pass them to the animation panel.
    //TODO refresh the panel.
  }

  @Override
  public void setSpeed(double tps) {
    this.speed = Math.max(tps, 1);
    this.timer.setDelay((int) Math.max(1, (1.0 / speed * 1000.0)));
  }

  @Override
  public void start() {

    if (this.speed == -1.0) {
      throw new IllegalArgumentException("Can't start if speed has not been set.");
    }
    this.timer = new Timer((int)this.speed, e -> this.refresh());
  }
}
