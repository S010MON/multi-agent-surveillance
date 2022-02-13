package app.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MapBuilder extends Canvas
{
    private Color backgroundColour;

    public MapBuilder(int width, int height)
    {
        super(width, height);
        backgroundColour = Color.WHITE;
        // Background
        GraphicsContext gc = this.getGraphicsContext2D();
        drawBackground(gc);
    }

    public void drawBackground(GraphicsContext gc)
    {
        gc.setFill(backgroundColour);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }
}
