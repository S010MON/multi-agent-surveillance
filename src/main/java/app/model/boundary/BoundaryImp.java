package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Line;
import app.controller.linAlg.Vector;
import app.model.Type;
import app.model.furniture.FurnitureType;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import javafx.scene.paint.Color;

import java.awt.geom.Line2D;

public class BoundaryImp implements Boundary
{
    protected Vector a;
    protected Vector b;
    protected Color colour;
    protected BoundaryType boundaryType = BoundaryType.VISIBLE_SOLID;
    @Getter protected Type type;

    public BoundaryImp(Vector a, Vector b)
    {
        this.a = a;
        this.b = b;
        this.type = Type.WALL;
    }

    public BoundaryImp(Vector a, Vector b, FurnitureType furnitureType)
    {
        this.a = a;
        this.b = b;
        this.type = Type.of(furnitureType);
    }

//    public BoundaryImp(Vector a, Vector b, BoundaryType boundaryType)
//    {
//        this.a = a;
//        this.b = b;
//        this.boundaryType = boundaryType;
//    }

    @Override
    public void draw(GraphicsContext gc)
    {
        if(colour == null)
            return;

        gc.setStroke(colour);
        gc.strokeLine(a.getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                a.getY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                b.getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                b.getY() * Info.getInfo().zoom + Info.getInfo().offsetY);
    }

    @Override
    public boolean isHit(Ray ray)
    {
        return Intersection.hasIntersection(a,b, ray);
    }

    @Override
    public Vector intersection(Ray ray)
    {
        return Intersection.findIntersection(a,b, ray);
    }

    @Override
    public boolean isCrossed(Vector startPoint, Vector endPoint)
    {
        return Intersection.hasIntersection(startPoint,endPoint,a,b);
    }

    @Override
    public boolean isCrossed(Vector centre, double radius)
    {
        if(!Intersection.hasIntersection(a, b, centre, radius))
            return false;

        Line line = new Line(a, b);
        Vector intersection = Intersection.findIntersection(a, b, centre, radius);
        return line.liesOn(intersection);
    }

    @Override
    public Vector getTeleport()
    {
        return null;
    }

    @Override
    public BoundaryType getBoundaryType()
    {
        return BoundaryType.of(type);
    }
}
