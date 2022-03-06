package testing;

import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.controller.soundEngine.*;
import app.model.Map;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SoundEngineTest {

    @Test void directSoundTest(){
        Settings settings = new Settings();
        settings.setHeight(400);
        settings.setWidth(300);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.INTRUDER_SPAWN);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.GUARD_SPAWN);

        SoundSource s1 = new SoundSourceBase(new Vector(0,0), 50);
        SoundSource s2 = new SoundSourceBase(new Vector(0,0), 100);

        settings.getSoundSources().add(s1);
        settings.getSoundSources().add(s2);

        Agent listener =  new AgentImp(new Vector(30,40), new Vector(), 1);

        SoundEngine cornerAimedTracing = new CornerAimedTracing();

        // 30**2 + 40**2 = 50**2, so we expect the ones with amp 100 to give a remaining amp of 50

        HashMap<SoundSource, Sound> soundHashMap = cornerAimedTracing.compute(new Map(settings),listener);

        double maxAmp = Math.max(soundHashMap.get(s1).getAmplitude(), soundHashMap.get(s2).getAmplitude());

        assertEquals(50, maxAmp);
    }

    @Test void blockedSoundTest(){
        Settings settings = new Settings();
        settings.setHeight(400);
        settings.setWidth(300);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.INTRUDER_SPAWN);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.GUARD_SPAWN);

        SoundSource s1 = new SoundSourceBase(new Vector(0,0), 60);
        SoundSource s2 = new SoundSourceBase(new Vector(60,80), 70);

        settings.getSoundSources().add(s1);
        settings.getSoundSources().add(s2);

        settings.addSoundFurniture(new Rectangle2D(40, -40, 10, 160));

        Agent listener =  new AgentImp(new Vector(30,40), new Vector(), 1);

        SoundEngine cornerAimedTracing = new CornerAimedTracing();

        HashMap<SoundSource, Sound> soundHashMapBlocked = cornerAimedTracing.compute(new Map(settings),listener);

        double maxAmpBlocked = soundHashMapBlocked.get(s1).getAmplitude();

        // we expect the weaker amplitude source to be heard because the other source should be blocked
        assertEquals(10, maxAmpBlocked);
        assertNull(soundHashMapBlocked.get(s2));
    }

    @Test void blockedSoundTestDiffraction(){
        Settings settings = new Settings();
        settings.setHeight(400);
        settings.setWidth(300);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.INTRUDER_SPAWN);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.GUARD_SPAWN);

        SoundSource source = new SoundSourceBase(new Vector(3,3), 80);

        settings.getSoundSources().add(source);

        Agent listener =  new AgentImp(new Vector(0,0), new Vector(), 1);

        SoundEngine cornerAimedTracing = new CornerAimedTracing();

        settings.addSoundFurniture(new Rectangle2D(1, 1, 1, 1));

        HashMap<SoundSource, Sound> soundSourceSoundHashMap = cornerAimedTracing.compute(new Map(settings),listener);

        Sound actualSound = soundSourceSoundHashMap.get(source);

        Sound expectedSound = new Sound(source, new Vector(0,0), new Vector(1,2), 1);

        assertEquals(expectedSound,actualSound);
    }
}
