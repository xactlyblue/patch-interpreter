package me.xactlyblue.interpreter;

import me.xactlyblue.interpreter.patches.Patch;
import me.xactlyblue.interpreter.patches.PatchBuilder;
import me.xactlyblue.interpreter.patches.datatypes.DeletedLine;
import me.xactlyblue.interpreter.patches.interpreters.DeletedPatchInterpreter;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static Pattern filePathPattern = Pattern.compile("^C:|~\\*\\$");
    private static Pattern patchFilePattern = Pattern.compile("^*[.]patch$");

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String filePath = getInput(input, "Enter the patch file's path (not including the file):");

        if (!isFilePath(filePath))
            throw new RuntimeException("Invalid file path - must start with \"C:\" or \"~\" and be of valid path format (ending in \\)");

        String fileName = getInput(input, "Enter the patch file's name:");

        if (!isPatchFile(fileName))
            throw new RuntimeException("Invalid file provided - must end with '.patch'");

        PatchBuilder patchBuilder = new PatchBuilder(filePath + fileName);
        patchBuilder.addInterpreter(new DeletedPatchInterpreter() {
            @Override
            public void interpret(DeletedLine deletedLine, HashMap<Integer, DeletedLine> deletedLines, List<String> finalLines) {
                System.out.println("Line was replaced - " + deletedLine.getNumber() + " - " + deletedLine.getLine());
            }
        });
        Patch patch = patchBuilder.build();
    }

    private static void writeFilteredToFile(Patch patch) {

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
