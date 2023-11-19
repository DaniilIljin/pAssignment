package helpers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static MatchResultType findMatchResultType(String neededType){
        for (MatchResultType e : MatchResultType.values()) {
            if (e.name().equals(neededType.toUpperCase().trim())){
                return e;
            }
        }
        throw new RuntimeException("No such type of result");
    }

    public static PlayerMoveType findPlayerMoveType(String neededMove) {
        for (PlayerMoveType e : PlayerMoveType.values()) {
            if (e.name().equals(neededMove.toUpperCase().trim())){
                return e;
            }
        }
        throw new RuntimeException("No such type of move");
    }

    public static void addLinesToResultString(StringBuilder stringBuilder, List<String> lines){
        if (lines.isEmpty()) stringBuilder.append("\n");
        for (String line : lines) {
            stringBuilder.append(line);
        }
        stringBuilder.append("\n");
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
            Files.writeString(Path.of(outputPath), content, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
