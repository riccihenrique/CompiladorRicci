package compilador.compiler;

import java.util.ArrayList;
import java.util.List;

public class ReservedWords {
    private final List<Token> reserved = new ArrayList<>();
        
    public ReservedWords() {
        reserved.add(new Token("t_main", "ricci"));
        reserved.add(new Token("t_var", "var"));
        reserved.add(new Token("t_type", "numeric"));
        reserved.add(new Token("t_type", "char"));
        reserved.add(new Token("t_type", "string"));
        reserved.add(new Token("t_type", "bool"));
        reserved.add(new Token("t_if", "if"));
        reserved.add(new Token("t_else", "else"));
        reserved.add(new Token("t_for", "for"));
        reserved.add(new Token("t_while", "while"));
        reserved.add(new Token("t_bool", "true"));
        reserved.add(new Token("t_bool", "false"));
        reserved.add(new Token("t_cast", "cast"));
    }
    
    public Token isReserved(String str) {
        for(Token t : reserved)
            if(t.getLexema().equals(str))
                return t;
        return null;
    }

    public List<Token> getReservedWords() {
        return reserved;
    }  
}
