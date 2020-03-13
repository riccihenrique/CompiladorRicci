package compilador.compiler;

import java.util.ArrayList;
import java.util.List;

public class Symbol {
    private final List<Token> symbols = new ArrayList<>();

    public Symbol() {
        symbols.add(new Token("t_{", "{"));
        symbols.add(new Token("t_}", "}"));
        symbols.add(new Token("t_[", "["));
        symbols.add(new Token("t_]", "]"));
        symbols.add(new Token("t_(", "("));
        symbols.add(new Token("t_)", ")"));
        symbols.add(new Token("t_op", "*"));
        symbols.add(new Token("t_op", "-"));
        symbols.add(new Token("t_op", "+"));
        symbols.add(new Token("t_op", "/"));
       // symbols.add(new Token("t_.", "."));
        symbols.add(new Token("t_att", "="));
        symbols.add(new Token("t_logic", "&&"));
        symbols.add(new Token("t_logic", "||"));
        symbols.add(new Token("t_logic", "!"));
        symbols.add(new Token("t_relational", ">"));
        symbols.add(new Token("t_relational", "<"));
        symbols.add(new Token("t_relational", ">="));
        symbols.add(new Token("t_relational", "<="));
        symbols.add(new Token("t_relational", "!="));
        symbols.add(new Token("t_relational", "=="));
        symbols.add(new Token("t_att", "++"));
        symbols.add(new Token("t_att", "--"));
        symbols.add(new Token("t_end", ";"));
        symbols.add(new Token("t_\"", "\""));
        symbols.add(new Token("t_'", "'"));
        symbols.add(new Token("t_$", "$"));
    }      
    
    public List<Token> getSymbols() {
        return symbols;
    }
    
    public Token isReserved(String str) {
        for(Token t : symbols)
            if(t.getLexema().equals(str))
                return t;
        return null;
    }
}