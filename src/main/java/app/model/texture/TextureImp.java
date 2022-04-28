package app.model.texture;

import app.controller.linAlg.Vector;
import app.view.simulation.Info;
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

    @Override
    public void draw(GraphicsContext gc)
    {
        gc.setFill(color);
        gc.fillRect(rectangle.getMinX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                rectangle.getMinY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                rectangle.getWidth() * Info.getInfo().zoom,
                rectangle.getHeight() * Info.getInfo().zoom);
    }

    @Override
    public boolean contains(Vector v)
    {
        return rectangle.contains(new Point2D(v.getX(), v.getY()));
    }
}
