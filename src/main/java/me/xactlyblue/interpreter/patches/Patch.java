package me.xactlyblue.interpreter.patches;

import me.xactlyblue.interpreter.patches.interpreters.PatchInterpreter;

import java.io.File;
import java.util.List;

public interface Patch {

    File getFile();
    String getFilePath();
    List<String> getRawLines();
    List<String> getFilteredLines();
    List<PatchInterpreter> getInterpreters();

}
