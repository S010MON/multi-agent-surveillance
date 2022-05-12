package app.model.agents.Evasion;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.AgentImp;

public class EvasionAgent extends AgentImp
{
    public EvasionStrategy strategy;

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
        direction = new Vector(0, 1).rotate(theta);
        Vector mov = new Vector(maxSprint, maxSprint).rotate(theta);

        return new Move(direction, mov);
    }

    private Move moveRandomDirected(){
        // TODO modify!
        return new Move(new Vector(), new Vector());
    }

    public Move moveAway(){
        // TODO modify!
        return new Move(new Vector(), new Vector());
    }

    private Vector findFirstGuardRay(){
        for(Ray ray: view)
        {
            if(ray.getType() == Type.GUARD){
                return ray.direction();
            }
        }
        return null;
    }


}
