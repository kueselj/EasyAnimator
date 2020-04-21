package cs3500.easyanimator.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cs3500.easyanimator.controller.MVCController;
import cs3500.easyanimator.layersimplementation.controller.ILayerMVCController;
import cs3500.easyanimator.layersimplementation.controller.LayerMVCController;
import cs3500.easyanimator.layersimplementation.view.ILayerView;
import cs3500.easyanimator.layersimplementation.view.LayerView;
import cs3500.easyanimator.model.EasyAnimator;
import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.ILayeredAnimatorModel;
import cs3500.easyanimator.model.LayeredAnimatorModel;
import cs3500.easyanimator.model.Point;
import cs3500.easyanimator.model.layers.BasicLayer;
import cs3500.easyanimator.model.layers.ILayer;
import cs3500.easyanimator.model.shapes.WidthHeight;
import cs3500.easyanimator.provider.controller.AnimatorController;
import cs3500.easyanimator.provider.model.AnimatorModel;
import cs3500.easyanimator.view.BasicViewFactory;
import cs3500.easyanimator.view.EditorSwingView;
import cs3500.easyanimator.view.IAnimatorView;

/**
 * An implementation of the application builder that doesn't do anything complicated. It actually
 * throws the IllegalArgumentException for setView during the launch call because we need to know
 * the output when going to the factory.
 */
public class BasicApplicationBuilder implements IApplicationBuilder {
  private FileReader inputReader; // Necessary to close.
  private Readable input;
  private FileWriter outputWriter; // This is necessary to close it. I'd love to not keep it.
  private Appendable output = System.out; // These parameters have defaults.
  private double speed = 1; // These parameters have defaults.
  private String viewType;

  @Override
  public void setInput(String pathname) throws FileNotFoundException {
    this.inputReader = new FileReader(pathname);
    this.input = this.inputReader;
  }

  @Override
  public void setOutput(String pathname) throws IOException {
    if (this.outputWriter != null) {
      this.outputWriter.close(); // We don't forget to close the writer we opened here.
    }
    this.outputWriter = new FileWriter(pathname, false);
    this.output = this.outputWriter; // This does the needed cast to appendable.
  }

  @Override
  public void setView(String viewType) {
    this.viewType = viewType;
    // We do interpretation of this argument during launch since we need a few details to plug into
    // our factory.
  }

  @Override
  public void setSpeed(String speed) throws IllegalArgumentException {
    double s = Double.parseDouble(speed); // We let the NumberFormatException get tossed here.
    if (s <= 0) {
      throw new IllegalArgumentException("Provided an invalid speed argument. " +
              "Cannot be non-positive.");
    }
    this.speed = s;
  }

  @Override
  public void launch() throws IllegalStateException {
    // We need the output for the text views. So it is supplied to the factory.
    // It's simply unused for visual views.
    if (viewType == null || (!viewType.equals("edit") && !viewType.equals("oldedit") && input == null)) {
      // We hotfix in a change to allow you to edit a fresh model.
      throw new IllegalStateException("Failed to provide both a view type and an input " +
              "file.");
    }

    // A rule to hot fix in using a model that's layered.
    if (viewType.equals("edit")) {
      ILayeredAnimatorModel model;
      if (input == null) {
        model = new LayeredAnimatorModel();
        ILayer base = new BasicLayer("Base", true, new EasyAnimator());
        base.getModel().setCanvas(new Point(0, 0), new WidthHeight(800, 600));
        model.addLayer(base);
      } else {
        AdvancedAnimationBuilder<ILayeredAnimatorModel> builder =
                new LayeredAnimatorModelBuilder("Base");
        new AdvancedAnimationReader().parseFile(input, builder);
        model = builder.build();

      }
      int canvasX = model.getCanvasSize().getWidth();
      int canvasY = model.getCanvasSize().getHeight();
      ILayerView view = new LayerView(canvasX, canvasY);
      LayerMVCController controller = new LayerMVCController(model, view);
      controller.setSpeed(speed);
      controller.go();
      // Skip the rest of the code that's here.
      return;
    }

    IAnimatorModel model;
    if (input == null) {
      // If we start without an input, for the edit view, then we wish to spin up a new model.
      model = new EasyAnimator();
      model.setCanvas(new Point(0, 0), new WidthHeight(800, 600));
    } else {
      // Let's prepare the model wherever its going.
      AnimationBuilder<IAnimatorModel> modelBuilder = new EasyAnimatorModelBuilder();
      new AnimationReader().parseFile(input, modelBuilder);
      model = modelBuilder.build();
    }

    if (viewType.equals("oldedit")) {
      // We hotfix this change in to support an editor.
      MVCController controller = new MVCController(model);
      controller.setView(new EditorSwingView(controller), this.speed);
      controller.start();
    } else if (viewType.equals("provider")) {
      int canvasX = model.getCanvasSize().getWidth();
      int canvasY = model.getCanvasSize().getHeight();
      AnimatorModel adaptedModel =
              //TODO just put dummy values here for now.
              new AnimatorModel(model, canvasX, canvasY, 0, 0);
      AnimatorController controller = new AnimatorController(adaptedModel, this.speed);

      return; // Quit out early.
    } else {
      IAnimatorView view;
      try {
        view = new BasicViewFactory().getView(this.viewType, this.output);
      } catch (IllegalArgumentException iae) {
        throw new IllegalStateException("Unable to use the set view type " + this.viewType);
      }

      // We can give that model over to the view.
      view.setModel(model);
      view.setSpeed(speed);
      // I think we are ready to launch.
      view.makeVisible();
    }

    // If we opened a file writer we have to close it to get those writes out.
    if (outputWriter != null) {
      try {
        outputWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (inputReader != null) {
      try {
        inputReader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
