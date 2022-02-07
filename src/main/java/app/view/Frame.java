package app.view;

import app.controller.GameEngine;
import app.model.map.Map;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class Frame extends BorderPane
{
    private GameEngine gameEngine;

    public Frame(int width, int height)
    {
        Map map = new Map();
        Renderer renderer = new Renderer(map, width, height);
        gameEngine = new GameEngine(map, renderer);
        this.setCenter(renderer);
    }

    public void handleKey(KeyEvent e)
    {
        gameEngine.handleKey(e);
    }
}
