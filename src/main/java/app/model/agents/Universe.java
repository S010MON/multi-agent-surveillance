package app.model.agents;

import java.util.HashMap;

public class Universe
{
    private static Universe instance;
    private HashMap<Team, MemoryGraph> hashMap;

    private Universe()
    {
        hashMap = new HashMap<>();
    }

    public static void init(Team team, int distance)
    {
        if(instance == null)
            instance = new Universe();

        if(!instance.hashMap.containsKey(team))
            instance.hashMap.put(team, new MemoryGraph(distance));
    }

    public static MemoryGraph getMemoryGraph(Team team)
    {
        return instance.hashMap.get(team);
    }
}
