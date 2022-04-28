package app.model.texture;

import app.controller.linAlg.Vector;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TextureImp implements Texture
{
    protected Rectangle2D rectangle;
    protected Color color;

    public TextureImp(Rectangle2D rectangle)
    {
        this.rectangle = rectangle;
        this.color = Color.WHITE;
    }

    public void draw(GraphicsContext gc) {}

    public boolean contains(Vector v)
    {
        return rectangle.contains(new Point2D(v.getX(), v.getY()));
    }
}
