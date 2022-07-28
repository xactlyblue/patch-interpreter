package me.xactlyblue.interpreter.patches.interpreters.impl;

import me.xactlyblue.interpreter.patches.datatypes.DeletedLine;
import me.xactlyblue.interpreter.patches.interpreters.DeletedPatchInterpreter;
import me.xactlyblue.interpreter.patches.interpreters.PatchInterpreter;

import java.awt.*;
import java.beans.PropertyEditorManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhitespaceInterpreter implements PatchInterpreter {

    /*
     * This isn't refactored yet and is pretty messy since I was, for the most part, just
     * experimenting with solutions; so... uh... sorry :D
     */

    private int totalLines = 0;
    private final HashMap<Integer, String> addedLines = new HashMap<>();
    private final HashMap<Integer, String> deletedLines = new HashMap<>();
    private final HashMap<Integer, String> neutralLines = new HashMap<>();

    @Override
    public void interpret(int number, String line) {
        if (line.startsWith("+") && !line.startsWith("+++")) {
            addedLines.put(number, line);
        } else if (line.startsWith("-") && !line.startsWith("---")) {
            deletedLines.put(number, line);
        } else {
            neutralLines.put(number, line);
        }

        totalLines++;
    }

    public List<String> reformatLinesWithoutWhitespace() {
        HashMap<Integer, String> reformatted = new HashMap<>();
        HashMap<Integer, String> allowedDeletedLines = new HashMap<>();

        for (int index = 0; index < totalLines; index++) {
            String neutralLine = neutralLines.get(index);
            String addedLine = addedLines.get(index);
            String deletedLine = deletedLines.get(index);

            if (neutralLine != null) {
                reformatted.put(index, neutralLine);
            } else if (addedLine != null) {
                int deletedCounterpartNumber = -1;
                String deletedCounterpartLine = null;

                String neutralEntryAddedLine = addedLine.replaceFirst("[+]", "\u0020")
                        .replaceAll("\u0009", "")
                        .replaceAll(" ", "");

                for (Map.Entry<Integer, String> entryDeletedLine : allowedDeletedLines.entrySet()) {
                    String neutralEntryDeletedLine = entryDeletedLine.getValue().replaceFirst("[-]", "\u0020")
                            .replaceAll("\u0009", "")
                            .replaceAll("\u0020", "");

                    if (neutralEntryAddedLine.equals(neutralEntryDeletedLine)) {
                        // It's a match, the only difference is whitespaces! :tada:
                        deletedCounterpartNumber = entryDeletedLine.getKey();
                        deletedCounterpartLine = entryDeletedLine.getValue();
                    } else {
                        /*
                         * Forgot why I added an else statement here but I'll keep it just in-case I remember later on
                         * but can't find out where
                         */
                    }
                }

                if (deletedCounterpartLine != null && deletedCounterpartNumber != -1) {
                    // It was in-fact just a whitespace change!
                    allowedDeletedLines.remove(deletedCounterpartNumber, deletedCounterpartLine);
                    reformatted.put(deletedCounterpartNumber, addedLine.replaceFirst("[+]", "\u0020"));
                } else {
                    reformatted.put(index, addedLine);
                }
            } else if (deletedLine != null) {
                allowedDeletedLines.put(index, deletedLine);
            }
        }

        /* Add the deleted lines that are allowed to stay */
        for (Map.Entry<Integer, String> entry : allowedDeletedLines.entrySet()) {
            reformatted.put(entry.getKey(), entry.getValue());
        }

        /* Now, compile them into a list */
        List<String> reformattedArrayList = new ArrayList<>();

        for (int index = 0; index < totalLines; index++) {
            if (reformatted.get(index) != null) {
                reformattedArrayList.add(reformatted.get(index));
            }
        }

        return reformattedArrayList;
    }

}
