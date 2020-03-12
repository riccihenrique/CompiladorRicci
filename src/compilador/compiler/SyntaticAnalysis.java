package compilador.compiler;

import java.util.ArrayList;
import java.util.List;

public class SyntaticAnalysis {
    
    private final String code;
    private Token nextToken;
    private final List<Error> errors = new ArrayList<>();
    private LexicalAnalysis la;
    private final First first;
    private final Follow follow;
    private boolean ignoreAll = false;
    
    public SyntaticAnalysis(String code) {
        this.code = code;
        this.first = new First();
        this.follow = new Follow();
    }
    
    private void var() {
        if(nextToken.getToken().equals("t_var")) {
            nextToken();
            if(nextToken.getToken().equals("t_[")) {
                nextToken();
                declaration();
                
                if(!nextToken.getToken().equals("t_]"))                    
                    addError("Hm... falta um ]");
                else
                    nextToken();
            }
            else {
                addError("É necessário um [ depois do var");
                ignoreAll = true;
            }
        }
    }
    
    private void declaration() {        
        boolean flag = false;  
        if(nextToken.getToken().equals("t_type")) {
            nextToken();
            if(nextToken.getToken().equals("t_id")){
                nextToken();
                if(nextToken.getToken().equals("t_end")) {
                    nextToken();
                    // TODO
                    flag = true;
                }
                else 
                    addError("Eae irmão, eu esperava um ; aqui");
            }
            else
                addError("Eu tava esperando uma variável aqui :/");
        }
        else
            addError("Que tipo de dado é esse rapaz?");
        if(!flag)    
            while(!first.getFirst_declaration().contains(nextToken.getToken()) && !follow.getFollow_declaration().contains(nextToken.getToken()))
                nextToken();
        
        if(!follow.getFollow_declaration().contains(nextToken.getToken()))
            if(first.getFirst_declaration().contains(nextToken.getToken()))
                declaration();
            else
                addError("Simbolo nao esperado aqui");
    }
    
    private void command() {
        if(first.getFirst_atribution().contains(nextToken.getToken()))
            atributtion();
        else if(first.getFirst_decision().contains(nextToken.getToken()))
            decision();
        else if(first.getFirst_repetition().contains(nextToken.getToken()))
            repetition();
        else
            addError("Cade o comando que deveria estar aqui?");
         
        while(!first.getFirst_command().contains(nextToken.getToken()) && !follow.getFollow_command().contains(nextToken.getToken()))
            nextToken();
        
        if(first.getFirst_command().contains(nextToken.getToken()))
            if(!nextToken.getToken().equals("t_}")) 
                command();
            else
                return;
    }
    
