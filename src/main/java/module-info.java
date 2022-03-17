module app
{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires lombok;
    requires org.jgrapht.core;
    opens app to javafx.fxml;
    exports app;
    exports app.model;
    opens app.model to javafx.fxml;
}