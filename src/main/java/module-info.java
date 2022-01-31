module jgfx.javagradlefx {
    requires javafx.controls;
    requires javafx.fxml;


    opens jgfx.javagradlefx to javafx.fxml;
    exports jgfx.javagradlefx;
}