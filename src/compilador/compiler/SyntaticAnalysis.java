package compilador.compiler;

import java.util.ArrayList;
import java.util.List;

public class SyntaticAnalysis {
    
    private final String code;
    private Token nextToken;
    private final List<Error> errors = new ArrayList<>();
    private LexicalAnalysis la;
    private final First first;
    private boolean ignoreAll = false;
    
    public SyntaticAnalysis(String code) {
        this.code = code;
        this.first = new First();
    }
    
    private void var() {
        if(nextToken.getToken().equals("t_var")) {
            nextToken = la.nextToken();
            if(nextToken.getToken().equals("t_[")) {
                nextToken = la.nextToken();
                declaration();
                
                if(!nextToken.getToken().equals("t_]"))                    
                    addError("Hm... falta um ]");
                else
                    nextToken = la.nextToken();
            }
            else {
                addError("É necessário um [ depois do var");
                ignoreAll = true;
            }                 
        }
    }
    
    private void declaration() {
        if(nextToken.getToken().equals("t_type")) {
            nextToken = la.nextToken();
            if(nextToken.getToken().equals("t_id")){
                nextToken = la.nextToken();
                if(nextToken.getToken().equals("t_end")) {
                    nextToken = la.nextToken();
                    // TODO
                }
                else 
                    addError("Eae irmão, eu esperava um ; aqui");
            }
            else
                addError("Eu tava esperando uma variável aqui :/");
        }
        else{
            addError("Que tipo de dado é esse rapaz?");
            la.retrocesso(nextToken);
            la.nextCommand();
            nextToken = la.nextToken();
        } 
        
        if(!nextToken.getToken().equals("t_]"))
            declaration();
    }
    
    private void command() {
        if(first.getFirst_atribution().contains(nextToken.getToken())) {
            //nextToken = la.nextToken();
            atributtion();
        }
        else if(first.getFirst_decision().contains(nextToken.getToken())){
            //nextToken = la.nextToken();
            decision();
        }            
        else if(first.getFirst_repetition().contains(nextToken.getToken())) {
            //nextToken = la.nextToken();
            repetition();
        }
        else {
            addError("Cade o comando que deveria estar aqui?");
            la.retrocesso(nextToken);
            la.nextCommand();
        }
        
        if(first.getFirst_command().contains(nextToken.getToken()))
            command();
        
    }
    
    private void atributtion() {
        if(nextToken.getToken().equals("t_id")) {
            nextToken = la.nextToken();
            if(nextToken.getToken().equals("t_att")) {
                if(nextToken.getLexema().equals("=")) {
                    nextToken = la.nextToken();

                    if(nextToken.getToken().equals("t_id")) {
                        //TODO
                    }
                    else if(nextToken.getToken().equals("t_num")) {
                        //TODO
                    }
                    else if(nextToken.getToken().equals("t_char")) {
                        //TODO
                    }
                    else if(nextToken.getToken().equals("t_bool")) {
                        //TODO
                    }
                    else if(nextToken.getToken().equals("t_str")) {
                        //TODO
                    }
                    else
                        addError("Tu não pode receber isso ai não");

                    nextToken = la.nextToken();
                    if(!nextToken.getToken().equals("t_end"))
                        addError("Ta faltando o ;");
                    else
                        nextToken = la.nextToken();
                }
                else if(nextToken.getLexema().equals("++") || nextToken.getLexema().equals("--")) {
                    nextToken = la.nextToken();
                    if(!nextToken.getLexema().equals("t_end")) 
                        addError("Ta faltando o ;");                    
                }
                else 
                    addError("Aqui eu nem sei o que falar, mas ta errado");
            }
            else
                addError("Cade a atribuição?");
        }
    }    
    
    private void decision() {        
        if(nextToken.getToken().equals("t_if")) {
            if_statement();
        }
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
        
        /*nextToken = la.nextToken();
        la.retrocesso(nextToken);
        if(first.getFirst_logicPlus().contains(nextToken.getToken()))
            logicPlus();*/
    }

    /*private void logicPlus() {
        nextToken = la.nextToken();
        
        if(nextToken.getLexema().equals("&&")) {
            
        }
        else if(nextToken.getLexema().equals("||")){
            
        }
    }*/

    private void relational() {
        if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_num")) {
            nextToken = la.nextToken();
            
            if(nextToken.getToken().equals("t_relational")) {
                nextToken = la.nextToken();
                
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
            nextToken = la.nextToken();
        }
        else if(first.getFirst_relationalCondition().contains(nextToken.getToken())) {
            relational();
            nextToken = la.nextToken();
        }  
        else
            addError("Isso não parece ser uma condição...");        
    }

    private void if_statement() {
        nextToken = la.nextToken();
            if(nextToken.getToken().equals("t_(")) {
                nextToken = la.nextToken();
                condition();
                if(nextToken.getToken().equals("t_)")) {
                    nextToken = la.nextToken();
                    if(nextToken.getToken().equals("t_{")) {
                        nextToken = nextToken();
                        command();
                        
                        if(!nextToken.getToken().equals("t_}")) {
                            addError("Ta faltando o }");
                            la.retrocesso(nextToken);
                        }
                        
                        nextToken = la.nextToken();
                        if(nextToken.getToken().equals("t_else")) {
                            nextToken = la.nextToken();
                            
                            if(nextToken.getToken().equals("t_{")) {
                                 nextToken = la.nextToken();
                                command();
                        
                                if(!nextToken.getToken().equals("t_}")) {
                                    addError("Ta faltando o }");
                                    la.retrocesso(nextToken);
                                }
                            }
                            else
                                addError("Ta faltando um { ");
                        }
                        /*else
                            la.retrocesso(nextToken);*/
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
    
    private void repetition() {
        
    }
    
    private void main() {
        if(nextToken.getToken().equals("t_main")) {
            nextToken = la.nextToken();        
            if(nextToken.getToken().equals("t_{")) {
                nextToken = la.nextToken();
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
        nextToken = la.nextToken();      
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
    
    private Token nextToken() {
        return la.nextToken();
    }
}
