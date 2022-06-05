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
    private Renderer renderer;
    private App app;

    public Simulation(App app)
    {
        Settings settings = FileManager.loadSettings("src/main/resources/map_1.txt");
        Map map = new Map(settings);
        renderer = new Renderer(map);
        gameEngine = new GameEngine(map, renderer);
        this.setCenter(renderer);
        this.setTop(new FileMenuBar(app));
        this.app = app;
    }

    public Simulation(App app, String fileName)
    {
        Settings settings = FileManager.loadSettings("src/main/resources/"+fileName);
        Map map = new Map(settings);
        renderer = new Renderer(map);
        gameEngine = new GameEngine(map, renderer);
        this.setCenter(renderer);
        this.setTop(new FileMenuBar(app));
    }

    public void handleKey(KeyEvent e)
    {
        gameEngine.handleKey(e);
    }

    public void pauseOrResume()
    {
        gameEngine.pausePlay();
    }

    public void updateRenderer(int toggle, boolean display)
    {
        switch(toggle)
        {
            case 1 -> renderer.setDisplayRay(display);
            case 2 -> renderer.setDisplaySound(display);
            case 3 -> renderer.setDisplayTrail(display);
            case 4 -> renderer.setDisplayMiniMaps(display);
            case 5 -> renderer.setDisplayAreas(display);
        }
    }
}
