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
    private boolean guardInView = true;
    private double randomness = 0.3;

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
            case DIRECTED -> {
                return moveDirected();
            }
            case RANDOMDIRECTED -> {
                return  moveRandomDirected();
            }
            case RANDOM -> {
                return moveRandom();
            }
        }
        return null;
    }

    private Move moveRandom(){
        int theta = (int) (Math.random() * 360);
        Vector randomDirection = new Vector(0, 1).rotate(theta);

        return new Move(direction, randomDirection.scale(maxSprint));
    }

    private Move moveRandomDirected(){
        // randomness should be a number between 0 and 1


        int theta = (int) (Math.random() * 360);
        Vector randomDirection = new Vector(0, 1).rotate(theta);

        Vector guardDirection = findFirstGuardDirection();

        if(guardDirection == null){
            // TODO this should flip to a new state
            //guardInView = false;
            return new Move(randomDirection, randomDirection.scale(maxSprint));
        }

        Vector flippedDirection = guardDirection.rotate(180);

        Vector mixedDirection = flippedDirection.scale(1 - randomness).add(randomDirection.scale(randomness));

        return new Move(guardDirection, mixedDirection.scale(maxSprint));    }

    public Move moveDirected(){

        // need condition on this value being null
        Vector guardDirection = findFirstGuardDirection();

        if(guardDirection == null){
            // TODO this should flip to a new state
            // guardInView = false;
            return new Move(direction, new Vector());
        }

        Vector flippedDirection = guardDirection.rotate(180);

        // Keep looking back to keep the agent in view

        return new Move(guardDirection, flippedDirection.scale(maxSprint));
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
