package app.model.agents.DirectionFollowAgent;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.agents.AgentImp;
import app.model.agents.Cells.BooleanCell;
import app.model.agents.Team;
import app.model.agents.WallFollow.WallFollowAgent;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;


public class DirectionFollowAgent extends AgentImp
{
    public enum InternalState
    {
        followWall,
        followRay,
        goToTarget,
    }

    public enum TurnType
    {
        LEFT,
        RIGHT,
        NO_TURN,
    }

    private Ray targetRay;
    @Getter private InternalState internalState;
    private boolean DEBUG = false;
    @Getter @Setter private double moveLength = 20;
    private TurnType lastTurn;
    private TurnType wallTurn;
    private final List<Vector> directions = Arrays.asList(new Vector(0,1),
            new Vector(1,0),
            new Vector(0,-1),
            new Vector(-1,0));

    //TODO: rescale targetRay if agent goes beyond it

    public DirectionFollowAgent(Vector position, Vector direction, double radius, Team team, Vector targetDirection)
    {
        super(position, direction, radius, team);
        if(targetDirection == null)
        {
            throw new RuntimeException("no targetDirection provided for intruder");
        }

        targetRay = new Ray(position, position.add(targetDirection.scale(10000)));

        internalState = InternalState.followRay;
        lastTurn = TurnType.NO_TURN;
    }

    public DirectionFollowAgent(Vector position, Vector direction, double radius, Team team, Vector targetDirection, double moveLen)
    {
        super(position, direction, radius, team);
        if(targetDirection == null)
        {
            throw new RuntimeException("no targetDirection provided for intruder");
        }
        targetRay = new Ray(position, targetDirection);
        moveLength = moveLen;

        internalState = InternalState.followRay;
        lastTurn = TurnType.NO_TURN;
    }


    @Override
    public Move move()
    {
        switch(internalState)
        {
            case followRay -> { return followRay(); }
            case followWall -> { return followWall(); }
            case goToTarget -> { return goToTarget(); }
        }

        return null;
    }

    private Move followRay()
    {
        // followRay assumes the start point is on the ray itself
        if(!direction.equals(targetRay.direction()))
        {
            return new Move(targetRay.direction(), new Vector(0,0));
        }

        double dist = Math.abs(distanceToObstacle(targetRay.angle()) - radius);

        if(dist < 0.0){
            // means there are no obstacles so just go straight to the target
            internalState = InternalState.goToTarget;
            return move();
        }
        else if(dist >= moveLength){
            return new Move(targetRay.direction(), targetRay.direction().scale(moveLength));
        }
        else
        {
            internalState = InternalState.followWall;
            return new Move(targetRay.direction(), targetRay.direction().scale(dist));
        }

        //return new Move(targetRay.getU(), targetRay.getU().scale(dist));
    }

    private Move followWall()
    {
        Move move = runWallFollowAlgorithm();
        Vector newPosition = position.add(move.getDeltaPos());
        if(onDirectionRay(newPosition))
        {
            internalState = InternalState.followRay;
        }
        return move;
    }

    private Move goToTarget()
    {
        return null;
    }

    /**
     * Method for checking for walls/obstacles for getting next move in the wall following algorithm.
     * Walls/obstacles are checked in the direction of the given rayAngle by checking if that ray detects
     * an obstacle within the moveLength distance range.
     * @param rayAngle angle of the direction to be checked.
     * @return true if no obstacle detected; false if obstacle detected
     */
    public boolean noWallDetected(double rayAngle)
    {
        for (Ray r : view)
        {
            if ((r.angle() <= rayAngle + 1.0 && r.angle() >= rayAngle - 1.0) && r.length() <= moveLength)
            {
                if (DEBUG)
                    System.out.println("WALL DETECTED! Ray Angle: " + rayAngle);
                return false;
            }
        }
        if (DEBUG)
            System.out.println("No wall detected with ray of angle: " + rayAngle);
        return true;
    }

    private double distanceToObstacle(double rayAngle){
        for (Ray r : view)
        {
            if ((r.angle() <= rayAngle + 1.0 && r.angle() >= rayAngle - 1.0))
            {
                if (DEBUG)
                    System.out.println("WALL DETECTED! Ray Angle: " + rayAngle);
                return r.length();
            }
        }
        if (DEBUG)
            System.out.println("No wall detected with ray of angle: " + rayAngle);
        return -1.0;
    }


