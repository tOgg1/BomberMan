package base;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tormod on 12.04.14.
 */
public class MapCreator {

    public static String config = "res/maps/config.txt";

    private final Factory factory;

    public static int MAP_NONE = 0;
    public static int MAP_PLAYER = 1;
    public static int MAP_AI = 2;
    public static int MAP_CRATE = 3;
    public static int MAP_METAL = 4;
    public static int MAP_TELEPORT = 5;

    public MapCreator(Factory factory) {
        this.factory = factory;
    }

    public boolean buildMap(String path){
        try{
            int[][] map = parseMap(path);
            for (int i = 0; i < map.length; i++) {
                java.lang.System.out.print("\n");
                for (int j = 0; j < map[i].length; j++) {
                    java.lang.System.out.print(map[i][j]);
                }
            }

        }catch(RuntimeException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int[][] parseMap(String path){
        int[][] map = null;
        try {

            // Load file and get content
            FileInputStream stream = new FileInputStream(path);

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder raw = new StringBuilder();

            String line = "";
            while((line = reader.readLine()) != null){
                raw.append(line.replaceAll(" ", "") +" ");
            }

            String rawString = raw.toString();

            // Parse the file
            java.lang.System.out.println(rawString.charAt(0));

            if(!rawString.matches("^s:\\d\\dx\\d\\d.*")){
                java.lang.System.out.println(rawString);
                throw new IllegalArgumentException("File header (e.g. s:16x16) is missing or malformatted");
            }

            Pattern pattern = Pattern.compile("^s:\\d\\dx\\d\\d");

            Matcher matcher = pattern.matcher(rawString);

            int sizex = 0, sizey = 0;

            if(matcher.find()){
                String header = matcher.group();

                header = header.replaceAll("^s:","");

                String[] sizes = header.split("x");
                java.lang.System.out.println(sizes[0]);
                java.lang.System.out.println(sizes[1]);
                sizex = Integer.parseInt(sizes[0]);
                sizey = Integer.parseInt(sizes[1]);
            }

            String[] lines = rawString.replaceAll("^s:\\d\\dx\\d\\d\\s", "").split(" ");

            if(!isWellFormatted(lines, sizex, sizey)){
                throw new IllegalArgumentException("Map found was not formatted correctly. Please see non-existing " +
                        "specification on how to format the maps");
            }

            map = new int[sizey][sizex];

            // Initialize map
            for (int i = 0; i < sizey; i++) {
                for (int j = 0; j < sizex; j++) {
                    map[i][j] = 0;
                }
            }

            // Fill map
            for (String s : lines) {


                int lineNumber = Integer.parseInt(""+s.charAt(0));
                int index = 0;

                s = s.replaceAll("^\\d:", "");

                String lastInt = "";
                boolean lastWasNumber = false;

                for (int i = 0; i < s.length(); i++) {

                    if(isNumber(""+s.charAt(i))) {
                        lastInt += s.charAt(i);
                    }else if(s.charAt(i) == ':') {
                        int newIndex = Integer.parseInt(lastInt)-1;
                        index = newIndex;
                        lastInt = "";
                    }else{
                        if(!lastInt.equals("")){
                            int count = Integer.parseInt(lastInt);

                            for (int j = 0; j < count; j++) {
                                map[lineNumber-1][index+j] = translateMapChar(s.charAt(i));
                            }

                            index += Integer.parseInt(lastInt);
                            lastInt = "";
                            lastWasNumber = true;
                        }else {
                            if (!lastWasNumber) {
                                map[lineNumber-1][index] = translateMapChar(s.charAt(i));
                                ++index;
                            } else {
                                lastWasNumber = false;
                                map[lineNumber-1][index] = translateMapChar(s.charAt(i));
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found: " + path);
        } catch(IOException e){
            throw new RuntimeException("Something went wrong while reading the file: " + e.getMessage());
        }
        return map;
    }

    public boolean isWellFormatted(String[] lines, int sizex, int sizey){
        // maxRowCount + 1 for the header
        if(lines.length > sizey)
            return false;

        for (String line : lines) {
            if(!line.matches("^[0-9]:[a-zA-Z0-9:]*")){
                java.lang.System.out.println("Here: " + line);
                return false;
            }

            if(!columnIsWellFormatted(line, sizex)){
                java.lang.System.out.println("Here2");
                return false;
            }
        }

        if(hasRowDuplicates(lines)){
            java.lang.System.out.println("Here3");
            return false;
        }

        return true;
    }

    public boolean columnIsWellFormatted(String line, int sizex){
        if(hasColumnDuplicates(line)){
            return false;
        }

        line = line.replaceAll("^\\d:", "");

        int index = 0;
        String lastInt = "";
        boolean lastWasNumber = false;

        for (int i = 0; i < line.length(); i++) {

            if(isNumber(""+line.charAt(i))) {
                lastInt += line.charAt(i);
            }else if(line.charAt(i) == ':') {
                int newIndex = Integer.parseInt(lastInt);
                if(newIndex < index){
                    return false;
                }
                index = newIndex;
                lastInt = "";
            }else{
                if(!lastInt.equals("")){
                    index += Integer.parseInt(lastInt);
                    lastInt = "";
                    lastWasNumber = true;
                }else {
                    if (!lastWasNumber) {
                        ++index;
                    } else {
                        lastWasNumber = false;
                    }
                }
            }
        }
        java.lang.System.out.println(line);
        java.lang.System.out.println(index);

        if(index > sizex)
            return false;

        return true;
    }

    public boolean hasColumnDuplicates(String line){
        boolean[] found;
        return false;
    }

    public boolean columnWidthIsNotLargerThan(String line, int width){
        return true;
    }

    /**
     * Assumes that isWellFormatted is already called. If this is not the case, a NumberFormatException
     * may be thrown.
     * @param lines
     * @return
     */
    public boolean hasRowDuplicates(String[] lines){
        boolean[] found;
        int max = 0;

        for (String line : lines) {
            int lineNumber = Integer.parseInt(""+line.charAt(0));
            max = lineNumber > max ? lineNumber : max;
        }

        found = new boolean[max];

        for (int i = 0; i < max; i++) {
            found[i] = false;
        }

        for (String line : lines) {
            int lineNumber = Integer.parseInt(""+line.charAt(0))-1;
            if(found[lineNumber])
                return true;
            found[lineNumber] = true;
        }

        return false;
    }

    public int translateMapChar(char c){
        switch(c){
            default:
            case 'b':
                return MAP_NONE;
            case 'c':
                return MAP_CRATE;
            case 'p':
                return MAP_PLAYER;
            case 't':
                return MAP_TELEPORT;
            case 'a':
                return MAP_AI;
            case 'm':
                return MAP_METAL;
        }
    }

    public boolean generateRandomMap(String mapName){
        return true;
    }

    public boolean isNumber(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    public static void main(String[] args) {
        MapCreator creator = new MapCreator(Factory.getInstance());
        creator.buildMap("res/maps/default.txt");
    }
}
