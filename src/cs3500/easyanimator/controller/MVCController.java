package cs3500.easyanimator.controller;

import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.shapes.WidthHeight;
import cs3500.easyanimator.view.IVisualView;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MVCController implements IController {

  IAnimatorModel model;
  IVisualView view;

  private int tick;
  private long tickRange;
  private Timer timer;

  private boolean shouldLoop;
  private boolean isPaused;

  public MVCController(IAnimatorModel model, IVisualView view) {
    this.model = model;
    this.view = view;

    this.shouldLoop = true;
    this.isPaused = false;

    this.tick = 0;
    this.tickRange = this.model.getMaxTick();

    this.timer = new Timer(30, e -> this.refresh());
    this.addListeners();
  }

  @Override
  public void go() {

    //get the correctSize for your view.
    WidthHeight wH = this.model.getCanvasSize();
    this.view.setViewSize(wH);
    view.makeVisible();
  }

  @Override
  public void refresh() {
    if (this.isPaused) {
      //do nothing
    }

    else if (this.tick == tickRange && shouldLoop) {
      this.tick = 0;
      this.view.setShapes(model.getShapesAtTick(this.tick));
      view.refresh();
    }
    else if (this.tick == tickRange && !shouldLoop){
      //do nothing.
    }
    else {
      this.tick++;
      this.view.setShapes(model.getShapesAtTick(this.tick));
      view.refresh();
    }
  }

  private void addListeners(){
    view.addActionListeners(new ActionListener() {
      public void actionPerformed(ActionEvent start) {
        timer.start();
        isPaused = false;
      }}, new ActionListener() {
      public void actionPerformed(ActionEvent pause) {
        isPaused = true;
      }}, new ActionListener() {
      public void actionPerformed(ActionEvent resume) {
        isPaused = false;
      }}, new ActionListener() {
      public void actionPerformed(ActionEvent restart) {
        tick = 0;
        isPaused = false;
      }}, new ActionListener() {
      public void actionPerformed(ActionEvent toggleLoop) {
        shouldLoop = !shouldLoop;
      }}, new ActionListener() {
      public void actionPerformed(ActionEvent increaseSpeed) {
        if (timer.getDelay() > 1) {
          timer.stop();
          timer = new Timer(timer.getDelay() - 1, e -> refresh());
          timer.start();
        }
        System.out.println(timer.getDelay());


      }}, new ActionListener() {
      public void actionPerformed(ActionEvent decreaseSpeed) {
        timer.stop();
        timer = new Timer(timer.getDelay() + 1, e -> refresh());
        timer.start();
      }});
  }


}
