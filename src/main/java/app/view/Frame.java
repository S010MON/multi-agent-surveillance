package app.view;

import app.controller.FileParser;
import app.controller.GameEngine;
import app.controller.Settings;
import app.model.Map;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class Frame extends BorderPane
{
    private GameEngine gameEngine;

    public Frame(int width, int height)
    {
        Settings settings = FileParser.readGameFile("src/main/resources/map_1.txt");
        Map map = new Map(settings);
        Renderer renderer = new Renderer(map, width, height);
        gameEngine = new GameEngine(map, renderer);
        this.setCenter(renderer);
    }

    public void handleKey(KeyEvent e)
    {
        gameEngine.handleKey(e);
    }
}
