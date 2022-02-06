package app.controller;

import java.util.Scanner;

public class FileParser
{
    private static Settings map;

    public static Settings readGameFile(String path)
    {
        map = new Settings();
        try(Scanner scan = new Scanner(path))
        {
            int countLines=1;
            while(scan.hasNextLine())
            {
                parseNextLine(scan.nextLine(), countLines);
                countLines++;
            }
        }
        catch(Exception e)
        {
            System.out.println("Failed creating scanner object using path supplied.");
        }
        return map;
    }

    public static void parseNextLine(String nextLine, int countLines)
    {
        try(Scanner scan = new Scanner(nextLine))
        {
            scan.useDelimiter("=");
            if(scan.hasNext())
            {
                String id=scan.next();
                String val=scan.next();
                id = id.trim();
                val = val.trim();
                String[] coords = val.split(" ");
                switch(id)
                {
                    case "gameMode":
                        map.setGamemode(Integer.parseInt(val));
                        break;
                    case "height":
                        map.setHeight(Integer.parseInt(val));
                        break;
                    case "width":
                        map.setWidth(Integer.parseInt(val));
                        break;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Line with exception: "+countLines);
            System.out.println("Content of line: "+nextLine);
        }
    }
}
