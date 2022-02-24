package app.view.mapBuilder;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.ArrayDeque;

public class DisplayPane extends Canvas
{
    @Getter private ArrayDeque<MbObject> objects;
    private StartMenu startMenu;

    private final Color backgroundColour = Color.WHITE;

    public DisplayPane(StartMenu startMenu)
    {
        super(1000, 1000);
        this.startMenu = startMenu;
        objects = new ArrayDeque<>();
        draw();
    }

    public void draw()
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(backgroundColour);
        gc.fillRect(0, 0, getWidth(), getHeight());

        objects.forEach(e -> e.draw(gc));
    }

    public void addObject(MbObject object)
    {
        objects.add(object);
        draw();
    }

    public void undo()
    {
        objects.removeLast();
        draw();
    }
}
