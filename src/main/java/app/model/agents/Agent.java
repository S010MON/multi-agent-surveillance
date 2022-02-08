package app.model.agents;

import app.controller.linAlg.Vector;
import app.controller.graphicsEngine.Ray;
import app.model.boundary.Boundary;
import java.util.ArrayList;

public interface Agent extends Boundary
{
    void move();

    Vector getPosition();

    Vector getDirection();

    ArrayList<Ray> getView();

    void updateView(ArrayList<Ray> view);

    double getHearing();
}
