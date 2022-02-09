package app.model.agents;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;

public class WallFollowAgent extends AgentImp {

    private Vector prevMove;
    private Ray forwardRay;
    private double obstacleDetectionDistance = 3;
    private double moveLength = 3;

    public WallFollowAgent(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
        prevMove = new Vector();
    }

    @Override
    public void move()
    {
        // pseudocode for simple (left) wall following algorithm:
//        if (turned left previously and forward no wall)
//            go forward;
//        else if (no wall at left)
//            turn 90 deg left;
//        else if (no wall forward)
//            go forward;
//        else
//            turn 90 deg right;

        // if (prevMove.equals(new Vector(0, -moveLength)) && !wallDetected(forwardRay))
        // keep in mind: need to face towards moving direction
    }

    private boolean wallDetected(Ray forwardRay)
    {
//        if (forwardRay.rayLength() < obstacleDetectionDistance)
//        {
//            return true;
//        }
        return false;
    }

}
