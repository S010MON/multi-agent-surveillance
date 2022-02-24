package app.view.mapBuilder;

import java.util.ArrayDeque;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lombok.Getter;

public class DisplayPane extends Canvas
{
    @Getter private ArrayDeque<MbObject> objects;
    private StartMenu startMenu;
    private Point2D click;
    private Rectangle2D selection;
    private final Color backgroundColour = Color.WHITE;

    public DisplayPane(StartMenu startMenu)
    {
        super(1100, 1000);
        this.startMenu = startMenu;
        objects = new ArrayDeque<>();
        click = null;
        selection = null;

        setOnMousePressed(this::mousePressed);
        setOnMouseDragged(this::mouseDragged);
        setOnMouseReleased(this::mouseReleased);
        draw();
    }

    public void draw()
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(backgroundColour);
        gc.fillRect(0, 0, getWidth(), getHeight());

        objects.forEach(e -> e.draw(gc));


        if(selection != null)
        {
            gc.setFill(Color.rgb(109,29,190, 0.5));
            gc.fillRect(selection.getMinX(), selection.getMinY(), selection.getWidth(), selection.getHeight());
        }
    }

    public void undo()
    {
        objects.removeLast();
        draw();
    }

    private void mousePressed(MouseEvent e)
    {
        click = new Point2D(e.getX(), e.getY());
    }

    private void mouseDragged(MouseEvent e)
    {
        if(click == null)
            return;

        double x, y, w, h;
        x = click.getX();
        y = click.getY();
        w = e.getX() - x;
        h = e.getY() - y;

        if(w < 0)
        {
           x = x + w;
           w = Math.abs(w);
        }

        if(h < 0)
        {
            y = y + h;
            h = Math.abs(h);
        }

        selection = new Rectangle2D(x, y, w, h);
        draw();
    }

    private void mouseReleased(MouseEvent e)
    {
        if(click != null)
        {
            objects.add(new MbObject(selection, startMenu.getFurniturePane().getCurrentType()));
            selection = null;
            click = null;
            draw();
        }
    }
}
