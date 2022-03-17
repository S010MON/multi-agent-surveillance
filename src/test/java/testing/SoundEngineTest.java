package testing;

import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.controller.soundEngine.*;
import app.model.Map;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.furniture.FurnitureType;
import app.model.soundSource.SoundSource;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class SoundEngineTest {

    @Test void directSoundTest(){
        Settings settings = new Settings();
        settings.setHeight(400);
        settings.setWidth(300);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.INTRUDER_SPAWN);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.GUARD_SPAWN);
        settings.addSoundSource(new Vector(0,0), 50);
        settings.addSoundSource(new Vector(0,0), 100);

        Map map = new Map(settings);

        SoundSource s1 = map.getSoundSources().get(0);
        SoundSource s2 = map.getSoundSources().get(1);

        Agent listener =  new AgentImp(new Vector(30,40), new Vector(), 1);

        SoundEngine cornerAimedTracing = new CornerAimedTracing();

        // 30**2 + 40**2 = 50**2, so we expect the ones with amp 100 to give a remaining amp of 50

        HashMap<SoundSource, Sound> soundHashMap = cornerAimedTracing.compute(map,listener);

        double maxAmp = Math.max(soundHashMap.get(s1).getAmplitude(), soundHashMap.get(s2).getAmplitude());

        Sound expectedSound = new Sound(s2, listener.getPosition(), s2.getPosition(), 0);

        assertEquals(50, maxAmp);
        assertEquals(expectedSound, soundHashMap.get(s2));
    }

    @Test void blockedSoundTest(){
        Settings settings = new Settings();
        settings.setHeight(400);
        settings.setWidth(300);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.INTRUDER_SPAWN);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.GUARD_SPAWN);
        settings.addSoundFurniture(new Rectangle2D(40, -40, 10, 160), FurnitureType.WALL);
        settings.addSoundSource(new Vector(0,0), 60);
        settings.addSoundSource(new Vector(60,80), 70);

        Map map = new Map(settings);

        SoundSource s1 = map.getSoundSources().get(0);
        SoundSource s2 = map.getSoundSources().get(1);

        Agent listener =  new AgentImp(new Vector(30,40), new Vector(), 1);

        SoundEngine cornerAimedTracing = new CornerAimedTracing();

        HashMap<SoundSource, Sound> soundHashMapBlocked = cornerAimedTracing.compute(map,listener);

        double maxAmpBlocked = soundHashMapBlocked.get(s1).getAmplitude();

        // we expect the weaker amplitude source to be heard because the other source should be blocked
        assertEquals(10, maxAmpBlocked);
        assertNull(soundHashMapBlocked.get(s2));

        Sound expectedSound = new Sound(s1, listener.getPosition(), s1.getPosition(), 0);
        assertEquals(expectedSound, soundHashMapBlocked.get(s1));
    }

    @Test void blockedSoundTestDiffraction(){
        Settings settings = new Settings();
        settings.setHeight(400);
        settings.setWidth(300);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.INTRUDER_SPAWN);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.GUARD_SPAWN);
        settings.addSoundFurniture(new Rectangle2D(1, 1, 1, 1), FurnitureType.WALL);
        settings.addSoundSource(new Vector(3,3), 80);

        Map map = new Map(settings);

        SoundSource source = map.getSoundSources().get(0);

        Agent listener =  new AgentImp(new Vector(0,0), new Vector(), 1);

        SoundEngine cornerAimedTracing = new CornerAimedTracing();

        HashMap<SoundSource, Sound> soundSourceSoundHashMap = cornerAimedTracing.compute(map,listener);

        Sound actualSound = soundSourceSoundHashMap.get(source);

        Sound expectedSound = new Sound(source, new Vector(0,0), new Vector(2,1), 1);

        assertEquals(expectedSound,actualSound);
    }
}
