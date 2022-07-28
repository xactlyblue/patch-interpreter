package me.xactlyblue.interpreter.utilities;

import me.xactlyblue.interpreter.patches.datatypes.DeletedLine;

import java.util.HashMap;

public class FilterUtils {

    public static DeletedLine getDeletedLine(HashMap<Integer, DeletedLine> deletedLines, String line) {
        for (DeletedLine deletedLine : deletedLines.values()) {
            if (deletedLine.getLine().equals(line))
                return deletedLine;
        }

        return null;
    }

    public static boolean isLineDeleted(HashMap<Integer, DeletedLine> deletedLines, String line) {
        for (DeletedLine deletedLine : deletedLines.values()) {
            if (deletedLine.getLine().equals(line))
                return true;
        }

        return false;
    }

}
