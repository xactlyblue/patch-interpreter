package me.xactlyblue.interpreter.patches.datatypes;

public class DeletedLine {

    private int number;
    private String line;
    private boolean replaced;

    public DeletedLine(int number, String line, boolean replaced) {
        this.number = number;
        this.line = line;
        this.replaced = replaced;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public boolean isReplaced() {
        return replaced;
    }

    public void setReplaced(boolean replaced) {
        this.replaced = replaced;
    }

    @Override
    public String toString() {
        return "DeletedLine{" +
                "number=" + number + "," +
                "line=" + line + "," +
                "replaced=" + replaced + "}";
    }

}
