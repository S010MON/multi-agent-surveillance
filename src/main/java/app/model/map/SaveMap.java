package app.model.map;

import javafx.util.converter.LocalTimeStringConverter;

import java.io.File;

public class SaveMap {

    private final String FILE_PATH = "src/main/java/resources/";

    public void saveMap(Map map) {

        String fileName = map.getSetting().getName();
        if(fileName== null)
            fileName = "Map_" + String.valueOf(Math.round(Math.random()*100000));
        File mapFile = new File(FILE_PATH + fileName + ".txt");
    }
}
