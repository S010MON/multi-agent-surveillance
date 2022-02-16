package app.model.agents.ACO;

import app.controller.linAlg.Vector;

import java.util.HashMap;

//Takes in an agent's vision and returns an action.
public class AcoVisionToAction
{
    private HashMap<Integer, Cell> agentMap;
    private final double[] cardinalPoints = {0, 90, 180, 270};
    private double obstacleDetectionDistance = 10;
    private Vector currentPosition;

    public AcoVisionToAction(HashMap<Integer, Cell> map)
    {
        agentMap = map;
        currentPosition = new Vector();
    }

}
