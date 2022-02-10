package app.controller;

public abstract class PathCreater {

    public static String getPathMap(String name)
    {
        return "src/main/resources/" + name;
    }
}
