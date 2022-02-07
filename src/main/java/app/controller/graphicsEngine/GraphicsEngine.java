package app.controller.graphicsEngine;

import app.controller.graphicsEngine.Ray;
import app.model.agents.Agent;
import app.model.Map;

import java.util.ArrayList;

public interface GraphicsEngine
{
    ArrayList<Ray> compute(Map map, Agent agent);
}
