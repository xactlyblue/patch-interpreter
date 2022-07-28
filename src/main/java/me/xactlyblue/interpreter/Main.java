package me.xactlyblue.interpreter;

import me.xactlyblue.interpreter.patches.Patch;
import me.xactlyblue.interpreter.patches.PatchBuilder;
import me.xactlyblue.interpreter.patches.datatypes.DeletedLine;
import me.xactlyblue.interpreter.patches.interpreters.DeletedPatchInterpreter;
import me.xactlyblue.interpreter.patches.interpreters.PatchInterpreter;
import me.xactlyblue.interpreter.patches.interpreters.impl.WhitespaceInterpreter;
import me.xactlyblue.interpreter.utilities.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    /*
     * This class isn't essential for the project, but acts more as a reference or demo for
     * the actual API this project provides. It also is a sort of gateway you can modify for quick
     * and easy interpretation for a specific file (instead of using it as an api, you just use it for
     * it's one-off functionality).
     */

    private static Pattern filePathPattern = Pattern.compile("^C:|~\\*\\$");
    private static Pattern patchFilePattern = Pattern.compile("^*[.]patch$");

    public static void main(String[] args) {
        System.out.println("Character: \u0009!");
        Scanner input = new Scanner(System.in);
        String filePath = getInput(input, "Enter the patch file's path (not including the file):");

        if (!isFilePath(filePath))
            throw new RuntimeException("Invalid file path - must start with \"C:\" or \"~\" and be of valid path format (ending in \\)");

        String fileName = getInput(input, "Enter the patch file's name:");

        if (!isPatchFile(fileName))
            throw new RuntimeException("Invalid file provided - must end with '.patch'");

        WhitespaceInterpreter whitespaceInterpreter = new WhitespaceInterpreter();

        PatchBuilder patchBuilder = new PatchBuilder(filePath + fileName);
        patchBuilder.addInterpreter(whitespaceInterpreter);

        Patch patch = patchBuilder.build();

        createWhitespaceFile(
                whitespaceInterpreter.reformatLinesWithoutWhitespace(),
                filePath,
                fileName
        );
    }

    private static void createWhitespaceFile(List<String> formatted, String filePath, String fileName) {
        try {
            String correctedFilePath = filePath + fileName.replaceAll("[.]patch$", "-whitespace.patch");
            System.out.println(correctedFilePath);
            File file = new File(correctedFilePath);

            if (file.createNewFile()) {
                System.out.println("Created whitespace file at " + correctedFilePath);
            } else {
                /* Already exists, woops */
            }

            BufferedWriter bufferedWriter = FileUtils.getBufferedWriter(file);
            String writeContents = "";

            for (String entry : formatted) {
                writeContents += (entry + "\n");
            }

            bufferedWriter.write(writeContents);
            bufferedWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isFilePath(String filePath) {
        Matcher filePathMatcher = filePathPattern.matcher(filePath);
        boolean filePathMatches = filePathMatcher.find();

        return filePathMatches;
    }

    private static boolean isPatchFile(String fileName) {
        Matcher fileNameMatcher = patchFilePattern.matcher(fileName);
        boolean fileNameMatches = fileNameMatcher.find();

        return fileNameMatches;
    }

    private static String getInput(Scanner input, String message) {
        System.out.println(message);

        return input.next();
    }

}
