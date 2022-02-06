package app.model.agents;

import app.controller.Beam;
import app.controller.Vector;
import app.model.Placeable;

import java.util.ArrayList;

public interface Agent extends Placeable
{
    void move();

    Vector getPosition();

    Vector getDirection();

    ArrayList<Beam> getView();

    void updateView(ArrayList<Beam> view);

    double getHearing();
}
