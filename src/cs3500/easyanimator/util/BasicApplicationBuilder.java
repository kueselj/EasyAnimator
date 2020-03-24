package cs3500.easyanimator.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.IAnimatorModelViewOnly;
import cs3500.easyanimator.view.BasicViewFactory;
import cs3500.easyanimator.view.IAnimatorView;
import cs3500.easyanimator.view.IVisualView;

/**
 * An implementation of the application builder that doesn't do anything complicated. It actually
 * throws the IllegalArgumentException for setView during the launch call because we need to know
 * the output when going to the factory.
 */
public class BasicApplicationBuilder implements IApplicationBuilder {
  private Readable input;
  private Appendable output = System.out; // These parameters have defaults.
  private double speed = 1; // These parameters have defaults.
  private String viewType;

  @Override
  public void setInput(String pathname) throws FileNotFoundException {
    this.input = new FileReader(pathname);
  }

  @Override
  public void setOutput(String pathname) throws IOException {
    this.output = new FileWriter(pathname);
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
    if (viewType == null || input == null) {
      throw new IllegalStateException("Failed to provide both a view type and an input " +
              "file.");
    }

    IAnimatorView view;
    try {
      view = new BasicViewFactory().getView(this.viewType, this.output);
    } catch (IllegalArgumentException iae) {
      throw new IllegalStateException("Unable to use the set view type " + this.viewType);
    }

    // Now let's make the model.
    AnimationBuilder<IAnimatorModel> modelBuilder = new EasyAnimatorBuilder();
    new AnimationReader().parseFile(input, modelBuilder);
    IAnimatorModel model = modelBuilder.build();
    // We can give that model over to the view.
    // We need to cast down because it takes the readonly version.
    view.setModel((IAnimatorModelViewOnly) model);
    // I think we are ready to launch.
    view.makeVisible();

    // If this is a visual view then we need to use the speed.
    if (view instanceof IVisualView) {
      IVisualView visualView = (IVisualView) view;
      visualView.makeVisible();
      // We do the action listener inline to not pollute the public methods of the builder.
      new Timer((int) (1.0 / speed * 1000), // We divide 1s by the tps speed. Then convert to ms.
              e -> visualView.refresh());
    }
  }
}
