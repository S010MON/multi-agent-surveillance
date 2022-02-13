package app.view;

import app.model.furniture.FurnitureType;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
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
        Button aSpawn = new Button("Agent Spawn Area");
        Button iSpawn = new Button("Intruder Spawn Area");
        vbox.getChildren().addAll(aSpawn, iSpawn);

        // Furniture type enums
        for(FurnitureType ft : FurnitureType.values())
        {
            Button furnType = new Button(""+ft);
            vbox.getChildren().add(furnType);
        }

        // Functionality buttons
        Button create = new Button("Create");
        Button crOpen = new Button("Create & Open");

        vbox.getChildren().addAll(create, crOpen);
    }
}
