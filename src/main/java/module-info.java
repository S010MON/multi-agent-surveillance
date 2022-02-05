module jgfx.javagradlefx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    opens app to javafx.fxml;
    exports app;
}