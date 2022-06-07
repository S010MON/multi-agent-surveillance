package app.model.agents;

import app.model.Type;
import app.model.agents.Capture.CaptureAgent;
import app.model.agents.Evasion.EvasionAgent;
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
                return new CaptureAgent(currentState);
        }

        //State INTRUDER
        else if(currentState.getType() == Type.INTRUDER)
        {
            if(currentState.isTypeSeen(Type.GUARD))
                return new EvasionAgent(currentState);

            if(currentState.isTypeSeen(Type.TARGET))
                return new TargetAgent(currentState);

            return currentState;
        }
        return currentState;
    }
}
