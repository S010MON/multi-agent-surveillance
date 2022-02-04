package app.view;

import app.model.MapTemp;
import javafx.scene.layout.BorderPane;

public class Frame extends BorderPane
{
    public Frame(int width, int height)
    {
        MapTemp map = new MapTemp();
        Renderer renderer = new Renderer(map, width, height);
        this.setCenter(renderer);
    }
}
