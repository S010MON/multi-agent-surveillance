package app.view;

import app.controller.GameEngine;
import app.model.MapTemp;
import javafx.scene.layout.BorderPane;

public class Frame extends BorderPane
{
    public Frame(int width, int height)
    {
        MapTemp map = new MapTemp();
        Renderer renderer = new Renderer(map, width, height);
        GameEngine gameEngine = new GameEngine(map, renderer);
        this.setCenter(renderer);
    }
}
