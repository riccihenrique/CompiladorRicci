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
        
        //if(!follow.getFollow_command().contains(nextToken.getToken()))
            //addError("Trecho não esperado aqui");
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
                        else if(nextToken.getToken().equals("t_op")) {
                            operation();
                        }
                        else
                            addError("Ta faltando o ;");                            
                    }
                    else
                        addError("Tu não pode receber isso ai não");
                   
                }
                else if(nextToken.getLexema().equals("++") || nextToken.getLexema().equals("--")) {
                    nextToken();
                    if(!nextToken.getToken().equals("t_end") && !nextToken.getToken().equals("t_)")) 
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
    
    private void operation() {
        if(nextToken.getToken().equals("t_op")) {
            nextToken();
            if(nextToken.getToken().equals("t_num") || nextToken.getToken().equals("t_id")) {
                nextToken();
                
                if(nextToken.getToken().equals("t_end")) ;
                    //nextToken();
                else
                    addError("Falta um ;");
            }                
            else
                addError("algo nessa operaçao");
        }
        else
            addError("Isso nao e um operador");
        
        while(!follow.getFollow_operation().contains(nextToken.getToken()))
            nextToken();
    }
    
    private void decision() {
        if_statement();
        
        /*while(!first.getFirst_decision().contains(nextToken.getToken()) && !follow.getFollow_decision().contains(nextToken.getToken()))
            nextToken();
        
        if(!follow.getFollow_decision().contains(nextToken.getToken()))
            if(first.getFirst_decision().contains(nextToken.getToken()))
                command();
            else
                addError("Simbolo nao esperado aqui");*/
    }
    
    private void logic() {
        if(nextToken.getLexema().equals("!"))  // !
            nextToken();
        
        if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_bool"))
            nextToken();
        else if(nextToken.getToken().equals("t_$"))
            relational();
        else {
            addError("Tipo de dado nao suportado aqui em comparaçoes logicas");
            nextToken();
        }
        
        if(first.getFirst_logicPlus().contains(nextToken.getToken()))
            logicPlus();
        else if(!follow.getFollow_logicCondition().contains(nextToken.getToken()))
            addError("Este comando nao esta no formato correto");
    }

    private void logicPlus() {        
        if(nextToken.getLexema().equals("&&") || nextToken.getLexema().equals("||")) {
            nextToken();
            if(nextToken.getToken().equals("t_logic")) { // !
                nextToken();
            }
            
            if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_bool")) {
                //TODO
                nextToken();
            }
            else if(nextToken.getToken().equals("t_$"))
                relational();
            else {
                addError("Isso nao parece fazer parte da condiçao"); // ARRUMAR AQUI
                
                while(!follow.getFollow_logicPlus().contains(nextToken.getToken()) /*|| !nextToken.getLexema().equals("!")*/)
                    nextToken();
                if(nextToken.getLexema().equals("!")) {
                    nextToken();
                    while(!follow.getFollow_logicPlus().contains(nextToken.getToken()) /*|| !nextToken.getLexema().equals("!")*/)
                        nextToken();
                }
                    
            }
        }
        
        if(first.getFirst_logicPlus().contains(nextToken.getToken()) && !nextToken.getLexema().equals("!")) //Eveita que ! entre tambem
            logicPlus();
        else if(!follow.getFollow_logicPlus().contains(nextToken.getToken())) {
            addError("Revise esse trecho do codigo");            
            while(!follow.getFollow_logicPlus().contains(nextToken.getToken()) && new Symbol().isReserved(nextToken.getLexema()) == null)
                    nextToken();
        }
    }

    private void relational() {
        if(nextToken.getToken().equals("t_$")) {
            nextToken();
            
            if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_num")) {
                nextToken();
            
                if(nextToken.getToken().equals("t_relational")) {
                    nextToken();
                
                    if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_num")) {
                        nextToken();
                        
                        if(nextToken.getToken().equals("t_$"))
                            nextToken();
                        else
                            addError("Amigao, ta faltando o $");
                    }   
                    else
                        addError("Não há parametros para comparação");
                }
                else
                    addError("Não é um operador relacional");
            }
        }
        
        while(!follow.getFollow_relationalCondition().contains(nextToken.getToken()))
            nextToken();
        
        if(first.getFirst_logicPlus().contains(nextToken.getToken()))
            logicPlus();
    }
    
    private void condition() {
        if(first.getFirst_relationalCondition().contains(nextToken.getToken()))
            relational();
        else if(first.getFirst_logicCondition().contains(nextToken.getToken()))
            logic();       
        else {
            addError("Isso não parece ser uma condição...");
            //while(!follow.getFollow_condition().contains(nextToken.getToken()))
             //   nextToken();
        }
    }

    private void if_statement() {
        boolean flag = false;
        if(nextToken.getToken().equals("t_if")) {
            nextToken();
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
                        }
                        
                        nextToken();
                        if(nextToken.getToken().equals("t_else")) {
                            nextToken();
                            
                            if(nextToken.getToken().equals("t_{")) {
                                nextToken();
                                command();
                        
                                if(!nextToken.getToken().equals("t_}")) {
                                    addError("Ta faltando o }");
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
        forS();
        whileS();
        
        /*while(!first.getFirst_repetition().contains(nextToken.getToken()) && !follow.getFollow_repetition().contains(nextToken.getToken()))
            nextToken();
        
        if(!follow.getFollow_repetition().contains(nextToken.getToken()))
            if(first.getFirst_repetition().contains(nextToken.getToken()))
                command();
            else
                addError("Simbolo nao esperado aqui");*/
    }
    
    private void forS() {
        if(nextToken.getToken().equals("t_for")) {
            nextToken();
            if(nextToken.getToken().equals("t_(")) {
                nextToken();
                atributtion();
                if(nextToken.getToken().equals("t_end")) // Adaptaçao tecnca
                    nextToken();
                
                condition();
                if(nextToken.getToken().equals("t_end")) 
                    nextToken();
                else
                    addError("CADE O ;?");
                if(first.getFirst_atribution().contains(nextToken.getToken()))
                    atributtion();
                else
                    addError("Isso nao se parece com uma atribuiçao");
                
                /*while(!nextToken.getToken().equals("t_)")) // Adaptaçao tecnca
                    nextToken();*/
                
                if(nextToken.getToken().equals("t_)")) {
                    nextToken();
                    
                    if(nextToken.getToken().equals("t_{")) {
                        nextToken();
                        command();
                        
                        if(nextToken.getToken().equals("t_}"))
                            nextToken();
                        else
                            addError("Ta faltando o }");
                    }
                    else
                        addError("Ta faltando o {");
                }
                else
                    addError("Ta faltando o )");
            }
            else
                addError("Ta faltando o (");
        }
    }
    
    private void whileS() {
        if(nextToken.getToken().equals("t_while")) {
            nextToken();
            if(nextToken.getToken().equals("t_(")) {
                nextToken();
                condition();
                
                if(nextToken.getToken().equals("t_)")) {
                    nextToken();
                    
                    if(nextToken.getToken().equals("t_{")) {
                        nextToken();
                        command();
                        
                        if(nextToken.getToken().equals("t_}")) {
                            nextToken();
                        }
                        else
                           addError("Ta faltando o {");
                    }
                    else
                        addError("Ta faltando o {");
                }
                else
                    addError("Ta faltando o )");
                
            }
            else
                addError("Ta faltando o (");
        }
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
        try {
            main();
        }
       catch(Exception e) {System.out.println(e.getMessage());}
        
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