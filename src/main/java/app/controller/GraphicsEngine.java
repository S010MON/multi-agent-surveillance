package app.controller;

import app.model.Agent;
import app.model.MapTemp;

import java.util.ArrayList;

public class GraphicsEngine
{
    private static GraphicsEngine instance;

    public static GraphicsEngine getInstance()
    {
        if(instance == null)
            instance = new GraphicsEngine();
        return instance;
    }

    private GraphicsEngine()
    {
        // Singleton
    }

    public ArrayList<Beam> compute(MapTemp map, Agent agent)
    {
        return null;
    }
}
