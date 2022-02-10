package app.model.agents;

import app.controller.linAlg.Vector;
import app.controller.graphicsEngine.Ray;
import app.model.boundary.Boundary;
import java.util.ArrayList;

public interface Agent extends Boundary
{
    void updateLocation(Vector endPoint);

    Vector move();

    Vector getPosition();

    Vector getDirection();

    ArrayList<Ray> getView();

    void updateView(ArrayList<Ray> view);

    double getHearing();
}
