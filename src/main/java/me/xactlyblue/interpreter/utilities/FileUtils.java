package me.xactlyblue.interpreter.utilities;

import java.io.*;

public class FileUtils {

    /*
     * I was having issues figuring this out, since you need to actually allocate
     * space for the characters to store them (atleast with the old method I used in the other class).
     * This is an issue because with that old strategy, you need to know the # of characters to even measure
     * how many characters there are.
     *
     * Credits to this for helping solve the problem:
     * https://www.tutorialspoint.com/Counting-number-of-characters-in-text-file-using-java
     */

    public static BufferedWriter getBufferedWriter(File file) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            return bufferedWriter;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedReader getBufferedReader(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            return bufferedReader;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getCharacterCount(File file, boolean includeNewline) {
        try {
            BufferedReader bufferedReader = getBufferedReader(file);
            String throwawayData;
            int characterCount = 0;

            while (( throwawayData = bufferedReader.readLine() ) != null) {
                if (includeNewline) {
                    characterCount += (throwawayData.length() + 1);
                } else {
                    characterCount += throwawayData.length();
                }
            }

            return characterCount;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getCharacterCount(File file) {
        return getCharacterCount(file, true);
    }

}