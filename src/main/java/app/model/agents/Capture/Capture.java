package app.model.agents.Capture;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Line;
import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.model.Move;
import app.model.Type;
import app.model.agents.AgentImp;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Capture extends AgentImp

{
    private final boolean DEBUG = false;
    private final int MAX_TICS_WITHOUT_SIGHT = 100;
    private final int MAX_MOVES_BEFORE_RETURN = 5;
    private final int MAX_POSITIONS_HELD = 3;
    private final double SCALE_TARGET = 1.5;
    @Getter @Setter private VectorSet beliefSet = new VectorSet();
    @Getter private VectorSet shortTermMemory = new VectorSet();
    private Queue<Vector> positionHistory = new LinkedList<>();
    private Queue<Vector> prevPositions =  new LinkedList<>();
    private Move prevMove;
    private int counter;

    /**
     * Constructor for capture agent.
     *
     * @param position    Our agents position.
     * @param direction   Our agents direction.
     * @param radius      Our radius.
     * @param type        Our type. (Guard).
     * @param intruderPos The position of the intruder to capture.
     */
    public Capture(Vector position, Vector direction, double radius, Type type, Vector intruderPos)
    {
        super(position, direction, radius, type);
        beliefSet.add(intruderPos);
        positionHistory.add(intruderPos);
    }

    /* Constructor for testing */
    public Capture(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    @Override
    public Move move()
    {
        if(!maxTicsReached())
        {
            updateKnowledge();
            if(moveFailed){
                return shortTermMemory();
            }else {
                shortTermMemory.clear();
            }
        } else
        {
            // TODO state change back to ACO...
            if(DEBUG) System.out.println("Max Tics Reached");
        }
        setShortTermMemory();
        return nextMove(findTarget());
    }

    public void updateKnowledge(){
        if(isTypeSeen(Type.INTRUDER))
        {
            if(DEBUG) System.out.println("Seen");
            if(DEBUG) System.out.println("Intruder Position: " + typePosition.toString());
            updatePositionHistory();
            beliefSet.clear();
            beliefSet.add(typePosition);
        }
        else
        {
            if(DEBUG) System.out.println("Not seen");
            updateBeliefSet();
            positionHistory.clear();
        }
        updatePrevPositions();
        updateCounter();
    }

    public Move shortTermMemory()
    {
        shortTermMemory.remove(position.add(prevMove.getDeltaPos()));

        // Remove possible moves that we have been in recently when we do not see the intruder.
        if(beliefSet.size() != 1)
            checkPrevPositions(shortTermMemory);

        Vector wantedMove = closestLocationInArray(new ArrayList<>(shortTermMemory), findTarget());
        Vector deltaPos = wantedMove.sub(position);
        Vector direction = deltaPos.normalise();
        prevMove = new Move(direction, deltaPos);
        return new Move(direction, deltaPos);
    }

    public void setShortTermMemory()
    {
        shortTermMemory.addAll(findAllPossiblePositions(position));
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
    }

    /**
     * Will loop through belief set and add all the next possible positions to the set.
     * Only unique positions are stored.
     */
    public void updateBeliefSet()
    {
        VectorSet newLocations = new VectorSet();
        for(Vector location : beliefSet)
        {
            newLocations.addAll(findAllPossiblePositions(location));
        }

        checkLocationsVisible(newLocations);
        beliefSet.addAll(newLocations);
    }

    /**
     * Returns the 4 cardinal locations reachable in one move.
     *
     * @param location Centre location to calculate from.
     *
     * @return The set of 4 vectors.
     */
    public VectorSet findAllPossiblePositions(Vector location)
    {
        VectorSet newLocations = new VectorSet();
        newLocations.add(new Vector(location.getX(), location.getY() + getMaxWalk())); // North
        newLocations.add(new Vector(location.getX() + getMaxWalk(), location.getY())); // East
        newLocations.add(new Vector(location.getX(), location.getY() - getMaxWalk())); // West
        newLocations.add(new Vector(location.getX() - getMaxWalk(), location.getY())); // South
        return newLocations;
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
        if(beliefSet.size() == 1 && positionHistory.size() > 0)
            return checkMomentum(new ArrayList<>(positionHistory));

        // Finds the centre of the belief region.
        Vector centre = findCentreOfRegion(new ArrayList<>(beliefSet));

        // Targets location closest to the centre.
        return closestLocationInArray(new ArrayList<>(beliefSet), centre);
    }

    public Vector checkMomentum(ArrayList<Vector> intruderHistory)
    {
        int arrLength = intruderHistory.size();
        if(arrLength == MAX_POSITIONS_HELD)
        {
            if(intruderHistory.get(arrLength-1).equals(intruderHistory.get(0)))
                return intruderHistory.get(arrLength-1);

            Line line = new Line(intruderHistory.get(0), intruderHistory.get(arrLength-1));
            for(int i = 1; i < arrLength - 1; i++)
            {
                if(!line.liesOn(intruderHistory.get(i)))
                    return intruderHistory.get(arrLength-1);
            }

            Vector intruderDirection = findDirection(intruderHistory.get(arrLength-1),
                                                     intruderHistory.get(0));
            double distBetween = intruderHistory.get(arrLength-1).dist(position) * SCALE_TARGET;
            return intruderHistory.get(arrLength-1).add(intruderDirection.scale(distBetween));
        }
        return intruderHistory.get(arrLength-1);
    }

    private Vector findDirection(Vector head, Vector tail)
    {
        Vector diff = head.sub(tail);
        return diff.normalise();
    }

    /**
     * Finds centre of the belief region denoted by the belief set.
     *
     * @param beliefRegion The belief set.
     * @return The centre of the region as a vector.
     */
    private Vector findCentreOfRegion(ArrayList<Vector> beliefRegion)
    {
        double totalX = 0;
        double totalY = 0;
        for(Vector location : beliefRegion)
        {
            totalX += location.getX();
            totalY += location.getY();
        }
        return new Vector(totalX / beliefRegion.size(), totalY / beliefRegion.size());
    }

    /**
     * Finds the closest vector to given point.
     *
     * @param locations Array of locations.
     * @param pos       Position to compare to.
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
     * Chooses the cardinal movement which gets the agent closer to the target.
     *
     * @param target the vector location to move towards.
     * @return The next Move.
     */
    private Move nextMove(Vector target)
    {
        if(DEBUG) System.out.println("Target: " + target.toString());
        // Populate with possible moves.
        VectorSet possibleMoves = findAllPossiblePositions(position);

        // Remove possible moves that we have been in recently when we do not see the intruder.
        if(beliefSet.size() != 1)
            checkPrevPositions(possibleMoves);

        Vector wantedMove = closestLocationInArray(new ArrayList<>(possibleMoves), target);
        Vector changeInPos = wantedMove.sub(position);
        direction = changeInPos.normalise();
        prevMove = new Move(direction, changeInPos);
        return new Move(direction, changeInPos);
    }

    private void checkPrevPositions(VectorSet moves)
    {
        moves.removeIf(l -> prevPositions.contains(l) && moves.size() > 1);
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

    private void updatePrevPositions()
    {
        prevPositions.add(position);
        if(prevPositions.size() > MAX_MOVES_BEFORE_RETURN)
            prevPositions.remove();
    }

    private void updatePositionHistory()
    {
        positionHistory.add(typePosition);
        if(positionHistory.size() > MAX_POSITIONS_HELD)
            positionHistory.remove();
    }
}