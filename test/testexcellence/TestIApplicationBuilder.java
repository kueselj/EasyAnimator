package testexcellence;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import cs3500.easyanimator.util.IApplicationBuilder;

import static org.junit.Assert.fail;

/**
 * A collection of tests for the class that launches the application. We mostly test our builder
 * here. We don't test how command line arguments are parsed as part of @635. We are disallowed a
 * library but to the benefit of not having to test that whole bag of complications.
 */
public abstract class TestIApplicationBuilder {
  // This first section is dedicated to testing command line parsing correct behaviors.

  /**
   * Provide the concrete implementation of the application builder.
   * @return  A fresh implementation instance.
   */
  abstract IApplicationBuilder getInstance();

  /**
   * Attempt to create a temporary input/output, check if creation was successful with
   * IOExceptions.
   *
   * @return Returns a string location of our temporary file.
   * @throws IOException If we were unable to create the temporary in or out file locations.
   */
  public String createTemporaryLocation(String name) throws IOException {
    File temporaryInput = File.createTempFile(name, "txt");
    temporaryInput.deleteOnExit();
    return temporaryInput.getAbsolutePath();
  }

  /**
   * Tests that we can survive a call to parse with increasingly smaller argument sizes, until we
   * reach the minimum arguments needed (at the moment these are -in and -view.
   */
  @Test
  public void testMinimumArguments() {
    String in, out;
    try {
      in = createTemporaryLocation("input");
      out = createTemporaryLocation("out");
    } catch (IOException ie) {
      ie.printStackTrace();
      fail("Unable to create temporary files to use.");
      return; // We need to abort testing at this point.
    }

    IApplicationBuilder builder = getInstance();
    try {
      builder.setInput(in);
      builder.setOutput(out);
      builder.setView("text");
      builder.launch();
    } catch (IllegalArgumentException iae) {
      iae.printStackTrace();
      fail("Did not expect to fail with only input, output, and view arguments.");
    } catch (IOException io) {
      fail("Did not expect to fail with an io exception with only input, output, and view args.");
    }

    builder = getInstance(); // Fresh instance.
    try {
      builder.setInput(in);
      builder.setView("text");
      builder.launch();
    } catch (IllegalArgumentException iae) {
      iae.printStackTrace();
      fail("Did not expect to fail with only input, and view arguments (the minimum).");
    } catch (IOException io) {
      io.printStackTrace();
      fail("Did not expect to fail with only input, and view arguments in io.");
    }
  }

  // Now let's try to mess things up with exceptions.

  /**
   * Tests what happens when the wanted view is not supplied. We expect an IllegalArgumentException.
   */
  @Test
  public void testNoView() {
    String in;
    try {
      in = createTemporaryLocation("input");
    } catch (IOException ie) {
      ie.printStackTrace();
      fail("Unable to create temporary input to use.");
      return; // We need to abort testing at this point.
    }

    IApplicationBuilder builder = getInstance(); // Fresh instance.
    try {
      builder.setInput(in);
      builder.launch();
      fail("Expected to throw an IllegalArgumentException because there is no view argument.");
    } catch (IOException io) {
      fail("Failed to provide view argument, there should have not been any io errors.");
    } catch (IllegalStateException ise) {
      // Nice. We wanted to be here.
    }
  }

  /**
   * Tests what happens when the wanted input file is not supplied. We expect an
   * IllegalArgumentException.
   */
  @Test
  public void testNoInput() {
    IApplicationBuilder builder = getInstance();

    try {
      builder.setView("text");
      builder.launch();
      fail("Expected to throw an IllegalArgumentException because there was no input argument.");
    } catch (IllegalStateException ise) {
      // Nice we wanted to be here.
    }
  }

  /**
   * Tests what happens when the pair of arguments is not valid. Specifically when the parameter
   * name is correct (-in, -out, -view, or -speed).
   */
  @Test
  public void testWrongTypeArguments() {
    String in;
    try {
      in = createTemporaryLocation("input");
    } catch (IOException ie) {
      ie.printStackTrace();
      fail("Unable to create temporary input to use.");
      return; // We need to abort testing at this point.
    }

    IApplicationBuilder builder = getInstance(); // Fresh instance.
    try {
      builder.setInput("/tmp/nowaythisexists.txt");
      builder.setView("text");
      builder.launch();
      fail("Expected to throw an IOException because we attempted to launch the program with a " +
              "bad input file.");
    } catch (IOException io) {
      // Nice. We wanted to be here.
    }

    try {
      builder.setInput(in); // Correct.
      builder.setView("There's no way this view exists!"); // Invalid.
      builder.launch();
      fail("Expected to fail when provided a view that is nonexistent.");
    } catch (IllegalStateException iae) {
      // Nice. We wanted to be here.
    } catch (IOException io) {
      fail("Expected to fail with an IllegalArgumentException when giving a nonexistent view. " +
              "Not an IOException.");
    }

    try {
      builder.setInput(in);
      builder.setView("text");
      builder.setSpeed("notanumber");
      fail("Expected to fail when providing notanumber as the speed.");
    } catch (IllegalArgumentException iae) {
      // Nice. We wanted to be here.
    } catch (IOException io) {
      fail("Expected to fail with NumberFormatException when providing notanumber as the speed. " +
              "Not an IOException.");
    }
  }
}
