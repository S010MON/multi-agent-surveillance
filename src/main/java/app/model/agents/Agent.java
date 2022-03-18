package app.model.agents;

import app.controller.linAlg.Vector;
import app.controller.graphicsEngine.Ray;
import app.model.boundary.Boundary;
import app.model.Move;
import app.view.agentView.AgentView;

import java.util.ArrayList;
import java.util.HashSet;

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

    void setMoveFailed(boolean failed);

    HashSet<Vector> getSeen();

    void updateSeen(Vector vector);

    void addViewWindow(AgentView agentView);
}
