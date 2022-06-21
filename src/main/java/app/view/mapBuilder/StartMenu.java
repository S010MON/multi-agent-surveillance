package app.view.mapBuilder;

import app.App;
import app.controller.io.FileManager;
import app.controller.settings.RandomSettingsGenerator;
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
        if(furniturePane.checkSettings())
        {
            Settings settings = settingsPane.getSettings();
            furniturePane.getSettings(settings);
            String fileName = Alert.answerAlert("Name of File", "Enter the Name of the file you wish to create.");
            settings.setName(fileName);
            FileManager.saveSettings(settings, fileName);
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
            Settings settings = settingsPane.getSettings();
            furniturePane.getSettings(settings);
            String fileName = Alert.answerAlert("Name of File", "Enter the Name of the file you wish to create.");
            settings.setName(fileName);
            FileManager.saveSettings(settings, fileName);
            app.gotoSimulation(fileName);
        }
        else
        {
            Alert.displayAlert("Error", "Map cannot be created as you are missing guard spawn, intruder spawn, or both!");
        }
    }

    public void openRandom()
    {
        RandomSettingsGenerator.clearRandomGenerator();
        Settings randomSettings = RandomSettingsGenerator.generateRandomSettings();
        app.gotoSimulation(randomSettings);
    }
}
