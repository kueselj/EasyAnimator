package cs3500.easyanimator;

import java.io.IOException;

import cs3500.easyanimator.util.BasicApplicationBuilder;
import cs3500.easyanimator.util.IApplicationBuilder;

import javax.swing.JOptionPane;

/**
 * Main class for EasyAnimator. Will take in command line arguments run the
 * EasyAnimator with the desired animation file, view, output, and tickspeed.
 */
public final class Excellence {
  /**
   * Show an error dialog to the user notifying them that we cannot launch the application with the
   * given message.
   * @param message The message to put into the dialog.
   */
  private static void errorOut(String message) {
    JOptionPane.showMessageDialog(null, // No parent component.
            message, "Excellence Command-line Error",
            JOptionPane.ERROR_MESSAGE);
    System.exit(1);
  }

  /**
   * Parse the given parameter and value combination and enter the details into the builder.
   * @param parameter The string parameter we got from the command line.
   * @param value     The associated value for that parameter from the command line.
   * @param builder   The builder to use.
   * @throws IllegalArgumentException Because the builder throws it. We also throw it if the
   *                                  parameter is invalid.
   * @throws IOException              Because the builder throws it.
   */
  private static void parseParameterValue(String parameter,
                                          String value,
                                          IApplicationBuilder builder)
          throws IllegalArgumentException, IOException {
    switch (parameter) {
      case "-in":
        builder.setInput(value);
        break;
      case "-out":
        builder.setOutput(value);
        break;
      case "-speed":
        builder.setSpeed(value);
        break;
      case "-view":
        builder.setView(value);
        break;
      default:
        throw new IllegalArgumentException(String.format("Invalid parameter supplied. %s",
                parameter));
    }
  }

  /**
   * The main method for our application. This will launch the application with the given command
   * line arguments.
   * @param args  The arguments from the command line as an array of strings.
   */
  public static void main(String[] args) {
    if (args.length % 2 != 0) {
      errorOut("There are not an even number of arguments meaning the command line argument pairs " +
              "are malformed.");
    }

    // We spin up our builder.
    IApplicationBuilder builder = new BasicApplicationBuilder();
    // We go parse the parameter value pairs to give to the builder.
    for (int i = 0; i < args.length; i += 2) {
      String parameter = args[i];
      String value = args[i + 1];
      try {
        parseParameterValue(parameter, value, builder);
      } catch (IllegalArgumentException iae) {
        errorOut(iae.getMessage());
      } catch (IOException io) {
        errorOut(io.getMessage());
      }
    }

    // Finally we ask the builder to launch. And we're done!
    try {
      builder.launch();
    } catch (IllegalStateException ise) {
      errorOut(ise.getMessage());
    }
  }
}
