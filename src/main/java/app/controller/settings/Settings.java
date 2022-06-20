package app.controller.settings;

import app.controller.linAlg.Vector;
import app.model.agents.AgentType;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Settings
{
    private String name;
    private int gameMode;
    private int height;
    private int width;
    private int noOfGuards;
    private int noOfIntruders;
    private double walkSpeedGuard;
    private double sprintSpeedGuard;
    private double walkSpeedIntruder;
    private double sprintSpeedIntruder;
    private double timeStep;
    private double scaling;
    private AgentType guardType = AgentType.ACO_MOMENTUM_SPIRAL_AVOIDANCE;
    private AgentType IntruderType = AgentType.WALL_FOLLOW_MED_DIR_HEURISTIC;
    private ArrayList<SettingsObject> furniture = new ArrayList<>();
    private ArrayList<SettingsObject> soundSources = new ArrayList<>();
    private ArrayList<SettingsObject> soundFurniture = new ArrayList<>();

    public void addFurniture(Rectangle2D rectangle, FurnitureType type)
    {
        furniture.add(new SettingsObject(rectangle, type));
    }

    public void addTeleport(Rectangle2D rectangle, Vector vector, double teleportRotation)
    {
        furniture.add(new SettingsObject(rectangle, vector, teleportRotation));
    }

    public void addSoundSource(Vector position, double amplitude)
    {
        SettingsObject soundSource = new SettingsObject(position, amplitude);
        soundSources.add(soundSource);
        furniture.add(soundSource);
    }
}
