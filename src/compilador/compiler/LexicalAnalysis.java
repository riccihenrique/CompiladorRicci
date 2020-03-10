package compilador.compiler;

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalysis {    
    
    private final String code;    
    private final List<Error> errors;       
    private final List<Token> tokens;
    
    private final Symbol symbols;
    private final ReservedWords reservedWords;
    
    private int line;
    private int countLetters;
    private int column;
    
    public LexicalAnalysis(String code) {
        this.code = code;
        line = 1;
        countLetters = column = 0;
        
        errors = new ArrayList<>();
        tokens = new ArrayList<>();
        symbols = new Symbol();
        reservedWords = new ReservedWords();        
        
        tokens.addAll(symbols.getSymbols());
        tokens.addAll(reservedWords.getReservedWords());
    }
    
    public Token nextToken() {
        String tokenAux = "";
        char c;
        Token t;
        
        while(countLetters < code.length()) { //Permanece até ler o arquivo por completo
            c = code.charAt(countLetters);
            while(c == ' ') { //Consome espaços que estiverem no inicio
                c = code.charAt(++countLetters);
                column++;
            }
            
            if(countLetters == code.length()) //Gambiarra temporaria
                break;
            
            if(c == '/') {
                addCounters();
                c = code.charAt(countLetters);
                
                if(c == '/')
                    while(c != '\n') {
                        addCounters();
                        c = code.charAt(countLetters);
                    }
                else if(c == '*') {                    
                   while(c != '/') {
                        c = code.charAt(countLetters);
                        while(c != '*') {
                            addCounters();
                            c = code.charAt(countLetters);
                        }
                        addCounters();
                   }
                }
            }
            else
            if(c == '\n') { //Trata as quebras de linha para os contadores
                countLetters++;
                line++;
                column = 0;
            }else if(isChar(c) || c == '_') { //Caso leia um char de inicio, pode ser varaivel ou palavra reservada                
                while(!isReservatedSymbol(c) && c != ' ' && (isChar(c) || isNumber(c))) {
                    tokenAux += c;
                    addCounters();
                    c = code.charAt(countLetters);
                }
                
                t = reservedWords.isReserved(tokenAux);           
                if(t != null)
                    return t;
                else { //Variavel
                    Token variable = new Token("t_id", tokenAux);
                    tokens.add(variable);
                    return variable;
                }
            }            
            else if(isNumber(c)) {
                while(!isReservatedSymbol(c) && c != ' ') {
                    if(c == '.' && tokenAux.contains("."))
                        errors.add(new Error("Ooo fião, não pode adicionar dois pontos pra um número", line, column));
                    else
                        tokenAux += c;
                    addCounters();
                    c = code.charAt(countLetters);
                }
                //addCounters();
                try {
                    double number = Double.parseDouble(tokenAux);
                    
                    if(!Double.isNaN(number)) {
                        t = new Token("t_num", number + "");
                        tokens.add(t);
                        return t;
                    }   
                }
                catch(Exception e) {
                    errors.add(new Error("Sintaxe incorreta. Voce quis escrever um número ou uma variável?", line, column));
                    return new Token("t_id", tokenAux);
                }
            }
            else if (isReservatedSymbol(c)) {
                if(c == '\'') { //Char
                    tokenAux += c;
                    addCounters();
                    c = code.charAt(countLetters);
                    while(c != '\'') {
                        tokenAux += c;
                        addCounters();
                        c = code.charAt(countLetters);
                    }
                    tokenAux += c;
                    
                    t = new Token("t_char", tokenAux);
                    tokens.add(t);
                    
                    if(!(tokenAux.startsWith("'") && tokenAux.endsWith("'")))
                        errors.add(new Error("Essa char está estranho", line, column));
                    
                    addCounters();
                    return t; 
                }
                else if(c == '"') {
                    tokenAux += c;
                    addCounters();
                    c = code.charAt(countLetters);
                    while(c != '"') {
                        tokenAux += c;
                        addCounters();
                        c = code.charAt(countLetters);
                    }
                    
                    tokenAux += c;
                    
                    t = new Token("t_str", tokenAux);
                    tokens.add(t);
                    
                    if(!(tokenAux.startsWith("\"") && tokenAux.endsWith("\"")))
                        errors.add(new Error("Essa char está estranho", line, column));
                    
                    addCounters();
                    return t; 
                }
                else {
                    while(isArithmeticOperator(c) || isRelationalOperator(c) || isLogicOperator(c)) {
                        tokenAux += c;
                        addCounters();
                        c = code.charAt(countLetters);
                    }
                    
                    if(tokenAux.equals(""))
                        tokenAux += c;

                    t = symbols.isReserved(tokenAux);                
                    addCounters();
                    if(t != null)
                       return t;
                    else {
                        errors.add(new Error("Desconheço isso que voce escreveu: " + tokenAux, line, column));
                        addCounters();
                        return null;
                    }
                }
            }
            else {
                errors.add(new Error("OO fião, que simbolo é esse? " + c, line, column));
                addCounters();
                return null;
            }
        }
        return null;
    }
    
    private void addCounters() {
        countLetters++;
        column++;
    }
    
    private boolean isChar(char c) {
        return Character.isAlphabetic(c);
    }
    
    private boolean isNumber(char c) {
        return Character.isDigit(c);
    }
    
    private boolean isRelationalOperator(char c) {
        return c == '<' || c == '>' || c == '=' || c == '!';
    }
    
    private boolean isLogicOperator(char c) {
        return c == '&' || c == '|' || c == '!';
    }
    
    private boolean isArithmeticOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    
    private boolean isReservatedSymbol(char c) { 
        return symbols.isReserved(c + "") != null;
    }
    
    public List<Error> getErrors() {
        return this.errors;
    }
    
    public int[] getLineInformation() {
        int[] vet = {line, column};        
        return vet;
    }
    
    public void retrocesso(Token t) {
        column -= t.getLexema().length();
        countLetters -= t.getLexema().length();
    }
    
    public void nextCommand() {
        char c = code.charAt(countLetters);
        while(!isReservatedSymbol(c)) { //Consome espaços que estiverem no inicio
            c = code.charAt(++countLetters);
            column++;
        }
        if(c == ';')
            countLetters++;
    }
    
    public void showTokens() {
        for(Token t: tokens)
            System.out.println(t);
    }
}