package app.controller.settings;

import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;

public abstract class SettingsGenerator
{
    public static Settings mockSettings()
    {
        Settings settings = new Settings();
        settings.setName("best_Map");
        settings.setGameMode(1);
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setNoOfGuards(3);
        settings.setNoOfIntruders(5);
        settings.setWalkSpeedGuard(13.5);
        settings.setWalkSpeedIntruder(15.0);
        settings.setSprintSpeedGuard(21.5);
        settings.setSprintSpeedIntruder(23);
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

    public static Settings defaultSettings()
    {
        Settings settings = new Settings();
        settings.setName("default_map");
        settings.setGameMode(1);
        settings.setWidth(1000);
        settings.setHeight(1000);
        settings.setNoOfGuards(0);
        settings.setNoOfIntruders(0);
        settings.setWalkSpeedGuard(13.5);
        settings.setWalkSpeedIntruder(15.0);
        settings.setSprintSpeedGuard(21.5);
        settings.setSprintSpeedIntruder(23);
        return settings;
    }

    public static Settings coverageMapTest(int width, int height)
    {
        Settings settings = new Settings();
        settings.setName("map_test");
        settings.setGameMode(1);
        settings.setHeight(height);
        settings.setWidth(width);
        settings.setScaling(4);
        settings.setNoOfGuards(5);
        settings.setNoOfIntruders(6);
        settings.setWalkSpeedIntruder(7);
        settings.setSprintSpeedIntruder(8);
        settings.setWalkSpeedGuard(9);
        settings.setSprintSpeedGuard(10);
        settings.setTimeStep(11);
        settings.addFurniture(new Rectangle2D(10, 10,100,100), FurnitureType.GUARD_SPAWN);
        settings.addFurniture(new Rectangle2D(10, 10,100,100), FurnitureType.INTRUDER_SPAWN);

        return settings;
    }

    public static Settings mapTest()
    {
        Settings settings = new Settings();
        settings.setName("furniture_type_test");
        settings.setGameMode(1);
        settings.setHeight(100);
        settings.setWidth(100);
        settings.setNoOfGuards(0);
        settings.setNoOfIntruders(0);
        settings.addFurniture(new Rectangle2D(90, 90,100,100), FurnitureType.GUARD_SPAWN);
        settings.addFurniture(new Rectangle2D(90, 90,100,100), FurnitureType.INTRUDER_SPAWN);
        settings.addFurniture(new Rectangle2D(50, 50,50,50), FurnitureType.WALL);
        return settings;
    }
}
