package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;

public class TransparentBoundary implements Boundary
{
    @Override
    public void draw(GraphicsContext gc) {}

    @Override
    public boolean isHit(Ray ray)
    {
        return false;
    }

    @Override
    public Vector intersection(Ray ray)
    {
        return null;
    }

    @Override
    public boolean validMove(Vector startPoint, Vector endPoint) {
        return false;
    }
}
