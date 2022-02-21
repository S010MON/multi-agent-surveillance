package app.view;

import app.controller.io.FileManager;
import app.controller.GameEngine;
import app.controller.settings.Settings;
import app.model.Map;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class Frame extends BorderPane
{
    private GameEngine gameEngine;

    public Frame()
    {
        Settings settings = FileManager.loadSettings("src/main/resources/map_1.txt");
        Map map = new Map(settings);
        FileMenuBar menuBar = new FileMenuBar(this);
        Renderer renderer = new Renderer(map);
        gameEngine = new GameEngine(map, renderer);
        this.setCenter(renderer);
        this.setTop(menuBar);
    }

    public void handleKey(KeyEvent e)
    {
        gameEngine.handleKey(e);
    }
}
