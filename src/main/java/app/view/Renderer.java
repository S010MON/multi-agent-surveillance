package app.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer extends Canvas
{
    public Renderer(int width, int height)
    {
        super(width, height);
        render();
    }

    public void render()
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.RED);
        gc.fillRect(0,0, 10, 10);
    }
}
