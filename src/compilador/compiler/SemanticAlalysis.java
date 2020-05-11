package compilador.compiler;

import java.util.ArrayList;
import java.util.List;

public class SemanticAlalysis {
    
    private List<Error> errors;
    
    public SemanticAlalysis() {
        errors = new ArrayList<>();
    }
    
    public Token variableDeclared(List<Token> tokens, Token newtoken, int line) { // Check variable exists
        Token t = null;
        for(Token token : tokens) {
            if(token.getLexema().equals(newtoken.getLexema()))
                t = token;
        }
        if(t == null)
            errors.add(new Error("Variável " + newtoken.getLexema() + " não declarada", line, 0));
        return t;
    }
    
    public void checkVariable(List<Token> tokens, Token newtoken, int line) { // Check variable already declarad
        for(Token token : tokens) {
            if(token.getLexema().equals(newtoken.getLexema()))
                errors.add(new Error("Variável " + newtoken.getLexema() + " já declarada", line, 0));
        }
    }
    
    public Token checkType(Token id, Token value, boolean casting, int line) {
        boolean flag = false;
        
        if(casting) {
            try {
                String s = ((String) value.getValue()).replace("\"", "");
                if(id.getType().equals("numeric")) 
                    id.setValue(Double.parseDouble(s));                
                else if(id.getType().equals("string")) 
                    id.setValue(s);                
                else if(id.getType().equals("char")) {
                    if(s.length() == 1)
                        id.setValue((char) s.charAt(0));
                    else
                        throw new Exception("");
                }
                else {
                    id.setValue(Boolean.parseBoolean(s));
                }
                flag = true;
            }
            catch(Exception e) {
                errors.add(new Error("Impossível fazer cast entre os tipos", line, 0));
            }
        }
        else {
            if(value.getToken().equals("t_id")) {
                if(id.getType().equals(value.getType())) {
                    flag = true;
                    id.setValue(value.getValue());
                }
                else {
                    errors.add(new Error("Variáveis de tipos diferentes", line, 0));
                }
            }
            else if(id.getType().equals("numeric")) {
                try {
                    double v = Double.parseDouble((String) value.getValue());
                    id.setValue(v  + "");
                    flag = true;
                }
                catch(Exception e) {
                    errors.add(new Error(value.getValue() + " não é o tipo numérico", line, 0));
                }
            }
            else if(id.getType().equals("char")) {
                String s = ((String) value.getValue()).replace("'", "");
                if(s.length() == 1) {
                    char c = s.charAt(0);
                    id.setValue(c);
                    flag = true;
                }
                else
                    errors.add(new Error(value.getValue() + " não é o tipo char", line, 0));
            }
            else if(id.getType().equals("string")) {       
                try {
                    String s = (String) value.getValue();
                    if(s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
                        s = s.replace("\"", "");                        
                        id.setValue(s);
                        flag = true;
                    }
                    else 
                        errors.add(new Error(value.getValue() + " não é o tipo string", line, 0));
                }
                catch(Exception e) {
                    errors.add(new Error(value.getValue() + " não é o tipo string", line, 0));
                }
            }
            else {
                if(value.getToken().equals("t_bool")) {
                    id.setValue(value.getLexema().equals("false") ? false : true);
                    flag = true;
                }
                else 
                    errors.add(new Error(value.getValue() + " não é o tipo bool", line, 0));            
            }
        }
        
        if(flag)
            return id;
        return null;
    }
    
    public boolean checkInit(Token t, int line) {
        if(t.getValue() == null) {
            errors.add(new Error("Variável " + t.getLexema() + " não inicializada", line, 0));
            return false;
        }
        return true;
    }
    
    public void checkRelational(Token t1, String op, Token t2, int line) {
        if(checkInit(t1, line) && checkInit(t2, line)) {
            if(op.equals("==") || op.equals("!=")) {
                if(!t1.getType().equals(t2.getType()))
                   errors.add(new Error("Impossível comparar " + t1.getLexema() + " com " + t2.getLexema(), line, line));
            }
            else {
                if(t1.getType().equals("numeric") && t2.getType().equals(t1.getType())) {
                    
                }
                else {
                    errors.add(new Error("Impossível comparar " + t1.getLexema() + " com " + t2.getLexema(), line, line));
                }  
            }
        }        
    }
    
    public List<Error> getErrors() {
        return this.errors;
    }

    void checkLogic(Token var, int line) {
        if(checkInit(var, line)) {
            if(!var.getType().equals("bool"))
                errors.add(new Error(var.getLexema() + " não pode ser usada como variável relacional", line, line));
        }
    }
}
