package helpers;
import main.Main;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Helper {
    public static MatchResultType findMatchResultType(String neededType){
        for (MatchResultType e : MatchResultType.values()) {
            if (e.getStringValue().equals(neededType)){
                return e;
            }
        }
        throw new RuntimeException("No such type of result");
    }

    public static PlayerMoveType findPlayerMoveType(String neededMove) {
        for (PlayerMoveType e : PlayerMoveType.values()) {
            if (e.getStringValue().equals(neededMove)){
                return e;
            }
        }
        throw new RuntimeException("No such type of move");
    }

    public static List<String> readFromFile(String filePath){
        ArrayList<String> list = new ArrayList<>();
        Path path = Paths.get(filePath);
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public static void createAndWriteToFile(String filePath, String content){
        try {
            String outputPath = filePath;
            Files.writeString(Path.of(outputPath), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
