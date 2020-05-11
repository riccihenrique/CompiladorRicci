package compilador.compiler;

public class Token {
    private String token;
    private String lexema;
    private String type = "";
    private Object value;

    public Token(String token, String lexema) {
        this.token = token;
        this.lexema = lexema;
        this.value = null;        
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return token + ": " + lexema + (this.token.equals("t_id") ? " - type : " + this.type + " value: " + this.value : "");
    }
}
