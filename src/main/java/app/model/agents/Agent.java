package app.model.agents;

import app.controller.linAlg.Vector;
import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.VectorSet;
import app.controller.soundEngine.SoundVector;
import app.model.boundary.Boundary;
import app.model.Move;
import app.view.agentView.AgentView;
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

    void clearHeard();

    void addHeard(ArrayList<SoundVector> soundVectors);

    ArrayList<SoundVector> getHeard();

    double getRadius();

    VectorSet getSeen();

    void setMoveFailed(boolean failed);

    void updateSeen(Vector vector);

    void addViewWindow(AgentView agentView);

    Team getTeam();

    void setTgtDirection(Vector tgtDirection);
}
