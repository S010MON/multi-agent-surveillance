package app.model;

import app.controller.Beam;
import app.controller.Vector;
import java.util.ArrayList;

public interface Agent extends Placeable
{
    void move();

    Vector getPosition();

    Vector getDirection();

    ArrayList<Beam> getView();

    double getHearing();
}
