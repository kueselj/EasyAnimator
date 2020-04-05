package cs3500.easyanimator.util;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * An IApplicationBuilder builds the starting parameters for launching the application using
 * different parameters as they come in. We do everything except handle the exceptions as they come
 * in. We toss that over to who ever has started the builder up.
 */
public interface IApplicationBuilder {
  /**
   * Set the input file for the given application as a string path.
   * @param pathname The pathname to use when finding the input file.
   * @throws FileNotFoundException  If the specified path does not lead to a readable file.
   */
  void setInput(String pathname) throws FileNotFoundException;

  /**
   * Set the output for the given application as.
   * @param pathname  The pathname of the file we should write to with this application.
   */
  void setOutput(String pathname) throws IOException;

  /**
   * Set the view for the given application with some string.
   * @param viewType  The name of the view to use. See the factory for what strings are supported.
   */
  void setView(String viewType);

  /**
   * Set the speed for the given animation view (if applicable).
   * @param speed The string of the speed, this should be of a number greater than zero.
   * @throws IllegalArgumentException  If the given speed is invalid.
   */
  void setSpeed(String speed) throws IllegalArgumentException;

  /**
   * Launch the application with the parameters supplied.
   *
   * @throws IllegalStateException If certain parameters were needed but not supplied.
   */
  void launch() throws IllegalStateException, IllegalArgumentException;
}