    /** Pseudocode for simple wall following algorithm:
         if (turned wallTurn previously and forward no wall)
            go forward;
         else if (no wall at wallTurn)
            turn 90 deg wallTurn;
         else if (no wall forward)
            go forward;
         else
            turn 90 deg opposite of wallTurn;

     * based on: https://blogs.ntu.edu.sg/scemdp-201718s1-g14/exploration-algorithm/
     */


    public Move runWallFollowAlgorithm()
    {
        Vector newMove = new Vector(0,0);
        Vector newDirection = direction;
        if (lastTurn == wallTurn && noWallDetected(direction.getAngle()))
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 1"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            lastTurn = TurnType.NO_TURN;
        }
        else if (noWallDetected(getAngleOfWallTurnRay()))
        {
            if (DEBUG) { System.out.println("Angle of left ray: " + getAngleOfLeftRay()); ; }
            if (DEBUG) { System.out.println("ALGORITHM CASE 2"); }
            newDirection = rotateAgentAsWallTurn();
            lastTurn = wallTurn;
        }
        else if (noWallDetected(direction.getAngle()))
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 3"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            lastTurn = TurnType.NO_TURN;
        }
        else
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 4"); }
            newDirection = rotateAgentRight();
            lastTurn = TurnType.RIGHT;
        }
        return new Move(newDirection,newMove);
    }

    public Move runLeftWallFollowAlgorithm()
    {
        Vector newMove = new Vector(0,0);
        Vector newDirection = direction;
        if (lastTurn == TurnType.LEFT && noWallDetected(direction.getAngle()))
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 1"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            lastTurn = TurnType.NO_TURN;
        }
        else if (noWallDetected(getAngleOfLeftRay()))
        {
            if (DEBUG) { System.out.println("Angle of left ray: " + getAngleOfLeftRay()); ; }
            if (DEBUG) { System.out.println("ALGORITHM CASE 2"); }
            newDirection = rotateAgentLeft();
            lastTurn = TurnType.LEFT;
        }
        else if (noWallDetected(direction.getAngle()))
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 3"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            lastTurn = TurnType.NO_TURN;
        }
        else
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 4"); }
            newDirection = rotateAgentRight();
            lastTurn = TurnType.RIGHT;
        }
        return new Move(newDirection,newMove);
    }

    private boolean onDirectionRay(Vector position)
    {
        return Intersection.hasIntersection(targetRay, position, moveLength/2);
    }

    public double getAngleOfWallTurnRay()
    {
        switch(wallTurn)
        {
            case LEFT -> {return getAngleOfLeftRay();}
            case RIGHT -> {return getAngleOfRightRay();}
        }
        return 0;
    }

    public double getAngleOfLeftRay()
    {
        double currentAngle = direction.getAngle();
        if (currentAngle < 270)
        {
            return currentAngle + 90;
        }
        else
        {
            return currentAngle + 90 - 360;
        }
    }

    public double getAngleOfRightRay()
    {
        double currentAngle = direction.getAngle();
        if (currentAngle > 90)
        {
            return currentAngle - 90;
        }
        else
        {
            return currentAngle - 90 + 360;
        }
    }

    public Vector rotateAgentAsWallTurn()
    {
        switch(wallTurn)
        {
            case LEFT -> {return rotateAgentLeft();}
            case RIGHT -> {return rotateAgentRight();}
        }
        // redundant by design
        return null;
    }

    public Vector rotateAgentAsOppositeWallTurn()
    {
        switch(wallTurn)
        {
            case RIGHT -> {return rotateAgentLeft();}
            case LEFT -> {return rotateAgentRight();}
        }
        // redundant by design
        return null;
    }

    public Vector rotateAgentLeft()
    {
        for (int i = 0; i <= directions.size(); i++)
        {
            if (direction.equals(directions.get(i)))
            {
                if (i == directions.size()-1)
                {
                    return directions.get(0);
                }
                else
                {
                    return directions.get(i+1);
                }
            }
        }
        throw new RuntimeException("None of the 4 cardinal directions were reached when rotating agent.");
    }

    public Vector rotateAgentRight()
    {
        for (int i = 0; i <= directions.size(); i++)
        {
            if (direction.equals(directions.get(i)))
            {
                if (i == 0)
                {
                    return directions.get(3);
                }
                else
                {
                    return directions.get(i-1);
                }
            }
        }
        throw new RuntimeException("None of the 4 cardinal directions were reached when rotating agent.");
    }
}
