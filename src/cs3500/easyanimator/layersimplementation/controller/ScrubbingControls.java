package cs3500.easyanimator.layersimplementation.controller;

/**
 * Interface defining scrubbing controls a scrubber could use.
 */
public interface ScrubbingControls {

  /**
   * Scrubbing occurred, do something.
   * @param value the value the scrubber is at.
   */
  void scrubbingOccurred(int value);
}
