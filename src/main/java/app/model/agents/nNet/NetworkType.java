package app.model.agents.nNet;

public enum NetworkType
{
    TEST,
    FULL;

    public static String getPath(NetworkType type)
    {
        return switch (type)
        {
            case TEST -> "nNet/test/";
            case FULL -> "nNet/full/";
        };
    }
}
