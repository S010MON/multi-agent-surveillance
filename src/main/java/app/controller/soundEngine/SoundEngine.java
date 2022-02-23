package app.controller.soundEngine;

import app.model.Map;
import app.model.agents.Agent;

public interface SoundEngine {
    double compute(Map map, Agent agent);
}
