package cs3500.easyanimator.provider.model;

/**
 * An abstract class to represent the different types of animation and their common behaviour.
 */
public abstract class AAnimation {

  protected int startTime;
  protected int tickDuration;
  protected int totalDuration;
  protected AShape target;
  protected String startState;
  protected String endState;

  /**
   * Stops the animation, even if it has not completed yet.
   */
  public void stop() {
    this.tickDuration = 0;
  }

  /**
   * Mutates the target shape, if it should. Decrements the tick duration as well, so that the
   * animation halts when it's supposed to.
   */
  public abstract void tick(int dt);

  /**
   * Determines if the animation should be removed from the list (and cleanup by garbage
   * collection).
   *
   * @return - a Boolean representing if the animation is completed or not.
   */
  public boolean isDone() {
    return this.tickDuration <= 0;
  }

  /**
   * Returns the target of this animation.
   *
   * @return the AShape the animation is modifying.
   */
  public AShape getTarget() {
    return this.target;
  }

  /**
   * Generates a String which represents the start and end state, thereby describing the animation.
   *
   * @return a String representing the animation
   */
  public String getTextualRepresentation() {
    if (!this.isDone()) {
      throw new IllegalStateException("This animation hasn't finished yet.");
    }
    return this.startState + " | " + this.endState;
  }

  /**
   * Generates a String which represents the animation in SVG.
   *
   * @return the animation in SVG
   */
  public abstract String toSVG(int animationSpeed);

  @Override
  public String toString() {
    String[] startString = this.startState.split(" ");
    String[] endString = {""};

    if (this.endState != null) {
      endString = this.endState.split(" ");
    }

    String retString = "motion ";
    for (int i = 0; i < startString.length; i++) {
      if (i != 1 && i != 2) {
        retString += startString[i] + " ";
      }
    }
    for (int i = 0; i < endString.length; i++) {
      if (i != 1 && i != 2) {
        retString += endString[i] + " ";
      }
    }
    return retString;
  }
}
