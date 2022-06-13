package app.model.agents;

import app.model.Type;
import app.model.agents.ACO.*;
import app.model.agents.Capture.BaselineCaptureAgent;
import app.model.agents.Capture.CaptureAgent;
import app.model.agents.Capture.DijkstraCaptureAgent;
import app.model.agents.Evasion.*;
import app.model.agents.WallFollow.WFHighDirHeuristic;
import app.model.agents.WallFollow.WFMedDirHeuristic;
import app.model.agents.WallFollow.WallFollowAgent;
import lombok.Getter;
import lombok.Setter;

public class StateTable
{
    @Setter private static AgentType defaultCaptureAgent = AgentType.CAPTURE;
    @Setter private static  AgentType defaultEvasionAgent = AgentType.EVASION_RANDOM;
    @Getter private static AgentType defaultAcoAgent = AgentType.ACO_MOMENTUM;
    @Getter private static AgentType defaultWFAgent = AgentType.WALL_FOLLOW_MED_DIR_HEURISTIC;

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

    public static Agent captureTableSearch(Agent currentState)
    {
        switch(defaultCaptureAgent)
        {
            case CAPTURE_BASELINE -> {return new BaselineCaptureAgent(currentState);}
            case CAPTURE -> {return new CaptureAgent(currentState);}
            case CAPTURE_DIJKSTRA -> {return new DijkstraCaptureAgent(currentState);}
            default -> throw new RuntimeException("Capture agent not specified");
        }
    }

    public static Agent evasionTableSearch(Agent currentState)
    {
        switch(defaultEvasionAgent)
        {
            case EVASION_RANDOM -> {return new EvasionRandom(currentState);}
            case EVASION_DIRECTED -> {return new EvasionDirected(currentState);}
            case EVASION_DISTANCE_MAX -> {return new EvasionDistanceMax(currentState);}
            case EVASION_HIDEY -> {return new HideyAgent(currentState);}
            case EVASION_RUNAWAY -> {return new RunAwayAgent(currentState);}
            case EVASION_INTELLIGENT -> {return new IntelligentEvasionAgent(currentState);}
            default -> throw new RuntimeException("Evasion agent not specified");
        }
    }

    public static Agent acoTableSearch(Agent currentState)
    {
        switch(defaultAcoAgent)
        {
            case ACO -> {return new AcoAgent(currentState);}
            case ACO_COLONY -> {return new AcoColony(currentState);}
            case ACO_MOMENTUM -> {return new AcoMomentum(currentState);}
            case ACO_MIDI -> {return new AcoMid(currentState);}
            case ACO_RANKING -> {return new AcoRanking(currentState);}
            case ACO_MOMENTUM_SPIRAL_AVOIDANCE -> {return new AcoMomentumSpiralAvoidance(currentState);}
            default -> throw new RuntimeException("ACO agent not specified");
        }
    }

    public static Agent wfTableSearch(Agent currentState)
    {
        switch(defaultWFAgent)
        {
            case WALL_FOLLOW -> {return new WallFollowAgent(currentState);}
            case WALL_FOLLOW_HIGH_DIR_HEURISTIC -> {return new WFHighDirHeuristic(currentState);}
            case WALL_FOLLOW_MED_DIR_HEURISTIC -> {return new WFMedDirHeuristic(currentState);}
            default -> throw new RuntimeException("WF agent not specified");
        }
    }

    public static String getDefaultCaptureAgent()
    {
        return defaultCaptureAgent.name() + " ";
    }

    public static String getDefaultEvasionAgent()
    {
        return defaultEvasionAgent.name();
    }
}
