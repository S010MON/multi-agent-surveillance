package app.view;

import lombok.AllArgsConstructor;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

@AllArgsConstructor
public class DrawRectangle implements UIRect
{
    private Rectangle2D rect;
    private GraphicsContext gc;
    private Color colour;
    private boolean fill;

    @Override
    public void run()
    {
        if(fill)
        {
            gc.setFill(colour);
            gc.fillRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
        }
        else
        {
            gc.setStroke(colour);
            gc.strokeRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
        }
    }

    @Override
    public void undo()
    {
        if(fill)
        {
            gc.setFill(Color.WHITE);
            gc.fillRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
        }
        else
        {
            gc.setStroke(Color.WHITE);
            gc.strokeRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
        }
    }
}
