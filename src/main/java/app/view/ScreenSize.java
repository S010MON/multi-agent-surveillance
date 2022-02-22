package app.view;

import javafx.stage.Screen;

public abstract class ScreenSize
{
    public static final double height = Screen.getPrimary().getBounds().getHeight();
    public static final double width = Screen.getPrimary().getBounds().getWidth();
}
