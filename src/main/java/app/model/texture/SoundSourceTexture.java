package app.model.texture;

import app.controller.linAlg.Vector;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class SoundSourceTexture implements Texture
{
    private Vector pos;
    private Color color;

    public SoundSourceTexture(Rectangle2D rectangle)
    {
        pos = new Vector(rectangle.getMinX(), rectangle.getMinY());
        this.color = Color.BLUE;
    }

    public void draw(GraphicsContext gc)
    {
        gc.setFill(color);
        gc.fillOval(pos.getX()-2, pos.getY()-2, 4, 4);
    }
}
