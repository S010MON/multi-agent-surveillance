package app.controller.soundEngine;

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

    @Override
    public boolean onSegment(Vector point) {
        // det(AB,AM) != 0 -> AB and AM are not parallel, so M cant be on AB
        if(b.sub(a).cross(point.sub(a)) != 0){
            return false;
        }
        double t = (point.getX() - a.getX()) / (b.getX() - a.getX());

        return  t <= 1 && t >= 0;
    }
}
