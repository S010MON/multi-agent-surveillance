package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;

public interface Boundary
{
    void draw(GraphicsContext gc);

    boolean isHit(Ray ray);

    Vector intersection(Ray ray);

    boolean isCrossed(Vector startPoint, Vector endPoint);

    Vector getTeleport();
}
