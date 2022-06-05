package experiments;

import app.controller.TestingEngine;
import app.controller.io.FileManager;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.agents.AgentType;
import jogging.Logger;

import java.util.Arrays;

public class Experiments
{
    private static final int iterations = 1;
    private static final int[] no_of_agents = {1, 2, 4, 6 ,5, 10};
    private static final AgentType[] agents = {AgentType.RANDOM, AgentType.ACO_MOMENTUM,
            AgentType.ACO_MOMENTUM_SPIRAL_AVOIDANCE, AgentType.WALL_FOLLOW, AgentType.WALL_FOLLOW_DIR_HEURISTIC};
    /**
     * Enter a map name and run the Experiments file to run every agent through the map 100 times
     */
    public static void main(String[] args)
    {
        run("experiment_map_1");
    }

    private static void run(String map_name)
    {
        String map_path = "src/main/resources/";

        System.out.println("Loading map: " + map_name);
        Settings settings = FileManager.loadSettings(map_path + map_name);
        settings.setNoOfIntruders(0);

        Logger logger = new Logger(map_name);
        logger.setOutputCsv();

        for(AgentType agent_under_test: agents)
        {
            settings.setGuardType(agent_under_test);

            for(int n: no_of_agents)
            {
                String agent_heading = agent_under_test + " - " + n + " agents ";
                System.out.println("Agent Type: " + agent_heading);

                for(int i = 0; i < iterations; i++)
                {
                    String test_name = "Iteration: " + i + "/" + iterations + " ";

                    Map map = new Map((settings));
                    TestingEngine gameEngine = new TestingEngine(map, test_name);
                    int[] data = gameEngine.run();

                    StringBuilder sb = new StringBuilder(agent_heading + test_name);
                    Arrays.stream(data).forEach(e -> sb.append("," + e));
                    logger.log(sb.toString());
                }
            }
        }
    }
}
