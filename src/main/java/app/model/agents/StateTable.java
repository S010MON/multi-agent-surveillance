package app.model.agents;

import app.model.Type;
import app.model.agents.Capture.CaptureAgent;
import app.model.agents.Evasion.EvasionAgent;
import lombok.Getter;
import lombok.Setter;

public class StateTable
{
    @Setter private static AgentType defaultCaptureAgent = AgentType.CAPTURE;
    @Setter private static  AgentType defaultEvasionAgent = AgentType.EVASION_DIRECTED;

    /**
     * Encodes state changes for all types of agents, returning either the current class
     * or creates a new class of the required type for the desired next state
     * @param currentState The current agent state
     * @return The next state of the agent
     */
    public static Agent lookupState(Agent currentState)
    {
        //State GUARD
        if(currentState.getType() == Type.GUARD)
        {
            if(currentState.isTypeSeen(Type.INTRUDER))
                return captureTableSearch(currentState);
        }

        //State INTRUDER
        else if(currentState.getType() == Type.INTRUDER)
        {
            if(currentState.isTypeSeen(Type.GUARD))
                return evasionTableSearch(currentState);

            if(currentState.isTypeSeen(Type.TARGET))
                return new TargetAgent(currentState);

            return currentState;
        }
        return currentState;
    }

    private static Agent captureTableSearch(Agent currentState)
    {
        switch(defaultCaptureAgent)
        {
            case CAPTURE -> {return new CaptureAgent(currentState);}
            default -> throw new RuntimeException("Capture agent not specified");
        }
    }

    private static Agent evasionTableSearch(Agent currentState)
    {
        //TODO Modify table when Evasion Agents are separated.
        switch(defaultEvasionAgent)
        {
            case EVASION_RANDOM -> {return new EvasionAgent(currentState);}
            case EVASION_DIRECTED -> {return new EvasionAgent(currentState);}
            case EVASION_RANDOMDIRECTED -> {return new EvasionAgent(currentState);}
            default -> throw new RuntimeException("Evasion agent not specified");
        }
    }

    public static String getDefaultCaptureAgent()
    {
        return defaultEvasionAgent.name() + " ";
    }

    public static String getDefaultEvasionAgent()
    {
        return defaultEvasionAgent.name();
    }
}
