package me.xactlyblue.interpreter.patches.interpreters.impl;

import me.xactlyblue.interpreter.patches.datatypes.DeletedLine;
import me.xactlyblue.interpreter.patches.interpreters.DeletedPatchInterpreter;
import me.xactlyblue.interpreter.patches.interpreters.PatchInterpreter;

import java.util.HashMap;
import java.util.List;

public class WhitespaceInterpreter implements PatchInterpreter, DeletedPatchInterpreter {

    // TODO: Track changes that specifically have to do with white spaces being turned to tabs

    @Override
    public void interpret(DeletedLine deletedLine, HashMap<Integer, DeletedLine> deletedLines, List<String> finalLines) {

    }

    @Override
    public void interpret(int number, String line) {

    }

}