    private void atributtion() {
        boolean flag = false;
        if(nextToken.getToken().equals("t_id")) {
            nextToken();
            if(nextToken.getToken().equals("t_att")) {
                if(nextToken.getLexema().equals("=")) {
                    nextToken();

                    if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_num") || nextToken.getToken().equals("t_char") || 
                            nextToken.getToken().equals("t_bool") || nextToken.getToken().equals("t_str")) {
                         nextToken();
                        if(nextToken.getToken().equals("t_end")) {
                            nextToken();
                            //TODO

                            flag = true;
                        }
                        else
                            addError("Ta faltando o ;");
                    }
                    else
                        addError("Tu não pode receber isso ai não");
                   
                }
                else if(nextToken.getLexema().equals("++") || nextToken.getLexema().equals("--")) {
                    nextToken();
                    if(!nextToken.getLexema().equals("t_end")) 
                        addError("Ta faltando o ;");                    
                }
                else 
                    addError("Aqui eu nem sei o que falar, mas ta errado");
            }
            else
                addError("Cade a atribuição?");
             
        if(!flag)    
            while(!first.getFirst_atribution().contains(nextToken.getToken()) && !follow.getFollow_atribution().contains(nextToken.getToken()))
                nextToken();
        
        if(!follow.getFollow_atribution().contains(nextToken.getToken()))
            if(first.getFirst_atribution().contains(nextToken.getToken()))
                command();
            else
                addError("Simbolo nao esperado aqui");
        }
    }    
    
    private void decision() {
        if_statement();
        
  
        while(!first.getFirst_decision().contains(nextToken.getToken()) && !follow.getFollow_decision().contains(nextToken.getToken()))
            nextToken();
        
        if(!follow.getFollow_decision().contains(nextToken.getToken()))
            if(first.getFirst_decision().contains(nextToken.getToken()))
                command();
            else
                addError("Simbolo nao esperado aqui");
    }
    
    private void logic() {
        if(nextToken.getLexema().equals("!")) { // !
            
        }
        
        if(nextToken.getToken().equals("t_id")) { //Var
            //TODO
        }
        else if(nextToken.getToken().equals("t_bool")) { // Bool
            //TODO
        }
        else if(first.getFirst_relationalCondition().contains(nextToken.getToken())) { // Relational
            //TODO
        }
    }

    /*private void logicPlus() {
        nextToken();
        
        if(nextToken.getLexema().equals("&&")) {
            
        }
        else if(nextToken.getLexema().equals("||")){
            
        }
    }*/

    private void relational() {
        if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_num")) {
            nextToken();
            
            if(nextToken.getToken().equals("t_relational")) {
                nextToken();
                
                if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_num")) {
                    //TODO
                }
                else
                    addError("Não há parametros para comparação");
            }
            else
                addError("Não é um operador relacional");
        }
    }
    
    private void condition() {       
        if(first.getFirst_logicCondition().contains(nextToken.getToken())) {
            logic();
            nextToken();
        }
        else if(first.getFirst_relationalCondition().contains(nextToken.getToken())) {
            relational();
            nextToken();
        }  
        else
            addError("Isso não parece ser uma condição...");        
    }

    private void if_statement() {
        boolean flag = false;
        if(nextToken.getToken().equals("t_if")) {
            if(nextToken.getToken().equals("t_(")) {
                nextToken();
                condition();
                if(nextToken.getToken().equals("t_)")) {
                    nextToken();
                    if(nextToken.getToken().equals("t_{")) {
                        nextToken();
                        command();
                        
                        if(!nextToken.getToken().equals("t_}")) {
                            addError("Ta faltando o }");
                            la.retrocesso(nextToken);
                        }
                        
                        nextToken();
                        if(nextToken.getToken().equals("t_else")) {
                            nextToken();
                            
                            if(nextToken.getToken().equals("t_{")) {
                                 nextToken();
                                command();
                        
                                if(!nextToken.getToken().equals("t_}")) {
                                    addError("Ta faltando o }");
                                    la.retrocesso(nextToken);
                                }
                            }
                            else
                                addError("Ta faltando um { ");
                        }
                    }
                    else
                        addError("Ta faltando um { ");
                }
                else
                    addError("Ta faltando o )");
            }
            else
                addError("Amigão, ou tu digitou errado, ou ta faltando (");            
        }
    }
    
    private void repetition() {
        
    }
    
    private void main() {
        if(nextToken.getToken().equals("t_main")) {
            nextToken();        
            if(nextToken.getToken().equals("t_{")) {
                nextToken();
                var();
                command();
                if(nextToken.getToken().equals("t_}"))
                    return;
                else
                    addError("Hm...falta um }");
            }
            else
                addError("Hm... Precisa de um { depois do Ricci");
        }
        else
            addError("Palavra ricci não encontrada no início");
    }
    
    public void Analyse() {
        la = new LexicalAnalysis(code.toLowerCase());        
        nextToken();      
        main();
        
        
        la.showTokens();
    }
    
    public List<Error> getErrors() {
        if(!ignoreAll)
            errors.addAll(la.getErrors());
        return errors;
    }
    
    private void addError(String error) {
        if(!ignoreAll) {
            int[] lineInformation = la.getLineInformation();
            errors.add(new Error(error, lineInformation[0], lineInformation[1]));
        }
    }
    
    private void nextToken() {
        if(!finished())
            nextToken = la.nextToken();
    }
    
    public boolean finished() {
        return la.finished();
    }
}
