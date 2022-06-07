package experiments;

import app.controller.io.FileManager;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.agents.AgentType;
import app.model.agents.StateTable;
import jogging.Logger;

import java.util.Arrays;

public class Experiments
{
    private static String map_path = "src/main/resources/";

    /**
     * Enter a map name and run the Experiments file to run every agent through the map 100 times
     */
    public static void main(String[] args)
    {
        runInfiltration("experiment_map_1");
    }

    private static void runCoverage(String map_name)
    {
        final int iterations = 1;
        final int[] no_of_agents = {1, 2, 4, 6 ,5, 10};
        final AgentType[] agents = {AgentType.RANDOM,
                                    AgentType.ACO_MOMENTUM,
                                    AgentType.ACO_MOMENTUM_SPIRAL_AVOIDANCE,
                                    AgentType.WALL_FOLLOW,
                                    AgentType.WALL_FOLLOW_MED_DIR_HEURISTIC,
                                    AgentType.WALL_FOLLOW_HIGH_DIR_HEURISTIC};

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
                    int[] data = gameEngine.runCoverageTest();

                    StringBuilder sb = new StringBuilder(agent_heading + test_name);
                    Arrays.stream(data).forEach(e -> sb.append("," + e));
                    logger.log(sb.toString());
                }
            }
        }
    }

    @Deprecated
    private static void runCapture(String map_name)
    {
        final int iterations = 10;
        final int[] no_of_guards = {1, 2, 3, 4 ,5, 6};

        System.out.println("Loading map: " + map_name);
        Settings settings = FileManager.loadSettings(map_path + map_name);
        settings.setNoOfIntruders(1);

        Logger logger = new Logger(map_name);
        logger.setOutputCsv();

        for(int guards: no_of_guards)
        {
            settings.setNoOfGuards(guards);
            String agent_heading = "No of guards" + " - " + guards;
            System.out.println(agent_heading);

            for(int i = 0; i < iterations; i++)
            {
                String test_name = "Iteration: " + i + "/" + iterations + " ";

                Map map = new Map((settings));
                TestingEngine gameEngine = new TestingEngine(map, test_name);
                int data = (int) gameEngine.runCaptureTest();
                logger.log(agent_heading + test_name + "," + data);
            }
        }
    }

    private static void runCapture()
    {
        StateTable.setDefaultCaptureAgent(AgentType.CAPTURE);
        StateTable.setDefaultEvasionAgent(AgentType.EVASION_DIRECTED);

        final String testName = "Capture_Experiment, " + StateTable.getDefaultCaptureAgent() + ", " + StateTable.getDefaultEvasionAgent();
        final int iterations = 100;

        Logger logger = new Logger(testName);
        logger.setOutputCsv();

        for(int i = 0; i < iterations; i++)
        {
            String test_heading = "Iteration: " + iterations + ", ";

            Map map = generateRandomMap();

            TestingEngine gameEngine = new TestingEngine(map, testName);
            //TODO Modify capture test
            int data = (int) gameEngine.runCaptureTest();

            logger.log(test_heading + data);
        }

    }

    private static void runInfiltration(String map_name)
    {
        final int iterations = 10;
        final int[] no_of_intruders = {1, 2, 3, 4 ,5, 6};

        System.out.println("Loading map: " + map_name);
        Settings settings = FileManager.loadSettings(map_path + map_name);
        settings.setNoOfGuards(0);

        Logger logger = new Logger(map_name);
        logger.setOutputCsv();

        for(int intruders: no_of_intruders)
        {
            settings.setNoOfIntruders(intruders);
            String agent_heading = "No of intruders" + ":" + intruders;
            System.out.println(agent_heading);

            for(int i = 0; i < iterations; i++)
            {
                String test_name = "Iteration: " + i + "/" + iterations + " ";

                Map map = new Map((settings));
                TestingEngine gameEngine = new TestingEngine(map, test_name);
                int data = (int) gameEngine.runInflitrationTest();
                logger.log(agent_heading + "," + data);
            }
        }
    }

    private static Map generateRandomMap()
    {
        //TODO Generate random map settings object
        Settings settings = new Settings();
        settings.setNoOfGuards(1);
        settings.setNoOfIntruders(1);

        Map map = new Map(settings);
        return map;
    }
}
