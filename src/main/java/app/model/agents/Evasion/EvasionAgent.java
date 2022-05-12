package app.model.agents.Evasion;

import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.AgentImp;

public class EvasionAgent extends AgentImp
{
    public EvasionAgent(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
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
}
