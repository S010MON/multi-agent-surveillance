package app.controller.soundEngine;

import app.model.Map;
import app.model.agents.Agent;
import app.model.soundSource.SoundSource;
import java.util.HashMap;

public interface SoundEngine
{
    HashMap<SoundSource, Sound> compute(Map map, Agent agent);
}
