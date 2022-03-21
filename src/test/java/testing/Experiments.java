package testing;

import app.controller.TestingEngine;
import app.controller.io.FileManager;
import app.controller.logging.Logger;
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
        int map_start_range = 1;
        int map_end_range = 5;

        for(int n = map_start_range; n < map_end_range; n++)
        {
            String map_path = "src/main/resources/";
            String map_name = "experiment_map_" + n;
            System.out.println("Loading map: " + map_name);
            Settings settings = FileManager.loadSettings(map_path + map_name);

            System.out.println("Agent Type: " + agent_under_test);
            settings.setGuardType(agent_under_test);
            settings.setNoOfGuards(no_of_agents);
            settings.setNoOfIntruders(0);

            Logger logger = new Logger(map_name);
            logger.setOutputCsv();

            for(int i = 0; i < iterations; i++)
            {
                Map map = new Map((settings));
                TestingEngine gameEngine = new TestingEngine(map);
                int[] data = gameEngine.run();
                logger.log(agent_under_test.name(), data);
            }
        }
    }
}
