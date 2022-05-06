package app.model.agents;

import java.util.HashMap;

public class Universe
{
    private Universe instance;
    private HashMap<Team, MemoryGraph> hashMap;

    private Universe()
    {
        hashMap = new HashMap<>();
    }

    public void init(Team team, int distance)
    {
        if(instance == null)
            instance = new Universe();

        if(!hashMap.containsKey(team))
            hashMap.put(team, new MemoryGraph(distance));
    }

    public MemoryGraph getMemoryGraph(Team team)
    {
        return hashMap.get(team);
    }
}
