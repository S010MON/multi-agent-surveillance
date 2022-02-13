package app.view;

import app.model.furniture.FurnitureType;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SideMenu extends StackPane
{
    public SideMenu(int width, int height)
    {
        this.setWidth(width);
        this.setHeight(height);
        VBox vbox = new VBox(10);
        loadButtons(vbox);
        this.setMargin(vbox, new Insets(10, 10, 10, 10));
        this.getChildren().addAll(vbox);
    }

    public void loadButtons(VBox vbox)
    {
        // Spawn areas
        Label spawns = new Label("Spwan Areas:");
        Button aSpawn = new Button("Agent Spawn Area");
        Button iSpawn = new Button("Intruder Spawn Area");
        vbox.getChildren().addAll(spawns, aSpawn, iSpawn);

        // Furniture type enums
        Label furniture = new Label("Furniture Items:");
        vbox.getChildren().add(furniture);
        for(FurnitureType ft : FurnitureType.values())
        {
            Button furnType = new Button(""+ft);
            vbox.getChildren().add(furnType);
        }

        // Functionality buttons
        Label func = new Label("Create your Map:");
        Button create = new Button("Create");
        Button crOpen = new Button("Create & Open");

        vbox.getChildren().addAll(func, create, crOpen);
    }
}
