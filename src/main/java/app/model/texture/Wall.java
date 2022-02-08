package app.model.texture;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall implements Texture
{
    private Rectangle rectangle;
    private Color color;

    public Wall(Rectangle rectangle)
    {
        this.rectangle = rectangle;
        this.color = Color.BLACK;
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
