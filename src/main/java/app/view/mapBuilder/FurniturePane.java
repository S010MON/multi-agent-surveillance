package app.view.mapBuilder;

import app.controller.settings.Settings;
import app.model.furniture.FurnitureType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.ArrayDeque;

public class FurniturePane extends StackPane
{
    @Getter private FurnitureType currentType;
    private TextField x;
    private TextField y;
    private StartMenu startMenu;
    private DisplayPane displayPane;
    private final int BUTTON_WIDTH = 140;

    public FurniturePane(StartMenu startMenu, DisplayPane displayPane)
    {
        this.startMenu = startMenu;
        this.displayPane = displayPane;
        this.currentType = FurnitureType.WALL;
        VBox vbox = new VBox(10);
        loadButtons(vbox);
        this.setMargin(vbox, new Insets(10, 10, 10, 10));
        this.getChildren().addAll(vbox);
    }

    public void loadButtons(VBox vbox)
    {
        // Portal co-ordinates
        Label teleport = new Label("Teleport to x and y:");
        x = new TextField();
        x.setPrefWidth(BUTTON_WIDTH);
        y = new TextField();
        y.setPrefWidth(BUTTON_WIDTH);

        vbox.getChildren().addAll(teleport, x, y);

        // Furniture type enums
        Label furniture = new Label("Furniture Items:");
        vbox.getChildren().add(furniture);

        for(FurnitureType ft : FurnitureType.values())
        {
            Button furnType = new Button(""+ft);
            furnType.setOnAction(e -> handleActionEvent(e, ft));
            furnType.setPrefWidth(BUTTON_WIDTH);
            vbox.getChildren().add(furnType);
        }

        // Functionality buttons
        Label func = new Label("Create your Map:");
        Button create = new Button("Create");
        create.setPrefWidth(BUTTON_WIDTH);
        create.setOnAction(e -> startMenu.saveSettings()); // TODO add event handling!

        Button crOpen = new Button("Create & Open");
        crOpen.setPrefWidth(BUTTON_WIDTH);

        vbox.getChildren().addAll(func, create, crOpen);
    }

    private void handleActionEvent(ActionEvent e, FurnitureType type)
    {
        currentType = type;
    }

    public void getSettings(Settings s)
    {
        for(MbObject object : displayPane.getObjects())
        {
            if(object.getType() == FurnitureType.PORTAL)
                s.addTeleport(object.getRect(), object.getVector(), object.getRotation());
            else
                s.addFurniture(object.getRect(), object.getType());
        }
    }
}
