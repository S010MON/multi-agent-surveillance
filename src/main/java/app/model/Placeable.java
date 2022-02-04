package app.model;

import app.controller.Vector;
import javafx.scene.paint.Color;

public interface Placeable
{
    Color getColor();

    boolean isHit(Vector v);

    Vector intersection(Vector v);
}
