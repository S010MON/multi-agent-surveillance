package testing;

import app.controller.io.FileManager;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.agents.Agent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapTest
{
    @Test
    void setSpeedsAgentsInConstructionWithSettings()
    {
        Settings s = FileManager.loadSettings("src/test/resources/speedAgentTestMap");
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
