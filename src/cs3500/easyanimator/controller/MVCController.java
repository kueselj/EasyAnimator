package cs3500.easyanimator.controller;

import cs3500.easyanimator.model.Color;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import cs3500.easyanimator.model.shapes.WidthHeight;
import cs3500.easyanimator.view.IEnhancedVisualView;
import cs3500.easyanimator.view.IVisualView;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

public class MVCController implements IController {

  IAnimatorModel model;
  IEnhancedVisualView view;

  private int tick;
  private long tickRange;
  private Timer timer;

  private boolean shouldLoop;
  private boolean isPaused;

  public MVCController(IAnimatorModel model, IEnhancedVisualView view) {
    this.model = model;
    this.view = view;

    this.shouldLoop = true;
    this.isPaused = false;

    this.tick = 0;
    this.tickRange = this.model.getMaxTick();

    this.timer = new Timer(30, e -> this.refresh());
    this.addPlayBackListeners();
    this.addSelectShapeListener();
    this.addSelectTickListener();
    this.addSaveKeyFrameListener();

    //get the correctSize for your view.
    WidthHeight wH = this.model.getCanvasSize();
    this.view.setViewSize(wH);

    //setTheAvaliableShapes/
    this.view.setAvailableShapes(this.model.getShapeNames());


  }

  @Override
  public void go() {

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

  @Override
  public void addKeyFrame(String id, IShape state, int tick) {
    this.model.addKeyframe(id, state, tick);
    //reset tickRange as it may have changed now.
    this.tickRange = this.model.getMaxTick();
  }

  @Override
  public void removeKeyFrame(String id, int tick) {
    this.model.removeKeyframe(id, tick);
    //reset tickRange as it may have changed now.
    this.tickRange = this.model.getMaxTick();

  }

  private void addPlayBackListeners(){
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

  /**
   * Listener for the select Shape combo box.
   */
  private void addSelectShapeListener() {
    view.addSelectShapeActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String shpSelected = view.getShapeSelected();
        List<Integer> availTicksInt = model.getShapeKeyframeTicks(shpSelected);
        List<String> availTicksString = availTicksInt.stream().map(s -> String.valueOf(s))
                .collect(Collectors.toList());
        availTicksString.add("New KeyFrame");
        view.setAvailableShapeTicks(availTicksString);
      }
    });
  }

  /**
   * Listener for the selectTick combo box.
   */
  private void addSelectTickListener() {
    view.addSelectTickActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String tickSelected = view.getTickSelected();
        if (tickSelected == "New KeyFrame") {
          //doNothing, do not fill out the textFields;
          List<String> fields = Arrays.asList("","","","","","","","");
          view.setTextFields(fields);
        }
        else {
          int tick = Integer.parseInt(tickSelected);
          List<String> fields = new ArrayList<String>();
          SortedMap<Integer, IShape> keyFrames = model.getKeyframes(view.getShapeSelected());
          IShape keyFrameShape = keyFrames.get(tick);
          fields.add(tickSelected);
          fields.add(Integer.toString(keyFrameShape.getPosition().getX()));
          fields.add(Integer.toString(keyFrameShape.getPosition().getY()));
          fields.add(Integer.toString(keyFrameShape.getSize().getWidth()));
          fields.add(Integer.toString(keyFrameShape.getSize().getWidth()));
          fields.add(Integer.toString(keyFrameShape.getColor().getRed()));
          fields.add(Integer.toString(keyFrameShape.getColor().getGreen()));
          fields.add(Integer.toString(keyFrameShape.getColor().getBlue()));
          view.setTextFields(fields);
        }
      }
    });
  }

  /**
   * Listener for saving a keyFrame.
   */
  private void addSaveKeyFrameListener() {
    view.addSaveKeyFrameActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        List<String> fields = view.getTextFields();
        List<Integer> fieldsAsInt = new ArrayList<Integer>();

        //loop through the fields converting them to integers.
        for (String s: fields) {
          try {
            int sInt = Integer.parseInt(s);
            fieldsAsInt.add(sInt);

          } catch (NumberFormatException ex) {
            //TODO throw an error in the view probably.
          }
        }

        //TODO this section is terrible, use a visitor instead. Implemented for simplicity for now.

        SortedMap<Integer, IShape> keyFrames = model.getKeyframes(view.getShapeSelected());
        if (keyFrames.get(fieldsAsInt.get(0)) instanceof Rectangle) {
          Point p = new Point(fieldsAsInt.get(1),fieldsAsInt.get(2));
          WidthHeight wh = new WidthHeight(fieldsAsInt.get(3), fieldsAsInt.get(4));
          Color color = new Color(fieldsAsInt.get(5), fieldsAsInt.get(6), fieldsAsInt.get(7));
          IShape rectToAdd = new Rectangle(wh, p, color);
          model.addKeyframe(view.getShapeSelected(), rectToAdd, fieldsAsInt.get(0));
        }

        else if (keyFrames.get(fieldsAsInt.get(0)) instanceof Rectangle) {
          Point p = new Point(fieldsAsInt.get(1),fieldsAsInt.get(2));
          WidthHeight wh = new WidthHeight(fieldsAsInt.get(3), fieldsAsInt.get(4));
          Color color = new Color(fieldsAsInt.get(5), fieldsAsInt.get(6), fieldsAsInt.get(7));
          IShape ovalToAdd = new Oval(wh, p, color);
          model.addKeyframe(view.getShapeSelected(), ovalToAdd, fieldsAsInt.get(0));
        }


      }
    });
  }


}
