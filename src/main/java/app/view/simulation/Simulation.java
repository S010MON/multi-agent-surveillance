package app.view.simulation;

import app.App;
import app.controller.io.FileManager;
import app.controller.GameEngine;
import app.controller.settings.Settings;
import app.model.Map;
import app.view.FileMenuBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class Simulation extends BorderPane
{
    private GameEngine gameEngine;

    public Simulation(App app)
    {
        Settings settings = FileManager.loadSettings("src/main/resources/map_1.txt");
        Map map = new Map(settings);
        Renderer renderer = new Renderer(map);
        gameEngine = new GameEngine(map, renderer);
        this.setCenter(renderer);
        this.setTop(new FileMenuBar(app));
    }

    public Simulation(App app, String fileName)
    {
        Settings settings = FileManager.loadSettings("src/main/resources/"+fileName);
        Map map = new Map(settings);
        Renderer renderer = new Renderer(map);
        gameEngine = new GameEngine(map, renderer);
        this.setCenter(renderer);
        this.setTop(new FileMenuBar(app));
    }

    public void handleKey(KeyEvent e)
    {
        gameEngine.handleKey(e);
    }
}
