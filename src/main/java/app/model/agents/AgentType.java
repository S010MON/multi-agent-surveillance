package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.Type;
import app.model.agents.ACO.*;
import app.model.agents.WallFollow.WFMedDirHeuristic;
import app.model.agents.WallFollow.WFHighDirHeuristic;
import app.model.agents.WallFollow.WallFollowAgent;

public enum AgentType
{
    ACO,
    ACO_MOMENTUM,
    ACO_MIDI,
    ACO_MOMENTUM_SPIRAL_AVOIDANCE,
    ACO_RANKING,
    ACO_COLONY,
    WALL_FOLLOW,
    WALL_FOLLOW_MED_DIR_HEURISTIC,
    WALL_FOLLOW_HIGH_DIR_HEURISTIC,
    RANDOM;

    public static Agent agentOf(AgentType agentType, Vector position, Vector direction, double radius, Type type)
    {
        switch(agentType)
        {
            case ACO ->
            {
                return new AcoAgent(position, direction, radius, type);
            }
            case ACO_MOMENTUM ->
            {
                return new AcoMomentum(position, direction, radius, type);
            }
            case ACO_MIDI ->
            {
                return new AcoMid(position, direction, radius, type);
            }
            case ACO_MOMENTUM_SPIRAL_AVOIDANCE ->
            {
                return new AcoMomentumSpiralAvoidance(position, direction, radius, type);
            }
            case ACO_RANKING ->
            {
                return new AcoRanking(position, direction, radius, type);
            }
            case ACO_COLONY ->
            {
                return new AcoColony(position, direction, radius, type);
            }
            case WALL_FOLLOW ->
            {
                return new WallFollowAgent(position, direction, radius, type, 20);
            }
            case WALL_FOLLOW_MED_DIR_HEURISTIC ->
            {
                return new WFMedDirHeuristic(position, direction, radius, type, 20);
            }
            case WALL_FOLLOW_HIGH_DIR_HEURISTIC ->
            {
                return new WFHighDirHeuristic(position, direction, radius, type, 20);
            }
            case RANDOM ->
            {
                return new AgentImp(position, direction, radius, type);
            }
            default -> throw new RuntimeException("Agent type not recognized");
        }
    }
}
