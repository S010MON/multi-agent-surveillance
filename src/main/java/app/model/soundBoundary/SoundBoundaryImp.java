package app.model.soundBoundary;

import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundRay;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class SoundBoundaryImp implements SoundBoundary {
    private Vector a;
    private  Vector b;

    public SoundBoundaryImp(Vector a, Vector b) {
        // TODO handle exception where a == b, because that just leads to terrible things

        this.a = a;
        this.b = b;
    }

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
    public boolean intersects(SoundRay soundRay) {
        return Intersection.hasIntersection(a,b,soundRay.getStart(), soundRay.getEnd());
    }

    @Override
    public boolean onSegment(Vector point) {
        // det(AB,AM) != 0 -> AB and AM are not parallel, so M cant be on AB
        if(b.sub(a).cross(point.sub(a)) != 0){
            return false;
        }

        double t;
        if(b.getX() == a.getX())
            t = (point.getY() - a.getY()) / (b.getY() - a.getY());
        else
            t = (point.getX() - a.getX()) / (b.getX() - a.getX());

        return  t <= 1 && t >= 0;
    }
}
