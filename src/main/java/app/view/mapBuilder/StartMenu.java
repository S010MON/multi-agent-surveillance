package app.view.mapBuilder;

import app.App;
import app.controller.io.FileManager;
import app.controller.settings.Settings;
import app.controller.settings.SettingsGenerator;
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
    private Settings settings;

    public StartMenu(App app)
    {
        this.app = app;
        settings = SettingsGenerator.mockSettings();
        settingsPane = new SettingsPane(this, settings);
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
        if(furniturePane.checkSettings())
        {
            furniturePane.getSettings(settings);
            settingsPane.getSettings();
            FileManager.saveSettings(settings, settings.getName());
            Alert.displayAlert("Done!", "The map file was successfully created.");
        }
        else
        {
            Alert.displayAlert("Error", "Map cannot be created as you are missing guard spawn, intruder spawn, or both!");
        }
    }

    public void saveSettingsAndOpen()
    {
        if(furniturePane.checkSettings())
        {
            furniturePane.getSettings(settings);
            settingsPane.getSettings();
            FileManager.saveSettings(settings, settings.getName());
            String fileName = settings.getName();
            app.gotoSimulation(fileName);
        }
        else
        {
            Alert.displayAlert("Error", "Map cannot be created as you are missing guard spawn, intruder spawn, or both!");
        }
    }
}
