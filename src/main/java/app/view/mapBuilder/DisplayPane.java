package app.view.mapBuilder;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayDeque;

public class DisplayPane extends Canvas
{
    private Color backgroundColour = Color.WHITE;


    public DisplayPane(StartMenu startMenu)
    {
        super(1000, 1000);
    }

    public void draw(ArrayDeque<MbObject> history)
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(backgroundColour);
        gc.fillRect(0, 0, getWidth(), getHeight());

        history.forEach(e -> e.draw());
    }
}
