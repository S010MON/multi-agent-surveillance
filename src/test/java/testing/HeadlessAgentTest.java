package testing;

import experiments.TestingEngine;
import app.controller.io.FileManager;
import app.controller.logging.Logger;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.agents.AgentType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class HeadlessAgentTest
{
    private void runOnce(String map_name, AgentType typeIntruder, AgentType typeGuard)
    {
        String map_path = "src/main/resources/";

        System.out.println("Loading map: " + map_name);
        Settings settings = FileManager.loadSettings(map_path + map_name);

        Logger logger = new Logger(map_name);
        logger.setOutputCsv();

        settings.setGuardType(typeGuard);
        settings.setIntruderType(typeIntruder);

        System.out.println(typeGuard + " - " + settings.getNoOfGuards() + " guard agents ");
        System.out.println(typeIntruder + " - " + settings.getNoOfIntruders() + " intruder agents ");

        Map map = new Map((settings));
        TestingEngine gameEngine = new TestingEngine(map, typeIntruder + " " + typeGuard);
        int[] data = gameEngine.runCoverageTest();
        logger.log(typeIntruder + " " + typeGuard + " test", data);
    }

    // This doesn't even test anything?
    // and it takes way too long, 100000 tics is almost 3 hours worth of time (10 tics = 1 sec)
    @Disabled
    @Test public void ACOWFTest()
    {
        // intruder ACO, guard WF
        runOnce("experiment_map_1", AgentType.ACO, AgentType.WALL_FOLLOW);
    }
}
