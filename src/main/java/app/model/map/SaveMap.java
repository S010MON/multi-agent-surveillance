package app.model.map;

import java.io.File;

public class SaveMap {

    private final String FILE_PATH = "src/main/java/app/model/map/files/";

    public void saveMap(Map map) {
        String fileName = map.getSetting().getName() + ".txt";
        File mapFile = new File(FILE_PATH + fileName);
    }

}
