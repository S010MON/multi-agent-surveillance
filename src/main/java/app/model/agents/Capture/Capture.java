package app.model.agents.Capture;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
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
    private ArrayList<Vector> positionHistory = new ArrayList<>();
    private boolean captureComplete = false;
    private Vector intruderPos;
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
    }

    /* Constructor for testing */
    public Capture(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    @Override
    public Move move()
    {
        // TODO add check for move failure and add code to resolve such a situation...

        if(!maxTicsReached() && !captureComplete)
        {
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
            updateCounter();
            return nextMove(findTarget());
        }
        // TODO state change back to ACO if 'if' is false...
        return null;
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
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

            checkLocationsVisible(newLocations);
        }
        beliefSet.addAll(newLocations);
    }

    /**
     * Removes vectors that are visible from beliefSet.
     *
     * @param locations Potential intruder locations.
     */
    public void checkLocationsVisible(VectorSet locations)
    {
        for(Ray r : view)
        {
            locations.removeIf(location -> Intersection.hasLimitedIntersection(r, location, 1));
        }
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

        // Currently, returning the closest point in the belief set to us. (Will improve this and add more heuristics)
        return closestLocationInArray(new ArrayList<>(beliefSet), position);
    }

    /**
     * Finds closest vector to given point.
     *
     * @param locations Array of locations.
     * @param pos Position to compare to.
     *
     * @return Closest vector to pos from locations.
     */
    private Vector closestLocationInArray(ArrayList<Vector> locations, Vector pos)
    {
        double shortestDist = Double.MAX_VALUE;
        Vector closestPoint = null;
        for(Vector location : locations)
        {
            if(location.dist(pos) < shortestDist)
            {
                shortestDist = location.dist(pos);
                closestPoint = location;
            }
        }

        return closestPoint;
    }

    /**
     * TODO decide and return the next move...
     *
     * @param target the vector location to move towards.
     *
     * @return The next Move.
     */
    private Move nextMove(Vector target)
    {
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
