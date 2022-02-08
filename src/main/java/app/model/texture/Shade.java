package app.model.texture;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Shade implements Texture
{
    private Rectangle rectangle;
    private Color color;

    public Shade(Rectangle rectangle)
    {
        this.rectangle = rectangle;
        this.color = Color.LIGHTGRAY;
    }

    public void draw(GraphicsContext gc)
    {
        gc.setFill(color);
        gc.fillRect(rectangle.getX(),
                rectangle.getY(),
                rectangle.getWidth(),
                rectangle.getHeight());
    }
}
