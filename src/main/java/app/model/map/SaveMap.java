package app.model.map;

import app.controller.FileParser;
import app.controller.Settings;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveMap {

    private final String FILE_PATH = "src/main/resources/saveMap/";

    public static void main(String[] args) {
        SaveMap saveMap = new SaveMap();
        Settings testSetting = FileParser.readGameFile("src/main/resources/map_1.txt");
        Map testMap = new Map(testSetting);
        saveMap.saveMap(testMap);
    }

    public void saveMap(Map map){
        try
        {
            Settings setting = map.getSetting();
            String fileName = setting.getName();
            if (fileName == null)
                fileName = "Map_" + String.valueOf(Math.round(Math.random() * 100000) + ".txt");
            else
                fileName = fileName + ".txt";
            System.out.println(FILE_PATH + fileName);


            File mapFile = new File(FILE_PATH + fileName);
            //File file = new File(getClass().getResource(fileName).getPath());

            FileWriter writer = new FileWriter(fileName);


            writeLine(writer, "name", setting.getName());
            writeLine(writer, "gameFile", FILE_PATH + fileName);
            writeLine(writer, "gameMode", Integer.toString(setting.getGamemode()));
            writeLine(writer, "height", Integer.toString(setting.getHeight()));
            writeLine(writer, "width", Integer.toString(setting.getWidth()));
//            writeLine(writer, "", );
//            writeLine(writer, "", );
//            writeLine(writer, "", );
//            writeLine(writer, "", );
//            writeLine(writer, "", );
//            writeLine(writer, "", );
            writer.close();
        }
        catch(Exception e)
        {
            System.out.println("An error occured while creating a map file.");
            e.printStackTrace();
        }

    }

    private void writeLine(FileWriter writer, String variable, String value) throws IOException {
        writer.write(variable + " = " + value + System.getProperty( "line.separator" ));
    }

}
