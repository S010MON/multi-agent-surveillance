package testing;

import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.controller.settings.SettingsGenerator;
import app.controller.soundEngine.CornerAimedTracing;
import app.controller.soundEngine.SoundEngine;
import app.controller.soundEngine.SoundRay;
import app.model.Map;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SoundEngineTest {
    @Test void directSoundTest(){
        Settings settings = new Settings();
        settings.setHeight(400);
        settings.setWidth(300);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.INTRUDER_SPAWN);
        settings.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.GUARD_SPAWN);
        settings.addSoundSource(new Vector(0,0), 50);
        settings.addSoundSource(new Vector(0,0), 100);

        Agent listener =  new AgentImp(new Vector(30,40), new Vector(), 1);

        SoundEngine cornerAimedTracing = new CornerAimedTracing();

        // 30**2 + 40**2 = 50**2, so we expect the ones with amp 100 to give a remaining amp of 50

        assertEquals(50,cornerAimedTracing.compute(new Map(settings),listener));
    }
}
