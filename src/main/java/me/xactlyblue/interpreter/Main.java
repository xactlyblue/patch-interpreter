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

        System.out.println("Creating patch and instantiating whitespace interpreter...");

        WhitespaceInterpreter whitespaceInterpreter = new WhitespaceInterpreter();

        PatchBuilder patchBuilder = new PatchBuilder(filePath + fileName);
        patchBuilder.addInterpreter(whitespaceInterpreter);

        Patch patch = patchBuilder.build();

        System.out.println("Cleaning up and creating whitespace file...");

        createWhitespaceFile(
                whitespaceInterpreter.reformatLinesWithoutWhitespace(),
                filePath,
                fileName
        );

        printSuccessMessage();
    }

    private static void createWhitespaceFile(List<String> formatted, String filePath, String fileName) {
        try {
            String correctedFilePath = filePath + fileName.replaceAll("[.]patch$", "-whitespace.patch");
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

    private static void printSuccessMessage() {
        System.out.println("Success! In the provided file path, there should be a new file containing the reformatted patch information!");
        System.out.println("The whitespace file will un-mark the specific changes that only change a line's whitespace.");
        System.out.println("This means it should clean up the differences (assuming the base version was changed to automatically have the tabs).");
        System.out.println("\n[NOTE] If you plan to commit this or apply this to a git repository, please make sure to copy over the");
        System.out.println("first few lines of the file (I believe they hold the commit name and description information) to the whitespaced version.");
        System.out.println("I'm not entirely sure how important it is, but I'd recommend being safe and having it there just in case.");
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
