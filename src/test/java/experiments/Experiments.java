package experiments;

import app.controller.io.FileManager;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.agents.AgentType;
import jogging.Logger;

import java.util.Arrays;

public class Experiments
{
    private static String map_path = "src/main/resources/";

    /**
     * Execute required experiment. Alter experiment parameters within each experiment method
     */
    public static void main(String[] args)
    {
        //runEvasion();
    }

    /**
     * Determine the amount of discrete time ticks for a set of agents to explore 85% of the specified map
     * @param map_name The map to be explored
     */
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

    /**
     * Experiment provides a 1 vs 1 | Capture vs Evasion situation within the thunder-dome.
     * Specify the default Capture and Evasion agents within the method to alter the pairings.
     * Experiment output is the number of ticks until the Evasion agent is captured
     */
    private static void runCapture()
    {
        StateTable.setDefaultCaptureAgent(AgentType.CAPTURE);
        StateTable.setDefaultEvasionAgent(AgentType.EVASION_RANDOM);

        final String testName = "Capture_Experiment_" +
                StateTable.getDefaultCaptureAgent() + "_" +
                StateTable.getDefaultEvasionAgent();
        final int iterations = 10;

        Logger logger = new Logger(testName);
        logger.setOutputCsv();

        for(int i = 0; i < iterations; i++)
        {
            String test_heading = "Iteration: " + i +  "/" + iterations + " ";
            System.out.println(test_heading);

            Map map = generateRandomMap();

            TestingEngine gameEngine = new TestingEngine(map, testName);
            int data = (int) gameEngine.runCaptureTest();
            logger.log(test_heading + data);
        }
    }

    /**
     * Experiment provides a 1 vs 1 | Capture vs Evasion situation within the thunder-dome.
     * Specify the default Capture and Evasion agents within the method to alter the pairings.
     * Experiment output is the number of ticks until the Capture agent has lost visual sighting of the Evading agent.
     */
    public static void runEvasion()
    {
        StateTable.setDefaultCaptureAgent(AgentType.CAPTURE);
        StateTable.setDefaultEvasionAgent(AgentType.EVASION_DIRECTED);

        final String testName = "Evasion_Experiment_" +
                StateTable.getDefaultCaptureAgent() + "_" +
                StateTable.getDefaultEvasionAgent();
        final int iterations = 100;

        Logger logger = new Logger(testName);
        logger.setOutputCsv();

        for(int i =0; i < iterations; i++)
        {
            String test_heading = "Iteration: " + iterations + ", ";
            System.out.println(test_heading);

            Map map = generateRandomMap();

            TestingEngine gameEngine = new TestingEngine(map, testName);
            int data = (int) gameEngine.runEvasionTest();
            logger.log(test_heading + data);
        }
    }

    /**
     * Experiment provides a 1 vs 1 | Capture vs Evasion situation within the thunder-dome.
     * Specify the default Capture and Evasion agents within the method to alter the pairings.
     * Experiment output is the number of ticks until the Evasion agent is captured
     */
    private static void runCapture()
    {
        StateTable.setDefaultCaptureAgent(AgentType.CAPTURE);
        StateTable.setDefaultEvasionAgent(AgentType.EVASION_DIRECTED);

        final String testName = "Capture_Experiment_" +
                StateTable.getDefaultCaptureAgent() + "_" +
                StateTable.getDefaultEvasionAgent();
        final int iterations = 100;

        Logger logger = new Logger(testName);
        logger.setOutputCsv();

        for(int i = 0; i < iterations; i++)
        {
            String test_heading = "Iteration: " + iterations + ", ";
            System.out.println(test_heading);

            Map map = generateRandomMap();

            TestingEngine gameEngine = new TestingEngine(map, testName);
            int data = (int) gameEngine.runCaptureTest();
            logger.log(test_heading + data);
        }
    }

    /**
     * Experiment provides a 1 vs 1 | Capture vs Evasion situation within the thunder-dome.
     * Specify the default Capture and Evasion agents within the method to alter the pairings.
     * Experiment output is the number of ticks until the Capture agent has lost visual sighting of the Evading agent.
     */
    public static void runEvasion()
    {
        StateTable.setDefaultCaptureAgent(AgentType.CAPTURE);
        StateTable.setDefaultEvasionAgent(AgentType.EVASION_DIRECTED);

        final String testName = "Evasion_Experiment_" +
                StateTable.getDefaultCaptureAgent() + "_" +
                StateTable.getDefaultEvasionAgent();
        final int iterations = 100;

        Logger logger = new Logger(testName);
        logger.setOutputCsv();

        for(int i =0; i < iterations; i++)
        {
            String test_heading = "Iteration: " + iterations + ", ";
            System.out.println(test_heading);

            Map map = generateRandomMap();

            TestingEngine gameEngine = new TestingEngine(map, testName);
            int data = (int) gameEngine.runEvasionTest();
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
        Settings settings = RandomSettingsGenerator.generateRandomSettings();
        settings.setNoOfGuards(1);
        settings.setNoOfIntruders(1);

        Map map = new Map(settings);
        return map;
    }
}
