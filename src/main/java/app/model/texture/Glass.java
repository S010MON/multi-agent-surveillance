package app.model.texture;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Glass implements Texture
{
    private Rectangle2D rectangle;
    private Color color;

    public Glass(Rectangle2D rectangle)
    {
        this.rectangle = rectangle;
        this.color = Color.LIGHTBLUE;
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        gc.setFill(color);
        gc.fillRect(rectangle.getMinX(),
                rectangle.getMinY(),
                rectangle.getWidth(),
                rectangle.getHeight());
    }
}
