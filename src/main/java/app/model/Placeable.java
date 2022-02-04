package app.model;

import app.controller.Ray;
import app.controller.Vector;
import javafx.scene.canvas.GraphicsContext;

public interface Placeable
{
    void draw(GraphicsContext gc);

    boolean isHit(Ray ray);

    Vector intersection(Ray ray);
}
