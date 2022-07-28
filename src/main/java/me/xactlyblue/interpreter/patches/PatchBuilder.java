package me.xactlyblue.interpreter.patches;

import me.xactlyblue.interpreter.patches.impl.ImplFilePatch;
import me.xactlyblue.interpreter.patches.interpreters.PatchInterpreter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PatchBuilder {

    private String path;
    private List<PatchInterpreter> patchInterpreters;
    private File file;

    public PatchBuilder(String path) {
        this.path = path;
        this.patchInterpreters = new ArrayList<>();

        File locatedFile = new File(path);

        if (locatedFile == null || !locatedFile.exists())
            throw new RuntimeException("Invalid file provided - could not find file corresponding to given path");

        this.file = locatedFile;
    }

    public PatchBuilder addInterpreter(PatchInterpreter interpreter) {
        this.patchInterpreters.add(interpreter);
        return this;
    }

    public PatchBuilder removeInterpreter(PatchInterpreter interpreter) {
        this.patchInterpreters.remove(interpreter);
        return this;
    }

    public PatchBuilder removeInterpreter(int i) {
        this.patchInterpreters.remove(i);
        return this;
    }

    public Patch build() {
        return new ImplFilePatch(file, path, patchInterpreters);
    }
}
