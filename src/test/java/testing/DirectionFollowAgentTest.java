package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.linAlg.Vector;
import app.controller.settings.SettingsObject;
import app.model.Map;
import app.model.agents.DirectionFollowAgent.DirectionFollowAgent;
import app.model.agents.Team;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureFactory;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectionFollowAgentTest
{
    @Test public void noObstacleTest(){
        Vector initialPosition = new Vector(0, 0);
        Vector initialDirection = new Vector(0,1);

        DirectionFollowAgent agent = new DirectionFollowAgent(initialPosition, initialDirection, 1, Team.INTRUDER, new Vector(0,1));

        agent.move();
        assertEquals(DirectionFollowAgent.InternalState.goToTarget, agent.getInternalState());
    }

    @Test public void obstacleHit(){
        GraphicsEngine graphicsEngine = new GraphicsEngine(181);

        Vector initialPosition = new Vector(0, 0);
        Vector initialDirection = new Vector(0,1);

        DirectionFollowAgent agent = new DirectionFollowAgent(initialPosition, initialDirection, 1, Team.INTRUDER, new Vector(0,1));

        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(-10, 10, 20, 2);
        SettingsObject obj = new SettingsObject(obstacle, obstacleType);
        ArrayList<Furniture> walls = new ArrayList<>(List.of(FurnitureFactory.make(obj)));
        Map map = new Map(agent, walls);

        agent.updateView(graphicsEngine.compute(map,agent));
        agent.move();
        assertEquals(agent.getInternalState(), DirectionFollowAgent.InternalState.followWall);
    }
}
