package jgfx.javagradlefx;

import app.controller.FileParser;
import app.controller.Settings;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.furniture.Furniture;
import app.model.map.Map;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapTest {

    @Test
    void setSpeedsAgentsInConstructionWithSettings()
    {
        Settings s = FileParser.readGameFile("src/test/java/jgfx/javagradlefx/mytest.txt");
        Map map = new Map(s);
        ArrayList<Agent> agents = map.getAgents();
        double maxWalk = s.getWalkSpeedGuard();
        double maxSprint= s.getSprintSpeedGuard();
        for(Agent a: agents)
        {
            assertEquals(maxWalk, a.getMaxWalk());
            assertEquals(maxSprint, a.getMaxSprint());
        }
    }
}
