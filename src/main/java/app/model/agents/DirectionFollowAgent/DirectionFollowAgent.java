package app.model.agents.DirectionFollowAgent;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Angle;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.TypeInformation;
import app.model.agents.AgentImp;
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
    private TurnType lastTurn;
    private TurnType wallTurn;

    private Move previousMove;


    private final List<Vector> directions = Arrays.asList(new Vector(0,1),
            new Vector(1,0),
            new Vector(0,-1),
            new Vector(-1,0));

    public DirectionFollowAgent(Vector position, Vector direction, double radius, Type team, Vector targetDirection)
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

    public DirectionFollowAgent(Vector position, Vector direction, double radius, Type team, Vector targetDirection, double moveLen)
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
        if(DEBUG) { System.out.println("\n\n new agent:" +this); }

        if(position.dist(targetRay.getV())>targetRay.length())
        {
            if(DEBUG) { System.out.println("direction old targetRay: " +targetRay.direction().toString());}
            targetRay = new Ray(targetRay.getU(), targetRay.direction().scale(10000).add(targetRay.getV()));
            if(DEBUG) { System.out.println("direction new targetRay: " +targetRay.direction().toString());}
        }

        switch(internalState)
        {
            case followRay -> { previousMove = followRay();}
            case followWall -> { previousMove = followWall(); }
            default -> { previousMove = null;}
        }
        if (DEBUG) { System.out.println("Move: " +previousMove.toString()); }
        return previousMove;
    }

    private Move followRay()
    {
        if(moveFailed)
        {
            if(DEBUG) {
                System.out.println("\nmove failed, no wallDetected in direction: " + noWallDetected(previousMove.getEndDir().getAngle()) +"\n");
                System.out.println("previous move failed, state: " +internalState);
                System.out.println("Move: " +previousMove.toString());
            }
        }


        // followRay assumes the start point is on the ray itself
        if(!direction.equals(targetRay.direction()))
        {
            return new Move(targetRay.direction(), new Vector(0,0));
        }

        Double dist = distanceToObstacle(targetRay.angle());


        if(dist==null || Math.abs(dist) - radius >= moveLength){
            if(DEBUG)
            {
                System.out.println("No wall, keep following ray to target");
            }
            return new Move(targetRay.direction(), targetRay.direction().scale(moveLength));
        }
        else
        {
            internalState = InternalState.followWall;
            if(Math.random()>0.5)
                wallTurn = TurnType.LEFT;
            else
                wallTurn = TurnType.RIGHT;
            lastTurn = TurnType.NO_TURN;

            Vector newDirection = getDirectionStartWallFollowing(targetRay.direction());
            if(DEBUG)
            {
                System.out.println("\n--------------");
                System.out.println("going into followWall state");
                System.out.println("newDirection: " +newDirection.toString());
                System.out.println("--------------");
            }

            return new Move(newDirection, targetRay.direction().scale(dist-radius));
        }
    }

    private Move followWall()
    {
        if(moveFailed)
        {
            if(DEBUG) {
                System.out.println("\nmove failed, no wallDetected in direction: " + noWallDetected(previousMove.getEndDir().getAngle()) +"\n");
                System.out.println("previous move failed, state: " +internalState);
                System.out.println("Move: " +previousMove.toString());
            }

            return followGlassWall();
        }

        if(!directions.contains(direction))
        {
            throw new RuntimeException("Agent is in followingWall state, but direction isn't cardinal");
        }
        Move move = runWallFollowAlgorithm();
        Vector newPosition = position.add(move.getDeltaPos());

        if(onDirectionRay(newPosition) && !onDirectionRay(position))
        {
            internalState = InternalState.followRay;
            if(DEBUG)
            {
                System.out.println("\n--------------");
                System.out.println("going into followRay state");
                System.out.println("--------------");
            }
        }
        if (DEBUG) { System.out.println("Move: " +move.toString()); }
        return move;
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
        double anglePrecision = 1;

        boolean wallDetected = false;
        for(Ray r : view)
        {
            if(TypeInformation.isSolid(r.getType()) && Angle.angleInRange(r.angle(), rayAngle+anglePrecision, rayAngle-anglePrecision))
            {
                wallDetected = true;
                if(r.length() <= moveLength)
                {
                    if(DEBUG)
                    {
                        System.out.println("type:" + r.getType());
                        System.out.println("     WALL DETECTED! Ray Angle: " + rayAngle);
                    }
                    return false;
                }
            }
        }
        if(DEBUG && wallDetected) { System.out.println("     ray with angle " +rayAngle+ " exists in view and no wall detected");}
        else if(DEBUG) { System.out.println("     No ray with Ray angle: " + rayAngle + " detected,  position: "+position.toString() +
                "\n       position: " + position.toString() +
                "\n       direction angle: " + direction.getAngle()); }

        return true;
    }

    private Move followGlassWall()
    {
        lastTurn = wallTurn;
        Vector newDirection = rotateAgentAsOppositeWallTurn();
        return new Move(newDirection, new Vector(0,0));
    }

    public Vector getDirectionStartWallFollowing(Vector diagonalDirection)
    {
        if(DEBUG) { System.out.println("in getDirectionStartWallFollowing-method");}
        // the default case should never happen, since this function should only be called if one of the directions has a wall in front of it
        int indexDirection = 0;
        double prevDelta = 400.0; // some number too big to be reached
        for(int i=0; i<directions.size(); i++)
        {
            Vector dir = directions.get(i);
            double directionDelta = Math.abs(diagonalDirection.getAngle()-dir.getAngle());

            // should noWallDetected be run from the viewpoint of the agent already hitting?
            if(!noWallDetected(dir.getAngle()))
            {
                if(directionDelta < prevDelta){
                    indexDirection = i;
                    prevDelta = directionDelta;
                }
            }
        }

        if(wallTurn==TurnType.RIGHT)
        {
            if(indexDirection==directions.size()-1)
                return directions.get(0);
            else
                return directions.get(indexDirection+1);
        }
        if(wallTurn==TurnType.LEFT)
        {
            if(indexDirection==0)
                return directions.get(directions.size()-1);
            else
                return directions.get(indexDirection-1);
        }

        throw new RuntimeException("wallTurn type not specified while in WallFollow state");
    }

    private Double distanceToObstacle(double rayAngle){
        if (DEBUG)
            System.out.println("distanceToObstacle method:");
        for (Ray r : view)
        {
            if (TypeInformation.isSolid(r.getType()) && (r.angle() <= rayAngle + 1.0 && r.angle() >= rayAngle - 1.0))
            {
                if (DEBUG)
                {
                    System.out.println("type:" + r.getType());
                    System.out.println("     WALL DETECTED! Distance: " + r.length() + ", Ray Angle: " + rayAngle);
                }
                return r.length();
            }
        }
        if (DEBUG)
            System.out.println("     No wall detected with ray of angle: " + rayAngle);

        return null;
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
            if (DEBUG) { System.out.println("Position change: " +newMove.toString()); }
        }
        else if (noWallDetected(getAngleOfWallTurnRay()))
        {
            if (DEBUG) { System.out.println("Angle of wallTurn ("+wallTurn+") ray: " + getAngleOfWallTurnRay()); ; }
            if (DEBUG) { System.out.println("ALGORITHM CASE 2"); }
            newDirection = rotateAgentAsWallTurn();
            if (DEBUG) { System.out.println("new direction: " +newDirection.toString()); }
            lastTurn = wallTurn;
        }
        else if (noWallDetected(direction.getAngle()))
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 3"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            lastTurn = TurnType.NO_TURN;
            if (DEBUG) { System.out.println("Position change: " +newMove.toString()); }
        }
        else
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 4"); }
            newDirection = rotateAgentAsOppositeWallTurn();
            if(wallTurn==TurnType.LEFT)
                lastTurn = TurnType.RIGHT;
            else
                lastTurn = TurnType.LEFT;
            if (DEBUG) { System.out.println("new direction: " +newDirection.toString()); }
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
        for (int i = 0; i < directions.size(); i++)
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
