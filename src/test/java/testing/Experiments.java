package testing;

import app.controller.GameEngineSansGui;
import app.controller.io.FileManager;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.agents.AgentType;

public class Experiments
{

    public static void main(String[] args)
    {
        AgentType agent_under_test = AgentType.ACO;
        int no_of_agents = 1;
        int iterations = 2;
        int map_start_range = 0;
        int map_end_range = 5;

        for(int n = map_start_range; n < map_end_range; n++)
        {
            String map_name = "src/main/resources/experiment_map_" + n;
            System.out.println("Loading map: " + map_name);
            Settings settings = FileManager.loadSettings(map_name);

            System.out.println("Agent Type: " + agent_under_test);
            settings.setGuardType(agent_under_test);
            settings.setNoOfGuards(no_of_agents);
            settings.setNoOfIntruders(0);

            for(int i = 0; i < iterations; i++)
            {
                Map map = new Map((settings));
                GameEngineSansGui gameEngine = new GameEngineSansGui(map);
                boolean complete = gameEngine.run();
            }
        }
    }
}
