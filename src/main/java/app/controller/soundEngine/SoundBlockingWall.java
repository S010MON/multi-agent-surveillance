package app.controller.soundEngine;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;

import java.util.ArrayList;


@AllArgsConstructor
public class SoundBlockingWall implements SoundBoundary{
    private Vector a;
    private  Vector b;

    @Override
    public ArrayList<Vector> getCorners(){
        ArrayList<Vector> corners = new ArrayList<>();
        corners.add(a);
        corners.add(b);
        return corners;
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        gc.setStroke(Color.BLACK);
        gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
    }

    @Override
    public Vector intersection(SoundRay soundRay)
    {
        double x_1 = a.getX();
        double y_1 = a.getY();
        double x_2 = b.getX();
        double y_2 = b.getY();
        double x_3 = soundRay.getStart().getX();
        double y_3 = soundRay.getStart().getY();
        double x_4 = soundRay.getEnd().getX();
        double y_4 = soundRay.getEnd().getY();

        double denominator = (x_1 - x_2) * (y_3 - y_4) - (y_1 - y_2) * (x_3 - x_4);

        // lines are parallel
        if(denominator == 0) return null;

        double t = ((x_1 - x_3) * (y_3 - y_4) - (y_1 - y_3) * (x_3 - x_4)) / denominator;
        double u = ((x_1 - x_3)*(y_1 - y_2) - (y_1 - y_3)*(x_1 - x_2)) / denominator;

        // lines intersect but not within the segments
        if(t > 1 || t < 0 || u > 1 || u < 0) return null;

        double x = x_1 + (t * (x_2 - x_1));
        double y = y_1 + (t * (y_2 - y_1));
        return new Vector(x, y);
    }
}
