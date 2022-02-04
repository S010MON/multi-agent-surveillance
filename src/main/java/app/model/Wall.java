package app.model;

import app.controller.Vector;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall implements Placeable
{

    private Rectangle rectangle;

    public Wall(double x, double y, double w, double h)
    {
        rectangle = new Rectangle(x, y, w, h);
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
        return null;
    }
}
