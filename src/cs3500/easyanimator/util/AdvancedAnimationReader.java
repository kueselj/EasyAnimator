package cs3500.easyanimator.util;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * A helper to read animation data and construct an animation from it.
 */
public class AdvancedAnimationReader {
  /**
   * A factory for producing new animations, given a source of shapes and a
   * builder for constructing animations.
   *
   * <p>
   * The input file format consists of two types of lines:
   * <ul>
   * <li>Layer lines: the keyword  "layer" followed by one identifier of the unique layer name.</li>
   * <li>Shape lines: the keyword "shape" followed by two identifiers (i.e.
   * alphabetic strings with no spaces), giving the unique name of the shape,
   * and the type of shape it is. Or alternatively three indentifiers,
   * the last being the name of the layer this shape belongs to.</li>
   * <li>Motion lines: the keyword "motion" followed by an identifier giving the name
   * of the shape to move, and 16 integers giving the initial and final conditions of the motion:
   * eight numbers giving the time, the x and y coordinates, the width and height,
   * and the red, green and blue color values at the start of the motion; followed by 
   * eight numbers for the end of the motion.  See {@link AnimationBuilder#addMotion}
   * OR ten numbers, with extra fields for rotation, see README.</li>
   * </ul>
   * </p>
   *
   * @param readable The source of data for the animation
   * @param builder  A builder for helping to construct a new animation
   * @param <Doc>    The main model interface type describing animations
   * @return
   */
  public static <Doc> Doc parseFile(Readable readable, AdvancedAnimationBuilder<Doc> builder) {
    Objects.requireNonNull(readable, "Must have non-null readable source");
    Objects.requireNonNull(builder, "Must provide a non-null AnimationBuilder");
    Scanner s = new Scanner(readable);
    // Split at whitespace, and ignore # comment lines
    s.useDelimiter(Pattern.compile("(\\p{Space}+|#.*)+")); 
    while (s.hasNextLine()) {
      String line = s.nextLine();
      Scanner lineScanner = new Scanner(line.trim());
      if (lineScanner.hasNext() == false) {
        continue;
      }
      String word = lineScanner.next();
      switch (word) {
        case "canvas":
          readCanvas(lineScanner, builder);
          break;
        case "layer":
          readLayer(lineScanner, builder);
          break;
        case "shape":
          readShape(lineScanner, builder);
          break;
        case "motion":
          readMotion(lineScanner, builder);
          break;
        case "#": // This was supposed to be caught by their scanner, but I guess I messed it up with lines.
          continue;
        // I'm scared to do keyframes away from their code.
        default:
          throw new IllegalStateException("Unexpected keyword: " + word + lineScanner.nextLine());
      }
    }
    return builder.build();
  }

  private static <Doc> void readCanvas(Scanner s, AdvancedAnimationBuilder<Doc> builder) {
    int[] vals = new int[4];
    String[] fieldNames = {"left", "top", "width", "height"};
    for (int i = 0; i < 4; i++) {
      vals[i] = getInt(s, "Canvas", fieldNames[i]);
    }
    builder.setBounds(vals[0], vals[1], vals[2], vals[3]);
  }

  private static <Doc> void readShape(Scanner s, AdvancedAnimationBuilder<Doc> builder) {
    // We have to limit ourselves to scanning only a line
    // since we don't know how many tokens there are.

    String name;
    String type;
    String layer;
    if (s.hasNext()) {
      name = s.next();
    } else {
      throw new IllegalStateException("Shape: Expected a name, but no more input available");
    }
    if (s.hasNext()) {
      type = s.next();
    } else {
      throw new IllegalStateException("Shape: Expected a type, but no more input available");
    }
    if (s.hasNext()) {
      layer = s.next();
      builder.declareShape(name, type, layer);
    } else {
      builder.declareShape(name, type);
    }
  }

  private static <Doc> void readMotion(Scanner s, AdvancedAnimationBuilder<Doc> builder) {
    String[] fieldNames = new String[]{
      "initial time",
      "initial x-coordinate", "initial y-coordinate",
      "initial width", "initial height",
      "initial red value", "initial green value", "initial blue value",
      "final time",
      "final x-coordinate", "final y-coordinate",
      "final width", "final height",
      "final red value", "final green value", "final blue value",
    };

    String[] fieldNamesAlternative = new String[]{
      "initial time",
      "initial x-coordinate", "initial y-coordinate",
      "initial width", "initial height",
      "initial red value", "initial green value", "initial blue value",
      "initial rotation",
      "final time",
      "final x-coordinate", "final y-coordinate",
      "final width", "final height",
      "final red value", "final green value", "final blue value",
      "final rotation",
    };

    int[] vals = new int[18];
    String name;
    if (s.hasNext()) {
      name = s.next();
    } else {
      throw new IllegalStateException("Motion: Expected a shape name, but no more input available");
    }

    boolean rot = false;
    for (int i = 0; i < 18; i++) {
      if (i == 16 && s.hasNext()) {
        rot = true;
      } else if (i == 16) {
        break; // We leave the last 2 null. We probably didn't have more.
      }
      if (rot) {
        vals[i] = getInt(s, "Motion", fieldNamesAlternative[i]);
      } else {
        vals[i] = getInt(s, "Motion", fieldNames[i]);
      }
    }
    if (!rot) {
      builder.addMotion(name,
              vals[0], vals[1], vals[2], vals[3], vals[4], vals[5], vals[6], vals[7],
              vals[8], vals[9], vals[10], vals[11], vals[12], vals[13], vals[14], vals[15]);
    } else {
      // If there was rotation.
      builder.addMotion(name,
              vals[0],
              vals[1], vals[2], vals[3], vals[4], vals[5], vals[6], vals[7], vals[8],
              vals[9],
              vals[10], vals[11], vals[12], vals[13], vals[14], vals[15], vals[16], vals[17]);
    }
  }

  private static <Doc> void readLayer(Scanner s, AdvancedAnimationBuilder<Doc> builder) {
    String name;
    boolean visibility;
    if (s.hasNext()) {
      name = s.next();
    } else {
      throw new IllegalStateException("Layer: Expected a name, but no more input available");
    }
    if (s.hasNextBoolean()) {
      visibility = s.nextBoolean();
    } else if (s.hasNext()) {
      throw new IllegalStateException("Layer: Expected a boolean visibility, got string input.");
    } else {
      throw new IllegalStateException("Layer: Expected a visibility, but no more input available.");
    }
    builder.declareLayer(name, visibility);
  }
  
  private static int getInt(Scanner s, String label, String fieldName) {
    if (s.hasNextInt()) {
      return s.nextInt();
    } else if (s.hasNext()) {
      throw new IllegalStateException(
              String.format("%s: expected integer for %s, got: %s", label, fieldName, s.next()));
    } else {
      throw new IllegalStateException(
              String.format("%s: expected integer for %s, but no more input available",
                            label, fieldName));
    }
  }

}
