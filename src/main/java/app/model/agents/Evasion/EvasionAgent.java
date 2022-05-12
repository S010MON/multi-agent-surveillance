package app.model.agents.Evasion;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.agents.WallFollow.WallFollowAgent;

public class EvasionAgent extends AgentImp
{
    private EvasionStrategy strategy;
    private Move zeroMove = new Move(new Vector(), new Vector());
    private boolean guardInView = true;

    public EvasionAgent(Vector position, Vector direction, double radius, Type type, EvasionStrategy strategy)
    {
        super(position, direction, radius, type);
        this.strategy = strategy;
    }

    public EvasionAgent(Vector position, Vector direction, double radius, Type type){
        this(position,direction, radius, type, EvasionStrategy.RANDOM);
    }

    @Override
    public Move move()
    {
        switch(strategy)
        {
            case AWAY -> {
                return moveAway();
            }
            case RANDOMDIRECTED -> {
                return  moveRandomDirected();
            }
            default -> {
                return moveRandom();
            }
        }
    }

    private Move moveRandom(){
        int theta = (int) (Math.random() * 360);
        Vector randomDirection = new Vector(0, 1).rotate(theta);

        return new Move(direction, randomDirection.scale(maxSprint));
    }

    private Move moveRandomDirected(){
        Vector guardDirection = findFirstGuardDirection();

        if(guardDirection == null){
            guardInView = false;
            return zeroMove;
        }

        Vector flippedDirection = guardDirection.rotate(180);

        int theta = (int) (Math.random() * 360);
        Vector randomDirection = new Vector(0, 1).rotate(theta);

        // randomness should be a number between 0 and 1
        Double randomness = 0.3;

        Vector mixedDirection = flippedDirection.scale(1 - randomness).add(randomDirection.scale(randomness));

        return new Move(flippedDirection, mixedDirection.scale(maxSprint));    }

    public Move moveAway(){

        // need condition on this value being null
        Vector guardDirection = findFirstGuardDirection();

        if(guardDirection == null){
            guardInView = false;
            return zeroMove;
        }

        Vector flippedDirection = guardDirection.rotate(180);

        return new Move(flippedDirection, flippedDirection.scale(maxSprint));
    }

    private Vector findFirstGuardDirection(){
        // we dont have a view range yet, just a viewcone for now

        for(Ray ray: view)
        {
            if(ray.getType() == Type.GUARD){
                return ray.direction();
            }
        }
        return null;
    }

    @Override public Agent nextState()
    {
        if(guardInView){
            return this;
        }

        return new WallFollowAgent(position, direction, radius, type);
    }
}
