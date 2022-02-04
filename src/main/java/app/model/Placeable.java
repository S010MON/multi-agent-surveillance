package app.model;

import app.controller.Vector;
import javafx.scene.paint.Color;

public interface Placeable
{
    Vector getPosition();

    Color getColor();

    boolean isHit();
}
