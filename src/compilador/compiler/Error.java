package compilador.compiler;

public class Error {
    private String error;
    private int line;
    private int column;

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
    
    public Error(String error, int line, int column) {
        this.error = error;
        this.line = line;
        this.column = column;
    }

    public String getDesc() {
        return error;
    }

    public void setDesc(String error) {
        this.error = error;
    }

    public int getLinha() {
        return line;
    }

    public void setLinha(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "Erro na linha " + line + ". " + error;
    }
}
