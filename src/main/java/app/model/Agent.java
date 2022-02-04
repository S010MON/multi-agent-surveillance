package app.model;

import app.controller.Ray;
import app.controller.Vector;
import java.util.ArrayList;

public interface Agent extends Placeable
{
    void move();

    Vector getDirection();

    ArrayList<Ray> getView();

    double getHearing();
}
