module jgfx.javagradlefx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.jgrapht.core;
    opens app to javafx.fxml;
    exports app;
}