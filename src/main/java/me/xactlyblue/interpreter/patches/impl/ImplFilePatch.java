package me.xactlyblue.interpreter.patches.impl;

import me.xactlyblue.interpreter.patches.Patch;
import me.xactlyblue.interpreter.patches.datatypes.DeletedLine;
import me.xactlyblue.interpreter.patches.interpreters.DeletedPatchInterpreter;
import me.xactlyblue.interpreter.patches.interpreters.PatchInterpreter;
import me.xactlyblue.interpreter.utilities.FileUtils;
import me.xactlyblue.interpreter.utilities.FilterUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImplFilePatch implements Patch {

    private File file;
    private String filePath;
    private List<PatchInterpreter> interpreters;

    private List<String> rawLines;
    private List<String> filteredLines;

    public ImplFilePatch(File file, String filePath, List<PatchInterpreter> interpreters) {
        this.file = file;
        this.filePath = filePath;
        this.interpreters = interpreters;

        this.rawLines = readLines(file);
        this.filteredLines = filterLines(rawLines);
    }

    private List<String> readLines(File unreadFile) {
        try {
            BufferedReader bufferedReader = FileUtils.getBufferedReader(unreadFile);
            List<String> lines = new ArrayList<>();
            String currentLine;

            while (( currentLine = bufferedReader.readLine() ) != null) {
                lines.add(currentLine);
            }

            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /* I know this method is messy but algorithms are hard! Cut me a little slack haha! */
    /* Also, I'd recommend optimizing it more; I just threw this together since it's a demo, so it's nowhere near as efficient as it could be! */
    private List<String> filterLines(List<String> unfilteredLines) {
        List<String> duplicatedLines = new ArrayList<>(unfilteredLines);
        List<Integer> linesToBeDiscarded = new ArrayList<>();
        List<Integer> discardedLines = new ArrayList<>();

        int discardingIndex = 0;

        /* Filter lines for non essentials */
        String[] allowedKeywords = new String[] { "+", "@", "-", " ", "diff", "new", "index" };

        for (String line : duplicatedLines) {
            boolean containsKeyword = false;

            /* If the line starts with any of the above keywords, it's important! */
            for (String keyword : allowedKeywords) {
                if (line.startsWith(keyword)) {
                    containsKeyword = true;
                    break;
                }
            }

            if (!containsKeyword) {
                linesToBeDiscarded.add(discardingIndex);
            }

            discardingIndex += 1;
        }

        /* Discard non essential lines, without calling a ConcurrentModificationException haha */
        while (linesToBeDiscarded.size() >= 1) {
            Integer index = linesToBeDiscarded.get(0);
            int modifier = 0;

            for (int entry : discardedLines) {
                if (index > entry) {
                    modifier--;
                }
            }

            int modifiedIndex = index + modifier;

            discardedLines.add(index);
            linesToBeDiscarded.remove(index);
            duplicatedLines.remove(modifiedIndex);

            System.out.println("Discarded line: " + index);
        }

        /* Keep track of the new set of lines, after initial filtering for junk, and start keeping track of deleted lines! */
        List<String> finalLines = new ArrayList<>(duplicatedLines);
        HashMap<Integer, DeletedLine> deletedLines = new HashMap<>();

        int deletionFilterIndex = 0;

        for (String line : finalLines) {
            if (line.startsWith("-") && !line.startsWith("---")) {
                String withoutSymbol = line.replaceFirst("[-]", " ");
                DeletedLine deletedLine = new DeletedLine(deletionFilterIndex, withoutSymbol, false);

                deletedLines.put(deletionFilterIndex, deletedLine);
            } else if (line.startsWith("+") && !line.startsWith("+++")) {
                String withoutSymbol = line.replaceFirst("[+]", " ");

                if (FilterUtils.isLineDeleted(deletedLines, withoutSymbol)) {
                    System.out.println("A line was replaced:");
                    System.out.println(withoutSymbol);
                }
            }

            deletionFilterIndex++;
        }

        /* Sorry it's messy, sleep deprivation moment haha */
        for (PatchInterpreter interpreter : getInterpreters()) {
            if (interpreter instanceof DeletedPatchInterpreter) {
                for (Map.Entry<Integer, DeletedLine> entry : deletedLines.entrySet()) {
                    ((DeletedPatchInterpreter) interpreter).interpret(entry.getValue(), deletedLines, finalLines);
                }
            }

            int interpreterIndex = 0;

            for (String line : finalLines) {
                interpreter.interpret(interpreterIndex, line);
                interpreterIndex += 1;
            }
        }

        return finalLines;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public List<String> getRawLines() {
        return rawLines;
    }

    @Override
    public List<String> getFilteredLines() {
        return filteredLines;
    }

    @Override
    public List<PatchInterpreter> getInterpreters() {
        return interpreters;
    }

}
