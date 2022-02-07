package app.model.agents;

import app.controller.Vector;
import app.controller.graphicsEngine.Ray;
import app.model.Placeable;
import java.util.ArrayList;

public interface Agent extends Placeable
{
    void move();

    Vector getPosition();

    Vector getDirection();

    ArrayList<Ray> getView();

    void updateView(ArrayList<Ray> view);

    double getHearing();
}
