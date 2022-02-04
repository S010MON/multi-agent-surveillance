package app.model;

import app.controller.Ray;
import app.controller.Vector;
import javafx.scene.paint.Color;

public interface Placeable
{
    Color getColor();

    boolean isHit(Ray ray);

    Vector intersection(Ray ray);
}
