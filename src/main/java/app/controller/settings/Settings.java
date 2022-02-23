package app.controller.settings;

import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundBlockingBlock;
import app.controller.soundEngine.SoundFurniture;
import app.controller.soundEngine.SoundSource;
import app.controller.soundEngine.SoundSourceBase;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import java.util.ArrayList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private ArrayList<SettingsObject> furniture = new ArrayList<>();
    private ArrayList<SoundSource> soundSources = new ArrayList<>();
    private ArrayList<SoundFurniture> soundFurniture = new ArrayList<>();

    public void addFurniture(Rectangle2D rectangle, FurnitureType type)
    {
        furniture.add(new SettingsObject(rectangle, type));
    }

    public void addTeleport(Rectangle2D rectangle, Vector vector)
    {
        furniture.add(new SettingsObject(rectangle, vector));
    }

    public void addSoundSource(Vector position, double amplitude){
        soundSources.add(new SoundSourceBase(position, amplitude));
    }

    public void addSoundFurniture(Rectangle2D rectangle){
        soundFurniture.add(new SoundBlockingBlock(rectangle));
    }
}
