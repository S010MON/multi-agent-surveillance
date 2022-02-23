package app.model.furniture;

public enum FurnitureType
{
    WALL("wall"),
    SHADE("shaded"),
    GLASS("glass"),
    TOWER("tower"),
    PORTAL("teleport"),
    GUARD_SPAWN("spawnAreaGuards"),
    INTRUDER_SPAWN("spawnAreaIntruders"),
    TARGET("targetArea"),
    TEXTURE("texture");

    public final String label;

    FurnitureType(String label)
    {
        this.label = label;
    }
}
