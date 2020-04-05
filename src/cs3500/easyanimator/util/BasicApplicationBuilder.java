package cs3500.easyanimator.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cs3500.easyanimator.model.IAnimatorModel;
import cs3500.easyanimator.model.IAnimatorModelViewOnly;
import cs3500.easyanimator.view.BasicViewFactory;
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
    AnimationBuilder<IAnimatorModel> modelBuilder = new AnimatorModelBuilder();
    new AnimationReader().parseFile(input, modelBuilder);
    IAnimatorModel model = modelBuilder.build();
    // We can give that model over to the view.
    // We need to cast down because it takes the readonly version.
    view.setModel((IAnimatorModelViewOnly) model);
    view.setSpeed(speed);
    // I think we are ready to launch.
    view.makeVisible();
    // If we opened a file writer we have to close it to get those writes out.
    if (outputWriter != null) {
      try {
        outputWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
