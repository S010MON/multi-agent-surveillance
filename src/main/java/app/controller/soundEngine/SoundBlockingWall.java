package app.controller.soundEngine;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
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
        return Intersection.findIntersection(a,b,soundRay.getStart(), soundRay.getEnd());
    }
}
