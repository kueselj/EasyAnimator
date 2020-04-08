package cs3500.easyanimator.view;

import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * JPanel specific to an animation, has a list of shapes that will be drawn onto the panel.
 */
public class AnimationPanel extends JPanel {
  List<IShape> shapes;

  /**
   * Basic Constructor for the Animation Panel.
   */
  public AnimationPanel() {
    super();
    this.setBackground(Color.WHITE);
    this.shapes = new ArrayList<>(); // By default the pane is blank.
  }

  /**
   * Sets the panel to draw the given shapes.
   * @param shapes the shapes to set.
   */
  public void setShapes(List<IShape> shapes) {
    this.shapes.clear();
    this.shapes.addAll(shapes);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.BLACK);
    // We are already transformed by get shapes at tick.
    IShapeVisitor<Void> drawingVisitor = new DrawShapeVisitor(g2d);
    for (IShape s : this.shapes) {
      s.accept(drawingVisitor);
    }
  }

  /**
   * Custom visitor to draw the desired shape on the given Graphics2D.
   */
  private class DrawShapeVisitor implements IShapeVisitor<Void> {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    Graphics2D g2d;

    /**
     * Constructor giving the visitor a graphics 2D to draw onto.
     * @param g2d the graphics to use.
     */
    public DrawShapeVisitor(Graphics2D g2d) {
      this.g2d = g2d;
    }

    /**
     * Prepares parameters in the visitor with extracting all the needed properties from the given
     * shape.
     * @param shape The generic shape to use.
     */
    private void prepare(IShape shape) {
      this.x = shape.getPosition().getX();
      this.y = shape.getPosition().getY();
      this.width = shape.getSize().getWidth();
      this.height = shape.getSize().getHeight();
      this.color = new Color(shape.getColor().getRed(),
              shape.getColor().getGreen(),
              shape.getColor().getBlue());
    }

    @Override
    public Void applyToRectangle(Rectangle rect) {
      prepare(rect);
      g2d.setColor(color);
      g2d.fillRect(x, y, width, height);
      return null;
    }

    @Override
    public Void applyToOval(Oval oval) {
      prepare(oval);
      g2d.setColor(color);
      g2d.fillOval(x, y, width, height);
      return null;
    }
  }
}
