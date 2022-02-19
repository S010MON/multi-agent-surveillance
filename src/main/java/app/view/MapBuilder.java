package app.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayDeque;

public class MapBuilder extends Canvas
{
    private Color backgroundColour;
    public GraphicsContext gc = this.getGraphicsContext2D();
    private ArrayDeque<UIRect> history = new ArrayDeque<>();

    public MapBuilder(int width, int height)
    {
        super(width, height);
        backgroundColour = Color.WHITE;
        // Background
        drawBackground(gc);
    }

    public void run(UIRect ur)
    {
        history.addLast(ur);
        ur.run();
    }

    public void undo()
    {
        if(!history.isEmpty())
        {
            UIRect lastRect = history.removeLast();
            lastRect.undo();
        }
    }

    public void drawBackground(GraphicsContext gc)
    {
        gc.setFill(backgroundColour);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }
}
