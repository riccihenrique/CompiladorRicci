package compilador.compiler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class CodeGenerator {
    
    private List<Token> tokens;
    private FileWriter file = null;
    private PrintWriter writer;
    private int position = 0, cont_lines = 60, i, qtd_par = 0, count_instruction = 0, count_for = 0;
    private List<Variable> vars = new ArrayList<>();
    private Token t, t_rec, v1, op, v2;

    public CodeGenerator(List<Token> tokens, String name) {
        this.tokens  = tokens;
        try {
            file = new FileWriter(name.subSequence(0, name.length() - 4) + ".asm");
        } 
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        writer = new PrintWriter(file);
    }
    
    public void Analyze() {
        int label_varaux = 0;
        boolean flag = false;
        List<Token> newTokens = new ArrayList<>();
        Stack<Token> stack = new Stack<>();
        for (int i = 0; i < tokens.size(); i++) {
            if(tokens.get(i).getToken().equals("t_id") && tokens.get(i + 1).getToken().equals("t_att") 
                    && (tokens.get(i + 3).getToken().equals("t_id") || tokens.get(i + 3).getToken().equals("t_op"))) {
                int index_var = i;
                i += 2;
                Queue<Token> aux_tks = new LinkedList<>();
                Queue<Token> aux_ops= new LinkedList<>();
                if(!tokens.get(i + 1).getToken().equals("t_end") && !tokens.get(i + 3).getToken().equals("t_end")) {
                    flag = false;
                    while(tokens.get(i + 1).getToken().equals("t_op")) {
                        Token var1 = tokens.get(i);
                        Token op = tokens.get(i + 1);
                        Token var2 = tokens.get(i + 2);

                        Token newTk = new Token("t_id", "ta_" + label_varaux);
                        newTk.setType("numeric");
                        newTk.setValue(0);
                        
                        newTokens.add(4, new ReservedWords().isReserved("numeric"));
                        newTokens.add(5, newTk);
                        newTokens.add(6, new Symbol().isReserved(";"));
                        
                        newTokens.add(newTk);
                        newTokens.add(new Symbol().isReserved("="));
                        newTokens.add(var1);
                        newTokens.add(op); 
                        newTokens.add(var2);
                        newTokens.add(new Symbol().isReserved(";"));
                        
                        aux_tks.add(newTk);
                        label_varaux ++;
                        i += 3;
                        if(!tokens.get(i).getToken().equals("t_end")) {
                            aux_ops.add(tokens.get(i));
                            i++;
                        }
                        else
                            flag = true;
                    }
                    
                    if(!flag)
                        aux_tks.add(tokens.get(i));
                    
                    
                    while(aux_tks.size() > 2) {
                        Token newTk = new Token("t_id", "ta_" + label_varaux);
                        label_varaux++;
                        newTk.setType("numeric");
                        newTk.setValue(0);
                        
                        newTokens.add(4, new ReservedWords().isReserved("numeric"));
                        newTokens.add(5, newTk);
                        newTokens.add(6, new Symbol().isReserved(";"));
                        
                        newTokens.add(newTk);
                        newTokens.add(new Symbol().isReserved("="));
                        newTokens.add(aux_tks.remove());
                        newTokens.add(aux_ops.remove()); 
                        newTokens.add(aux_tks.remove());
                        newTokens.add(new Symbol().isReserved(";"));
                        
                        aux_tks.add(newTk);
                    }
                    
                    i++;
                    
                    newTokens.add(tokens.get(index_var));
                    newTokens.add(new Symbol().isReserved("="));
                    newTokens.add(aux_tks.remove());
                    newTokens.add(aux_ops.remove()); 
                    newTokens.add(aux_tks.remove());
                    newTokens.add(tokens.get(i));
                }
                else {
                    i -= 2;
                    while(!tokens.get(i).getToken().equals("t_end")) {
                        newTokens.add(tokens.get(i));
                        i++;
                    }
                    newTokens.add(tokens.get(i));
                }
            }
            else if(tokens.get(i).getToken().equals("t_for")) {
                while(!tokens.get(i).getToken().equals("t_end"))
                    newTokens.add(tokens.get(i++));
                newTokens.add(tokens.get(i++));
                int j = i;
                while(!tokens.get(j).getToken().equals("t_end"))
                    j++;
                j++;
                int k = j;
                while(!tokens.get(k).getToken().equals("t_end"))
                    newTokens.add(tokens.get(k++));
                newTokens.add(tokens.get(k++));
                
               while(!tokens.get(i).getToken().equals("t_end"))
                   newTokens.add(tokens.get(i++));
               newTokens.add(tokens.get(i));
               i = k - 1;
            }
            else
                newTokens.add(tokens.get(i));
        }
        this.tokens = newTokens;
    }
    
    private void t_while() {
        boolean isRelational = false;
        i += 2; // (
        t = tokens.get(i);        
        boolean flag;
        if(t.getToken().equals("t_$")) {  // relational
            isRelational = true;
            flag = t_relational();
        }
        else
            flag = t_logic();
        int aux_qtd = qtd_par, aux_line;
        
        qtd_par ++;
        if(flag) { // Executa a construção do conteúdo do if
            int a = count_instruction++;
            addInFile("load R1, 0");
            addInFile("jmpEQ R1=R0, nextInstruction" + a);            
            aux_line = cont_lines;
            t = tokens.get(i);
            while(t.getToken().equals("t_id") || t.getToken().equals("t_if") && 
                    t.getToken().equals("t_for") || t.getToken().equals("t_for")) {
                if(t.getToken().equals("t_id")) {                
                    t_att();
                    i += 2;
                    t = tokens.get(i);
                }
                if(t.getToken().equals("t_for")) {
                    t_for();
                    t = tokens.get(i++);
                }                
                if(t.getToken().equals("t_while")) {
                    t_while();
                    t = tokens.get(i++);
                }
                if(t.getToken().equals("t_if")) { 
                    t_if();
                }
            }
            addInFile("jmp " + Integer.toHexString(aux_line - (isRelational ? 8 : 6)) + "h");
            addInFile("nextInstruction" + a + ":");
            count_instruction++;
        } // Ignora todo o IF
        else
            while(qtd_par > aux_qtd) {
                if(tokens.get(i).getToken().equals("t_{"))
                    qtd_par ++;
                else if(tokens.get(i).getToken().equals("t_}"))
                    qtd_par --;
                i++;
            }
    }
    
    private void t_for() {
        int aux_qtd = qtd_par, aux_line, aux_for = count_for;
        i += 2; // (
        t = tokens.get(i);
        t_att();
        writer.println("jmp for" + aux_for);
        i++; // ;        
        t = tokens.get(i);        
        aux_line = cont_lines;
        t_att();
        i += 2;
        t = tokens.get(i);        
        writer.println("for" + aux_for + ":"); count_for++;
        boolean flag;
        if(t.getToken().equals("t_$"))   // relational
            flag = t_relational();
        else
            flag = t_logic();
        
        qtd_par ++;
        if(flag) { // Executa a construção do conteúdo do if
            int a = count_instruction++;
            addInFile("load R1, 0");
            addInFile("jmpEQ R1=R0, nextInstruction" + a);
            i++;
            t = tokens.get(i);
            while(t.getToken().equals("t_id") || t.getToken().equals("t_if") && 
                    t.getToken().equals("t_for") || t.getToken().equals("t_for")) {
                if(t.getToken().equals("t_id")) {                
                    t_att();
                    i += 2;
                    t = tokens.get(i);
                }
                if(t.getToken().equals("t_for")) {
                    t_for();
                    t = tokens.get(i++);
                }                
                if(t.getToken().equals("t_while")) {
                    t_while();
                    t = tokens.get(i++);
                }
                if(t.getToken().equals("t_if")) { 
                    t_if();
                }
            }
            addInFile("jmp " + Integer.toHexString(aux_line + 4) + "h");
            addInFile("nextInstruction" + a + ":");
            count_instruction++;
        } // Ignora todo o IF
        else
            while(qtd_par > aux_qtd) {
                if(tokens.get(i).getToken().equals("t_{"))
                    qtd_par ++;
                else if(tokens.get(i).getToken().equals("t_}"))
                    qtd_par --;
                i++;
            }
    }
    
    private boolean t_logic() {        
        v1 = tokens.get(i++);
        boolean flag = false;
        if(tokens.get(i).getToken().equals("t_)")) // Apenas 1 expressão
            flag = true;
            
        if(v1.getToken().equals("t_id"))
            addInFile("load R1, [" + getPosition(vars, v1.getLexema()) + "]");
        else {            
            if(flag) {
                if(v1.getLexema().equals("true"))
                    addInFile("load R1, " + 1);
                else {
                    i += 2;
                    return false;
                }
            }
            else
                addInFile("load R1, " + (v1.getLexema().equals("true") ? 1 : 0));
        } 
                
        while(!tokens.get(i).getToken().equals("t_)")) {
            op = tokens.get(i++);
            v2 = tokens.get(i++);
            if(v2.getToken().equals("t_id"))
                addInFile("load R2, [" + getPosition(vars, v2.getLexema()) + "]");
            else
                addInFile("load R2, " + (v2.getLexema().equals("true") ? 1 : 0));

            if(op.getLexema().equals("&&"))
                addInFile("and R1, R1, R2");
            else
                addInFile("or R1, R1, R2");
        }
        addInFile("load R0, 0");
        i += 2;
        return true;
    }
    
    private boolean t_relational() {
        v1 = tokens.get(++i);
        if(v1.getToken().equals("t_id"))
            addInFile("load R1, [" + getPosition(vars, v1.getLexema()) + "]");
        else
            addInFile("load R1, " + ((int) Double.parseDouble(v1.getLexema())));

        op = tokens.get(++i);
        v2 = tokens.get(++i);
        if(v2.getToken().equals("t_id"))
            addInFile("load R2, [" + getPosition(vars, v2.getLexema()) + "]");
        else
            addInFile("load R2, " + ((int) Double.parseDouble(v2.getLexema())));
        
        if(!v1.getToken().equals("t_id") && !v2.getToken().equals("t_id")) { // Elimina caso sempre seja falso
            if(op.getLexema().equals("==") && !v2.getLexema().equals(v1.getLexema()))
                return false;
            else if(op.getLexema().equals("!=") && v2.getLexema().equals(v1.getLexema()))
                return false;
            else if(op.getLexema().equals("<") && Integer.parseInt(v1.getLexema()) >= Integer.parseInt(v2.getLexema()))
                return false;
            else if(op.getLexema().equals("<=") && Integer.parseInt(v1.getLexema()) > Integer.parseInt(v2.getLexema()))
                return false;
            else if(op.getLexema().equals(">") && Integer.parseInt(v1.getLexema()) <= Integer.parseInt(v2.getLexema()))
                return false;
            else if(op.getLexema().equals(">=") && Integer.parseInt(v1.getLexema()) < Integer.parseInt(v2.getLexema()))
                return false;
        }
        
        if(op.getLexema().equals("==")) {
            addInFile("load R3, 1");            
            addInFile("and R0, R1, R2");
        }
        else if(op.getLexema().equals("!=")) {
            addInFile("xor R0, R1, R2");
        }
        else if(op.getLexema().equals("<=")) {

        }
        else if(op.getLexema().equals("<")) {

        }
        else if(op.getLexema().equals(">=")) {

        }
        else if(op.getLexema().equals(">")) {
            
        }
        i += 4; // $ ) {
        return true;
    }
    
    private void t_att() {
        t_rec = tokens.get(i++);
        op = tokens.get(i++); // = 
        if(op.getLexema().equals("=")) {
            v1 = tokens.get(i);

            if(v1.getToken().equals("t_(")) {                    
                i += 3;
                v1 = tokens.get(i);
            }

            if(!v1.getType().equals("numeric")) {
                String pos = getPosition(vars, t_rec.getLexema());
                if(t_rec.getType().equals("bool")) {
                    addInFile("load R1, " + (v1.getLexema().equals("true") ? 1 : 0));
                    addInFile("store R1, [" + pos + "]");
                }
                else {                                
                    for(int j = 0; j < ((String) v1.getValue()).length(); j++) {                            
                        addInFile("load R1, \"" + ((String) v1.getValue()).charAt(j) + "\"");
                        addInFile("store R1, [" + (Integer.parseInt(pos) + j) + "]");
                    }
                }
            }
            else {
                op = tokens.get(++i);
                if(!op.getToken().equals("t_op"))  // Simple Atributtion  
                    addInFile("load R1, " + ((int) Double.parseDouble(t_rec.getValue() + "")));
                else            
                { // Operation
                    v2 = tokens.get(++i);

                    if(v1.getToken().equals("t_id"))    
                        addInFile("load R2, [" + getPosition(vars, v1.getLexema()) + "]");
                    else
                        addInFile("load R2, " + ((int) Double.parseDouble(v1.getLexema())));

                    if(v2.getToken().equals("t_id"))
                        addInFile("load R3, [" + getPosition(vars, v2.getLexema()) + "]");
                    else
                        addInFile("load R3, " + ((int) Double.parseDouble(v2.getLexema())));

                    if(op.getLexema().equals("-")) {
                        addInFile("load R4, 1");
                        addInFile("xor R3, R3, RF");
                        addInFile("addi R3, R3, R4");
                    }

                    if(op.getLexema().equals("+") || op.getLexema().equals("-")) {                    
                        addInFile("addi R1, R2, R3");                        
                    }
                    else if(op.getLexema().equals("*")) {
                        addInFile("load R0, 2");                        
                        addInFile("load R5, 1");
                        addInFile("move R6, R3");
                        addInFile("addi R0, R0, RF");
                        int pos_aux = cont_lines;                        
                        addInFile("jmpLE R2<=R0, " + Integer.toHexString(pos_aux + 8) + "h");
                        addInFile("addi R3, R3, R6");
                        addInFile("addi R4, R4, R5");
                        addInFile("move R1, R3");
                    }
                    else {
                        addInFile("load R5, 255");
                        addInFile("xor R4, R3, R5");  
                        addInFile("load R6, 1");
                        addInFile("load R7, 0");
                        addInFile("addi R4, R4, R6");
                        addInFile("move R0, R3");
                        addInFile("addi R0, R0, RF");
                        int pos_aux = cont_lines; 
                        addInFile("jmpLE R2 <= R0, " +  Integer.toHexString(pos_aux + 12) + "h");
                        pos_aux = cont_lines;                        
                        addInFile("addi R2, R2, R4");
                        addInFile("move R0, R2");
                        addInFile("addi R7, R7, R6");
                        addInFile("jmpLE R3<=R0, " + Integer.toHexString(pos_aux + 2) + "h");

                        addInFile("move R1, " + (op.getLexema().equals("/") ? "R7" : "R2"));
                    }
                }
            }    
        }
        else {
            addInFile("load R2, [" + getPosition(vars, t_rec.getLexema()) + "]");
            if(op.getLexema().equals("--")) 
                addInFile("addi R1, R2, Rf");
            else {
                addInFile("load R3, 1");
                addInFile("addi R1, R2, R3");
            }
            i--;
        }        
        addInFile("store R1, [" + getPosition(vars, t_rec.getLexema()) + "]");
    }
    
    private void t_if() {
        i += 2; // (
        t = tokens.get(i);
        boolean flag;
        if(t.getToken().equals("t_$"))  // relational
            flag = t_relational();
        else
            flag = t_logic();
        int aux_qtd = qtd_par;
        qtd_par ++;
        if(flag) { // Executa a construção do conteúdo do if
            int a = count_instruction;
            addInFile("jmpEQ R1=R0, nextInstruction" + a);
            t = tokens.get(i);
            if(t.getToken().equals("t_id"))
                t_att();
            else if(t.getToken().equals("t_for"))
                t_for();
            else if(t.getToken().equals("t_while")) 
                t_while();
            else if(t.getToken().equals("t_if"))
                t_if();
            addInFile("nextInstruction" + a + ":");                        
            count_instruction++;
        } // Ignora todo o IF
        else
            while(qtd_par > aux_qtd) {
                if(tokens.get(i).getToken().equals("t_{"))
                    qtd_par ++;
                else if(tokens.get(i).getToken().equals("t_}"))
                    qtd_par --;
                i++;
            }
    }
    
    public void generateCode() {        
        for(i = 0; i < tokens.size(); i++) {
            t = tokens.get(i);
            if(t.getToken().equals("t_var")) {
                i += 2;
                t = tokens.get(i);
                while (!t.getToken().equals("t_]")) {
                    if (t.getLexema().equals("string")) {
                        t = tokens.get(++i);
                        while (!t.getToken().equals("t_end")) {
                            if (!t.getToken().equals("t_,")) {
                                addInFile("db " + "\"          \"");
                                vars.add(new Variable(t.getLexema(), Integer.toHexString(position)));
                                position += 10;
                            }

                            i++;
                            t = tokens.get(i);
                        }
                    } 
                    else {
                        boolean ischar = tokens.get(i).getLexema().equals("char");
                        int inc = 1;
                        if(t.getLexema().equals("numeric"))
                            inc = 2;
                        t = tokens.get(++i);
                        while (!t.getToken().equals("t_end")) {
                            if (!t.getToken().equals("t_,")) {
                                addInFile("db " + (ischar ? "\" \"" : 0));
                                vars.add(new Variable(t.getLexema(), position + ""));
                                position += 1;
                            }
                            i++;
                            t = tokens.get(i);
                        }
                    }
                    i++;
                    t = tokens.get(i);
                }
                
                addInFile("org 30h");
                addInFile("load RF, -1");
                cont_lines = 48;
           }
           else if(t.getToken().equals("t_if")) {
              t_if();
           }
           else if(t.getToken().equals("t_while")) {
               t_while();
           }
           else if(t.getToken().equals("t_for")) {
               t_for();
           }
           else if(t.getToken().equals("t_id")) { // Atributtion
                t_att();
           }
        }
        addInFile("halt");
        writer.close();
    }
    
    private void addInFile(String s) {
        writer.println(s);
        cont_lines += 2;
    }
    
    public List<Token> getTokens() {
        return this.tokens;
    }
    
    private String getPosition(List<Variable> l, String lexema) {
        for(Variable v : l) {
            if(v.getVar().equals(lexema))
                return v.getPos();
        }
        return null;
    }
    
    private class Variable {
        private String var, pos;

        public Variable(String var, String pos) {
            this.var = var;
            this.pos = pos;
        }

        public String getVar() {
            return var;
        }

        public void setVar(String var) {
            this.var = var;
        }

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }
    }
}
