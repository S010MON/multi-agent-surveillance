package app.view;

import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

@AllArgsConstructor
@Setter
public class DrawRectangle implements UIRect
{
    @Getter private FurnitureType type;
    @Getter private Vector vector;
    @Getter private Rectangle2D rect;
    private GraphicsContext gc;
    private Color colour;
    private boolean fill;

    public DrawRectangle(){}

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
