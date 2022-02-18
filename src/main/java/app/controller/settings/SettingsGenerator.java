package app.controller.settings;

import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;

public abstract class SettingsGenerator
{
    public static Settings mockSettings()
    {
        Settings settings = new Settings();
        settings.setHeight(400);
        settings.setWidth(300);
        settings.addFurniture(new Rectangle2D(10, 10, 10, 10), FurnitureType.WALL);
        settings.addFurniture(new Rectangle2D(30, 10, 10, 10), FurnitureType.GLASS);
        settings.addFurniture(new Rectangle2D(10, 30, 10, 10), FurnitureType.TARGET);
        settings.addFurniture(new Rectangle2D(30, 30, 10, 10), FurnitureType.TOWER);
        return settings;
    }

    public static Settings saveSettingsTest()
    {
        Settings settings = new Settings();
        settings.setName("map_test");
        settings.setGameMode(1);
        settings.setHeight(2);
        settings.setWidth(3);
        settings.setScaling(4);
        settings.setNoOfGuards(5);
        settings.setNoOfIntruders(6);
        settings.setWalkSpeedIntruder(7);
        settings.setSprintSpeedIntruder(8);
        settings.setWalkSpeedGuard(9);
        settings.setSprintSpeedGuard(10);
        settings.setTimeStep(11);
        settings.addFurniture(new Rectangle2D(12, 13,14,15), FurnitureType.WALL);
        return settings;
    }

}
