package app.view.mapBuilder;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GridLine extends Line
{
    public GridLine(double startX, double startY, double endX, double endY)
    {
        super(startX, startY, endX, endY);
    }

    public void draw(GraphicsContext gc)
    {
        gc.setStroke(Color.rgb(102,204,255, 0.5));
        gc.strokeLine(getStartX(), getStartY(), getEndX(), getEndY());
    }
}
