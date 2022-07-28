package me.xactlyblue.interpreter.patches.interpreters;

import me.xactlyblue.interpreter.patches.datatypes.DeletedLine;

import java.util.HashMap;
import java.util.List;

public interface DeletedPatchInterpreter extends PatchInterpreter {

    void interpret(DeletedLine deletedLine, HashMap<Integer, DeletedLine> deletedLines, List<String> finalLines);

    @Deprecated
    @Override
    default void interpret(int number, String line) {
        return;
    }

}
