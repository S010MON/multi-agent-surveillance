package app.model.agents;

import app.model.Type;

import java.util.HashMap;

public class Universe
{
    private static Universe instance;
    private HashMap<Type, MemoryGraph> hashMap;

    private Universe()
    {
        hashMap = new HashMap<>();
    }

    public static void init(Type team, int distance)
    {
        if(instance == null)
            instance = new Universe();

        if(!instance.hashMap.containsKey(team))
            instance.hashMap.put(team, new MemoryGraph(distance));
    }

    public static MemoryGraph getMemoryGraph(Type team)
    {
        return instance.hashMap.get(team);
    }

    public static void clearUniverse()
    {
        if(instance != null)
        {
            instance.hashMap.clear();
        }
    }

    public static void createPerfectUniverse(MemoryGraph perfectWorld)
    {
        if(instance == null)
            instance = new Universe();

        instance.hashMap.put(Type.GUARD, perfectWorld);
        instance.hashMap.put(Type.INTRUDER, perfectWorld);
    }
}
