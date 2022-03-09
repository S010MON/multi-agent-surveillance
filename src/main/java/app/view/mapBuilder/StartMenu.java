package app.view.mapBuilder;

import app.App;
import app.controller.io.FileManager;
import app.controller.settings.Settings;
import app.view.Alert;
import app.view.FileMenuBar;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

public class StartMenu extends BorderPane
{
    @Getter private App app;
    @Getter private FurniturePane furniturePane;
    @Getter private DisplayPane displayPane;
    @Getter private SettingsPane settingsPane;

    public StartMenu(App app)
    {
        this.app = app;
        settingsPane = new SettingsPane(this);
        displayPane = new DisplayPane(this);
        furniturePane = new FurniturePane(this, displayPane);

        this.setTop(new FileMenuBar(app));
        this.setLeft(furniturePane);
        this.setRight(settingsPane);
        this.setCenter(displayPane);
    }

    public int getMapWidth()
    {
        return settingsPane.getMapWidth();
    }

    public int getMapHeight()
    {
        return settingsPane.getMapHeight();
    }

    public void saveSettings()
    {
        Settings settings = settingsPane.getSettings();
        furniturePane.getSettings(settings);
        FileManager.saveSettings(settings, settings.getName());
        Alert.displayAlert("Done!", "The map file was successfully created.");
    }

    public void saveSettingsAndOpen()
    {
        Settings settings = settingsPane.getSettings();
        furniturePane.getSettings(settings);
        FileManager.saveSettings(settings, settings.getName());
        String fileName = settings.getName();
        app.gotoSimulation(fileName);
    }
}
