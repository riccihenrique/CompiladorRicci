package compilador.compiler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class CodeGenerator {
    
    private List<Token> tokens;
    
    public CodeGenerator(List<Token> tokens) {
        this.tokens  = tokens;
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
                        newTk.setType("double");
                        newTk.setValue(0);
                        
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
                    
                    if(!flag) {
                        aux_tks.add(tokens.get(i));
                    }
                    
                    while(aux_tks.size() > 2) {
                        Token newTk = new Token("t_id", "ta_" + label_varaux);
                        label_varaux++;
                        newTk.setType("double");
                        newTk.setValue(0);
                        
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
                else
                    newTokens.add(tokens.get(i));
            }
            else
                newTokens.add(tokens.get(i));
        }
        
        this.tokens = newTokens;
    }
    
    private String addVariable(Token t) {
        
    }
    
    public void generateCode() {
        for(Token t : tokens) {
            
        }
    }
    
    public List<Token> getTokens() {
        return this.tokens;
    }
}
