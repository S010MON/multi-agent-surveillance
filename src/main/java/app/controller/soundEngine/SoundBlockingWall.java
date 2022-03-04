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

    @Override
    public Vector projectOnto(Vector point) {
        Vector v = b.sub(a);
        double sqrNorm = v.dot(v);

        double x_p  = a.getX();
        double y_p = a.getY();

        double x_v = v.getX();
        double y_v = v.getY();

        double x_m = point.getX();
        double y_m = point.getY();

        // Projecting point M onto Line (P,v) to get the new point M' = (x,y)

        double nominator = y_m * y_v * y_v + y_p * x_v * x_v + y_v * x_m * x_v - x_v * x_p * y_v;

        double y =  nominator / sqrNorm;

        double x = (x_m * x_v + y_m * y_v - y * y_v) / x_v;

        Vector mPrime = new Vector(x,y);

        if(onSegment(mPrime)){
            return mPrime;
        } else {
            return null;
        }
    }
}
