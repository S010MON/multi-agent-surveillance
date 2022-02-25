package testing;

import app.controller.GameEngine;
import app.controller.io.FileManager;
import app.controller.io.FilePath;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.view.simulation.Renderer;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.fail;

public class GameEngineTeleportTest {

    Map map;
    GameEngine gameEngine;

    /** Create a GameEngine */
    @BeforeAll
    void createMapFile()
    {
        Settings s = new Settings();
        s.setGameMode(1);
        s.setHeight(200);
        s.setWidth(200);
        s.setNoOfGuards(4);
        s.setNoOfIntruders(0);
        s.setWalkSpeedGuard(10);
        s.setWalkSpeedIntruder(10);
        s.setSprintSpeedGuard(15);
        s.setSprintSpeedGuard(15);
        s.setTimeStep(1);
        s.setScaling(1);
        s.addTeleport(new Rectangle2D(10, 10, 10, 10), new Vector(50, 50), 0);

        map = new Map(s);
        Renderer renderer = new Renderer(map);
        gameEngine = new GameEngine(map, renderer);
    }

    void TestTeleport()
    {

    }

}
