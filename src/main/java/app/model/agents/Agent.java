package app.model.agents;

import app.controller.linAlg.Vector;
import app.controller.graphicsEngine.Ray;
import app.model.boundary.Boundary;
import app.model.map.Move;
import java.util.ArrayList;

public interface Agent extends Boundary
{
    void updateLocation(Vector endPoint);

    boolean isHit(Ray ray);

    Move move();

    Vector getPosition();

    Vector getDirection();

    ArrayList<Ray> getView();

    double getMaxWalk();

    double getMaxSprint();

    void setMaxWalk(double walkSpeed);

    void setMaxSprint(double sprintSpeed);

    void updateView(ArrayList<Ray> view);

    double getHearing();

    double getRadius();

    Vector getTargetDirection();

    void setTargetDirection(Vector targetDirection);

}
