package app.controller.graphicsEngine;

import app.controller.Beam;
import app.model.agents.Agent;
import app.model.Map;

import java.util.ArrayList;

public interface GraphicsEngine
{
    ArrayList<Beam> compute(Map map, Agent agent);
}
