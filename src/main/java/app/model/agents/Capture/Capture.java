package app.model.agents.Capture;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.model.Move;
import app.model.Type;
import app.model.agents.AgentImp;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Capture extends AgentImp

{
    private final int MAX_TICS_WITHOUT_SIGHT = 100;
    @Getter @Setter private VectorSet beliefSet = new VectorSet();
    private Vector intruderPos;
    private ArrayList<Vector> positionHistory = new ArrayList<>();
    private int counter;

    /**
     * Constructor for capture agent.
     *
     * @param position Our agents position.
     * @param direction Our agents direction.
     * @param radius Our radius.
     * @param type Our type. (Guard).
     * @param intruderPos The position of the intruder to capture.
     */
    public Capture(Vector position, Vector direction, double radius, Type type, Vector intruderPos)
    {
        super(position, direction, radius, type);
        this.intruderPos = intruderPos;
        initializeCapturing();
    }

    /* Constructor for testing */
    public Capture(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    /**
     * Starts the capturing agent and loops until state complete.
     */
    private void initializeCapturing()
    {
        while(!maxTicsReached())
        {
            updateCounter();
            if(isIntruderSeen())
            {
                addPositionHistory(intruderPos);
                beliefSet.clear();
                beliefSet.add(intruderPos);
            }
            else
            {
                findAllPossiblePositions();
            }
            Move nextMove = nextMove(findTarget());
            // TODO moving implemented
        }
    }

    /**
     * Method checks rays to see if we still see the intruder. Updates the intruderPos variable.
     *
     * @return True/False if we see intruder or not.
     */
    public boolean isIntruderSeen()
    {
        boolean seen = false;
        for(Ray r : view)
        {
            if(r.getType() == Type.INTRUDER)
            {
                seen = true;
                intruderPos = r.getV();
                break;
            }
        }
        return seen;
    }

    private void addPositionHistory(Vector curPos)
    {
        positionHistory.add(curPos);
    }


    /**
     * Will loop through belief set and add all the next possible positions to the set.
     * Only unique positions are stored.
     */
    public void findAllPossiblePositions()
    {
        VectorSet newLocations = new VectorSet();
        for(Vector location : beliefSet)
        {
            // Use our max walk as estimate.
            newLocations.add(new Vector(location.getX(), location.getY() + getMaxWalk())); // North
            newLocations.add(new Vector(location.getX() + getMaxWalk(), location.getY())); // East
            newLocations.add(new Vector(location.getX(), location.getY() - getMaxWalk())); // West
            newLocations.add(new Vector(location.getX() - getMaxWalk(), location.getY())); // South

            // TODO Check if we see these points, i.e. the intruder is not there.
        }
        beliefSet.addAll(newLocations);
        // Will add a check to see if the position we are adding to the belief set is intersecting with one of our rays
        // because we would therefore see the intruder, and it could not be a possible position.
    }

    /**
     * Picks a location in the belief set to use as a target for the next move.
     *
     * @return the vector location to target.
     */
    public Vector findTarget()
    {
        //Using heuristics decides on target for next move.
        // If our belief set is size 1, then we have our target.
        if(beliefSet.size() == 1)
            for(Vector v : beliefSet)
                return v;

        double shortestDist = Double.MAX_VALUE;
        Vector closestPoint = null;
        for(Vector location : beliefSet)
        {
            if(location.dist(position) < shortestDist)
            {
                shortestDist = location.dist(position);
                closestPoint = location;
            }
        }

        // Currently, returning the closest point in the belief set to us. (Will improve this and add more heuristics)
        return closestPoint;
    }

    /**
     * TODO
     *
     * @param target the vector location to move towards.
     *
     * @return The next Move.
     */
    private Move nextMove(Vector target)
    {
        // Decides on the best next move towards the target position. (Will be implemented when new World graph
        // implementation is understood).
        return null;
    }

    private boolean maxTicsReached()
    {
        return (counter > MAX_TICS_WITHOUT_SIGHT);
    }

    private void updateCounter()
    {
        if(beliefSet.size() > 1)
            counter++;
        else if(beliefSet.size() == 1)
            counter = 0; // Reset counter when we see the intruder.
    }
}
