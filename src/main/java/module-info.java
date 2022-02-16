module app
{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires lombok;
    opens app to javafx.fxml;
    exports app;
}