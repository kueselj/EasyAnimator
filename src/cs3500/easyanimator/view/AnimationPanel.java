package cs3500.easyanimator.view;

import cs3500.easyanimator.model.shapes.IShape;
import cs3500.easyanimator.model.shapes.IShapeVisitor;
import cs3500.easyanimator.model.shapes.Oval;
import cs3500.easyanimator.model.shapes.Rectangle;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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
    this.shapes = new ArrayList<IShape>();
  }

  /**
   * Sets the panel to contain the given shapes.
   * @param shapes the shapes to set.
   */
  public void setShapes(List<IShape> shapes) {
    this.shapes.clear();
    this.shapes.addAll(shapes);
  }

  @Override
  protected void paintComponent(Graphics g) {
    //never forget to call super.paintComponent!
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor(Color.BLACK);

    /*
    the origin of the panel is top left. In order
    to make the origin bottom left, we must "flip" the
    y coordinates so that y = height - y

    We do that by using an affine transform. The flip
    can be specified as scaling y by -1 and then
    translating by height.
     */

    AffineTransform originalTransform = g2d.getTransform();

    //the order of transforms is bottom-to-top
    //so as a result of the two lines below,
    //each y will first be scaled, and then translated
    //Uncomment these if you want to change to draw from bottom left.
    //g2d.translate(0, this.getPreferredSize().getHeight());
    //g2d.scale(1, -1);

    for (IShape s : this.shapes) {

      s.accept(new drawShapeVisitor(g2d));
    }
  }

  /**
   * Custom visitor to draw the desired shape on the given Graphics2D.
   */
  private class drawShapeVisitor implements IShapeVisitor<Void> {

    Graphics2D g2d;

    /**
     * Constructor giving the visitor a graphics 2D to draw onto.
     * @param g2d
     */
    public drawShapeVisitor(Graphics2D g2d) {
      this.g2d = g2d;
    }

    @Override
    public Void applyToRectangle(Rectangle rect) {
      int x = rect.getPosition().getX();
      int y = rect.getPosition().getY();
      int width = rect.getSize().getWidth();
      int height = rect.getSize().getHeight();
      int r = rect.getColor().getRed();
      int g = rect.getColor().getGreen();
      int b = rect.getColor().getBlue();

      g2d.setColor(new java.awt.Color(r, g, b));
      g2d.fillRect(x, y, width, height);
      return null;
    }

    @Override
    public Void applyToOval(Oval oval) {
      int x = oval.getPosition().getX();
      int y = oval.getPosition().getY();
      int width = oval.getSize().getWidth();
      int height = oval.getSize().getHeight();
      int r = oval.getColor().getRed();
      int g = oval.getColor().getGreen();
      int b = oval.getColor().getBlue();

      g2d.setColor(new java.awt.Color(r, g, b));
      g2d.fillOval(x, y, width, height);

      return null;
    }
  }
}
