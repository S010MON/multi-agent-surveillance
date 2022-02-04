package app.model;

import app.controller.Vector;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall implements Placeable
{

    private Rectangle rectangle;
    private Vector[] corners;

    public Wall(double x, double y, double w, double h)
    {
        rectangle = new Rectangle(x, y, w, h);
        corners = new Vector[4];
        corners[0] = new Vector(x,y);
        corners[1] = new Vector(x+w,y);
        corners[2] = new Vector(x,y+h);
        corners[3] = new Vector(x+w,y+h);
    }

    @Override
    public Color getColor()
    {
        return null;
    }

    @Override
    public boolean isHit(Vector v)
    {
        return  v.getX() >= rectangle.getX() &&
                v.getX() <= rectangle.getX() + rectangle.getWidth() &&
                v.getY() >= rectangle.getY() &&
                v.getY() <= rectangle.getY() + rectangle.getHeight();
    }

    @Override
    public Vector intersection(Vector v)
    {
        // Find closest line
        Vector a = null;
        Vector b = null;
        double da = 1E11;
        double db = 1E11;

        for(Vector c: corners)
        {
            double dist = c.dist(v);

            if(dist < da)       // Check if first closest
            {
                a = c;
                da = dist;
            }
            else if (dist < db) // Check if second closest
            {
                b = c;
                db = dist;
            }
        }

        // Line x -> y
        Vector v_1 = new Vector().sub(a);
        Vector v_2 = b.sub(a);
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        Vector v_3 = new Vector(dx, dy);


        // Find intersection of line


        return null;
    }
}
