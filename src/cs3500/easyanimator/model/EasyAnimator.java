package cs3500.easyanimator.model;

import cs3500.easyanimator.model.motions.IMotion;
import cs3500.easyanimator.model.shapes.IShape;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* An EasyAnimator is an implementation of the animator model that works via maps.
*/
public class EasyAnimator implements IAnimatorModel {

  HashMap<String, IShape> shapes;
  // The list of motions we associate with will have the invariant that the end state of the
  // previous motion will match the start state of the next motion.
  // Additionally none of the motions will clash in tick range.
  // We have another invariant here that the keysets for the shapes map and the motions map should
  // match.
  HashMap<String, List<IMotion>> motions;

  /**
   * Basic constructor for an EasyAnimator, just gives sets both fields to empty HashMaps.
   */
  public EasyAnimator() {
    this.shapes = new HashMap<String, IShape>();
    this.motions = new HashMap<String, List<IMotion>>();
  }

  @Override
  public void addShape(String id, IShape shape) {
    if (id == null || shape == null) {
      throw new IllegalArgumentException("Attempted to addShape with uninitialized parameters.");
    }

    // We add the shape. And watch to see if we replaced anything.
    IShape previous = this.shapes.put(id, shape);
    if (previous == null) {
      // We will need to add a blank list to motions.
      this.motions.put(id, new ArrayList<>());
    } else {
      // Do nothing. We preserve the motions.
    }
  }

  @Override
  public void removeShape(String id) {
    if (id == null) {
      throw new IllegalArgumentException("Attempted to remove a shape with an uninitialized id.");
    }

    if (this.shapes.containsKey(id)) {
      // If there was a matching key.
      this.shapes.remove(id);
      // We also need to remove any motions, this should not throw an exception.
      this.motions.remove(id);
    } else {
      // If there was no matching key.
      throw new IllegalArgumentException("Attempted to removeShape with an id that doesn't match.");
    }
  }

  @Override
  public Map<String, IShape> getShapes() {
    return this.shapes;
  }

  /**
   * A private helper that checks if the given motion when added would cause any conflicts of time.
   * @param id      The id of the shape being animated.
   * @param motion  The motion that would be added.
   * @return  A boolean indicating if there is a time conflict (true) or not (false).
   */
  private boolean conflictingTime(String id, IMotion motion) {
    return this.motions.get(id)
            .stream()
            .anyMatch(otherMotion ->
                    (motion.getStartTime() >= otherMotion.getStartTime() &&
                     otherMotion.getEndTime() >= motion.getStartTime()) ||
                    (motion.getStartTime() <= otherMotion.getStartTime() &&
                     otherMotion.getStartTime() >= motion.getEndTime()));
  }

  /**
   * Seeks backward through the list of motions for the given id for the next previous motion to
   * the provided one (doesn't have to be in the list).
   * @param id      The id of the shape to seek backwards in the list of.
   * @param motion  The motion to seek from.
   * @return        The IMotion "behind" this motion (in terms of end time) or empty.
   */
  private Optional<IMotion> seekBackward(String id, IMotion motion) {
    return this.motions.get(id).stream()
            // We only want the ones behind it.
            .filter(otherMotion -> otherMotion.getEndTime() < motion.getEndTime())
            // And we want the nearest one.
            .max(Comparator.comparingInt(IMotion::getEndTime));
  }

  /**
   * Seeks forward through the list of motions for the given id for the next motion to
   * the provided one (doesn't have to be in the list).
   * @param id      The id of the shape to seek forwards in the list of.
   * @param motion  The motion to seek from.
   * @return        The IMotion "in front of" this motion (in terms of end time) or empty.
   */
  private Optional<IMotion> seekForward(String id, IMotion motion) {
    return this.motions.get(id).stream()
            // We only want the ones behind it.
            .filter(otherMotion -> otherMotion.getStartTime() > motion.getEndTime())
            // And we want the nearest one.
            .max(Comparator.comparingInt(IMotion::getStartTime));
  }

  /**
   * Tests if the end state of the first motion matches the start state of the second motion.
   * @param first   The first motion.
   * @param second  The second motion.
   * @return  True if the end state and start state match, false otherwise.
   */
  private boolean endStartStateMatch(IMotion first, IMotion second) {
    return (first.getEndSize().equals(second.getStartSize()) &&
            first.getEndPosition().equals(second.getStartPosition()) &&
            first.getEndColor().equals(second.getStartColor()));
  }

  @Override
  public void addMotion(String id, IMotion motion) {
    if (id == null || motion == null) {
      throw new IllegalArgumentException("Attempted to addMotion with uninitialized parameters.");
    }

    if (this.motions.containsKey(id)) {
      // We then need to check that we now don't violate our two invariants with this motion.

      // The first is easy, do we conflict with any other motions in range of time.
      boolean conflict = conflictingTime(id, motion);
      if (conflict) {
        throw new IllegalArgumentException("Unable to add motion, conflicts in time ranges.");
      }

      // The second is harder,
      // do we conflict with any other motions by not having our states line up?
      Optional<IMotion> previous = seekBackward(id, motion);
      Optional<IMotion> next = seekForward(id, motion);
      if (previous.isPresent()) {
        if (!endStartStateMatch(previous.get(), motion)) {
          throw new IllegalArgumentException("Attempted to add a motion that creates a state" +
                  " mismatch.");
        }
      }
      if (next.isPresent()) {
        if (!endStartStateMatch(motion, next.get())) {
          throw new IllegalArgumentException("Attempted to add a motion that creates a state" +
                  " mismatch.");
        }
      }

      this.motions.get(id).add(motion);
    } else {
      // Motions does not have a key for id.
      throw new IllegalArgumentException("Attempted to addMotion with an invalid unmatched id.");
    }
  }

  @Override
  public void removeMotion(String id, IMotion motion) {
    if (id == null || motion == null) {
      throw new IllegalArgumentException("Attempted to removeMotion with uninitialized " +
              "parameters.");
    }

    if (!motions.containsKey(id)) {
      throw new IllegalArgumentException("Attempted to removeMotion with an id that doesn't match" +
              " to a shape.");
    }

    if (!motions.get("C").contains(motion)) {
      throw new IllegalArgumentException("Attempted to removeMotion with a motion that wasn't " +
              "added to the shape.");
    }

    // We then need to check if removing this motion breaks our invariants.
    Optional<IMotion> previous = seekBackward(id, motion);
    Optional<IMotion> next = seekForward(id, motion);

    if (previous.isPresent() && next.isPresent() &&
            !endStartStateMatch(previous.get(), next.get())) {
      throw new IllegalArgumentException("Attempted to removeMotion that would lead to invalid " +
              "state.");
    }

    motions.remove(motion);
  }

  @Override
  public Map<String, List<IMotion>> getMotions() {
    return this.motions;
  }

}
