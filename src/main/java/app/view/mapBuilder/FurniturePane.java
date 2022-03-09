package app.view.mapBuilder;

import app.controller.settings.Settings;
import app.model.furniture.FurnitureType;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class FurniturePane extends StackPane
{
    @Getter private FurnitureType currentType;
    @Getter private boolean portal = false;
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

    public void getSettings(Settings s)
    {
        for(MbObject object : displayPane.getObjects())
        {
            if(object.getType() == FurnitureType.PORTAL)
                s.addTeleport(object.getRect(), object.getTeleportTo(), object.getRotation());
            else
                s.addFurniture(object.getRect(), object.getType());
        }
    }

    public boolean checkSettings()
    {
        boolean guard = false;
        boolean intruder = false;
        for(MbObject object : displayPane.getObjects())
        {
            if(object.getType() == FurnitureType.GUARD_SPAWN)
                guard = true;

            if(object.getType() == FurnitureType.INTRUDER_SPAWN)
                intruder = true;
        }

        return guard && intruder;
    }

    private void loadButtons(VBox vbox)
    {
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
        create.setOnAction(e -> startMenu.saveSettings());

        Button crOpen = new Button("Create & Open");
        crOpen.setOnAction(e -> startMenu.saveSettingsAndOpen());
        crOpen.setPrefWidth(BUTTON_WIDTH);

        vbox.getChildren().addAll(func, create, crOpen);
    }

    private void handleActionEvent(ActionEvent e, FurnitureType type)
    {
        currentType = type;
    }
}
