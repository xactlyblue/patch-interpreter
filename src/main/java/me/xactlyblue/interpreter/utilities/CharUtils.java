package me.xactlyblue.interpreter.utilities;

import java.util.ArrayList;
import java.util.List;

public class CharUtils {

    public static List<String> splitByLines(char[] charset) {
        List<String> lines = new ArrayList<>();
        String currentLine = "";

        for (char character : charset) {
            if (character == '\n') {
                lines.add(currentLine);
                currentLine = "";
            } else {
                currentLine += character;
            }
        }

        return lines;
    }

}
