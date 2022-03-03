package app.controller.soundEngine;

import app.model.Map;
import app.model.agents.Agent;

import java.util.ArrayList;
import java.util.HashMap;

public interface SoundEngine {
    double compute(Map map, Agent agent);
    HashMap<SoundSource, Sound> computeSources(Map map, Agent agent);
}
