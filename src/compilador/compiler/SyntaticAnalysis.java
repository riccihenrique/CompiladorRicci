package compilador.compiler;

import java.util.ArrayList;
import java.util.List;

public class SyntaticAnalysis {
    
    private final String code;
    private Token nextToken;
    private final List<Error> errors;
    private LexicalAnalysis la;
    private final First first;
    private final Follow follow;
    
    public SyntaticAnalysis(String code) {
        this.code = code;
        this.first = new First();
        this.follow = new Follow();
        this.errors = new ArrayList<>();
    }
    
    private void t_var() {
        if(nextToken.getToken().equals("t_var")) {
            nextToken();
            if(nextToken.getToken().equals("t_[")) {
                nextToken();
                t_declaration();
                
                if(nextToken != null && nextToken.getToken().equals("t_]")) 
                    nextToken();
                else
                    addError("Esta faltando um ]");
            }
            else
                addError("É necessário um [ depois do var");
        }
    }
    
    private void t_declaration() {
        if(nextToken.getToken().equals("t_type")) {
            nextToken();
            if(nextToken.getToken().equals("t_id")){
                nextToken();
                if(nextToken.getToken().equals("t_end")) {
                    nextToken();
                    // TODO
                }
                else 
                    addError("Esta faltando um ;");
            }
            else
                addError("Eu tava esperando uma variável aqui :/");
        }
        else
            addError("Tipo de dado nao conhecido");
        
 
        while(nextToken != null && !first.getT_declaration().contains(nextToken.getToken()) && !follow.getT_declaration().contains(nextToken.getToken()) && 
                    !first.getT_command().contains(nextToken.getToken()) && !follow.getT_command().contains(nextToken.getToken()))
            nextToken();
        
        if(nextToken != null && !follow.getT_declaration().contains(nextToken.getToken()) && first.getT_declaration().contains(nextToken.getToken()))
            t_declaration();
    }
    
    private void t_command() {
        if(nextToken != null) {
            if(first.getT_attribution().contains(nextToken.getToken()))
                t_att();
            else if(first.getT_decision().contains(nextToken.getToken()))
                t_decision();
            else if(first.getT_repetition().contains(nextToken.getToken()))
                t_repetition();
            else
                addError("Onde esta o comando daqui?");

            while(nextToken != null && !first.getT_command().contains(nextToken.getToken()) && !follow.getT_command().contains(nextToken.getToken()))
                nextToken();
  
            if(nextToken != null && first.getT_command().contains(nextToken.getToken()))
                t_command();
        }
    }
    
    private void t_att() {
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
                        }
                        else if(nextToken.getToken().equals("t_op")) {
                            t_operation();
                        }
                        else
                            addError("Esta faltando um  ;");                            
                    }
                    else
                        addError("Tu não pode receber isso ai não");
                   
                }
                else if(nextToken.getLexema().equals("++") || nextToken.getLexema().equals("--")) {
                    nextToken();
                    if(!follow.getT_attribution().contains(nextToken.getToken())) 
                        addError("Esta faltando um  ;");
                    else
                        nextToken();
                }
            }
            else
                addError("Onde esta a atribuiçao?");
        }
    }
    
    private void t_operation() {
        if(nextToken.getToken().equals("t_op")) {
            nextToken();
            if(nextToken.getToken().equals("t_num") || nextToken.getToken().equals("t_id")) {
                nextToken();
                
                if(nextToken.getToken().equals("t_end")) ;
                    //nextToken();
                else
                    addError("Esta faltando um  ;");
            }                
            else
                addError("algo nessa operaçao");
        }
        else
            addError("Isso nao e um operador");
        
        while(!follow.getT_operation().contains(nextToken.getToken()))
            nextToken();
    }
    
    private void t_decision() {
        t_if();
    }
    
    private void t_logicC() {
        if(nextToken.getLexema().equals("!"))  // !
            nextToken();
        
        if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_bool"))
            nextToken();
        else if(nextToken.getToken().equals("t_$"))
            t_relationalC();
        else {
            addError("Tipo de dado nao suportado em comparaçoes logicas");
            nextToken();
        }
        
        if(first.getT_logicCPlus().contains(nextToken.getToken()))
            t_logicCPlus();
        else if(!follow.getT_logicC().contains(nextToken.getToken()))
            addError("Este comando nao esta no formato correto");
    }

    private void t_logicCPlus() {        
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
                t_relationalC();
            else {
                addError("Isso nao parece fazer parte da condiçao"); // ARRUMAR AQUI
                
                while(!follow.getT_logicCPlus().contains(nextToken.getToken()) /*|| !nextToken.getLexema().equals("!")*/)
                    nextToken();
                if(nextToken.getLexema().equals("!")) {
                    nextToken();
                    while(!follow.getT_logicCPlus().contains(nextToken.getToken()) /*|| !nextToken.getLexema().equals("!")*/)
                        nextToken();
                } 
            }
        }
        
        if(first.getT_logicCPlus().contains(nextToken.getToken()) && !nextToken.getLexema().equals("!")) //Eveita que ! entre tambem
            t_logicCPlus();
        else 
            
            if(!follow.getT_logicCPlus().contains(nextToken.getToken())) {
                addError("Revise esse trecho do codigo");            
                while(!follow.getT_logicCPlus().contains(nextToken.getToken()) && new Symbol().isReserved(nextToken.getLexema()) == null)
                    nextToken();
            }
    }

    private void t_relationalC() {
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
                            addError("Esta faltando um $");
                    }   
                    else
                        addError("Nao encontrei algo para relacionar");
                }
                else
                    addError("Esta faltando um operador relacional");
            }
            else
                addError("Nao encontrei algo para relacionar");
        }
        
        if(first.getT_logicCPlus().contains(nextToken.getToken()))
            t_logicCPlus();
    }
    
    private void t_condition() {
        if(first.getT_relationalC().contains(nextToken.getToken()))
            t_relationalC();
        else if(first.getT_logicC().contains(nextToken.getToken()))
            t_logicC();       
        else 
            addError("Isso não parece ser uma condição...");
    }

    private void t_if() {
        boolean flag = false;
        if(nextToken.getToken().equals("t_if")) {
            nextToken();
            if(nextToken.getToken().equals("t_(")) {
                nextToken();
                t_condition();
                if(nextToken.getToken().equals("t_)")) {
                    nextToken();
                    if(nextToken.getToken().equals("t_{")) {
                        nextToken();
                        t_command();
                        
                        if(!nextToken.getToken().equals("t_}")) {
                            addError("Esta faltando um  }");
                        }
                        
                        nextToken();
                        t_else();
                    }
                    else
                        addError("Esta faltando um { ");
                }
                else
                    addError("Esta faltando um )");
            }
            else
                addError("Esta faltando um (");            
        }
    }
    
    private void t_else() {
        if(nextToken.getToken().equals("t_else")) {
            nextToken();

            if(nextToken.getToken().equals("t_{")) {
                nextToken();
                t_command();

                if(!nextToken.getToken().equals("t_}"))
                    addError("Esta faltando um  }");
                else
                    nextToken();
            }
            else
                addError("Esta faltando um { ");
        }
    }
    
    private void t_repetition() {
        t_for();
        t_while();
    }
    
    private void gambi() {
        while(nextToken != null && !nextToken.getLexema().equals(")") && !nextToken.getLexema().equals("{"))
            nextToken();
        
        if(nextToken.getLexema().equals(")")) 
            nextToken();
        else
            addError("Falta um )");
            
        if(nextToken.getToken().equals("t_{"))
            nextToken();
        else
            addError("Falta um {");
        
        t_command();
        if(nextToken.getToken().equals("t_}"))
             nextToken();
        else
            addError("Falta um }");
    }
    
    private void t_for() {
        if(nextToken.getToken().equals("t_for")) {
            nextToken();
            if(nextToken.getToken().equals("t_(")) {
                nextToken();
                t_att();                
                
                if(first.getT_condition().contains(nextToken.getToken())) {
                    t_condition();
                    if(nextToken.getToken().equals("t_end")) {
                        nextToken();
                        
                        if(first.getT_attribution().contains(nextToken.getToken())) {   
                            t_att();
                            if(nextToken.getToken().equals("t_)")) {
                                nextToken();

                                if(nextToken.getToken().equals("t_{")) {
                                    nextToken();
                                    t_command();

                                    if(nextToken.getToken().equals("t_}"))
                                        nextToken();
                                    else
                                        addError("Esta faltando um  }");
                                }
                                else
                                    addError("Esta faltando um  {");
                            }
                            else {
                                gambi();
                                addError("Esta faltando um  )");
                            }
                        }
                        else {
                            gambi();
                            addError("Ou falta um ; ou a atribuiiçao esta incorreta");
                        }
                    }
                    else {
                        gambi();
                        addError("Esta faltando um  ;");
                    }
                }
                else {
                    gambi();
                    addError("Erro na 1ª atribuiçao");
                }                
            }
            else {
                gambi();
                addError("Esta faltando um  (");
            }
        }
    }
    
    private void t_while() {
        if(nextToken.getToken().equals("t_while")) {
            nextToken();
            if(nextToken.getToken().equals("t_(")) {
                nextToken();
                t_condition();
                
                if(nextToken.getToken().equals("t_)")) {
                    nextToken();
                    
                    if(nextToken.getToken().equals("t_{")) {
                        nextToken();
                        t_command();
                        
                        if(nextToken.getToken().equals("t_}"))
                            nextToken();                        
                        else
                           addError("Esta faltando um  {");
                    }
                    else
                        addError("Esta faltando um  {");
                }
                else
                    addError("Esta faltando um  )");                
            }
            else
                addError("Esta faltando um  (");
        }
    }
    
    private void t_main() {
        if(nextToken.getToken().equals("t_main")) {
            nextToken();        
            if(nextToken.getToken().equals("t_{")) {
                nextToken();
                t_var();
                t_command();
                if(nextToken != null && nextToken.getToken().equals("t_}"))
                    return;
                else
                    addError("Esta faltando um }");
            }
            else
                addError("Esta faltando um { depois do Ricci");
        }
        else
            addError("Palavra ricci não encontrada no início");
    }
    
    public void Analyse() {
        la = new LexicalAnalysis(code.toLowerCase());        
        nextToken();      
        try {
            t_main();
        }
       catch(Exception e) {System.out.println(e.getMessage());}
        if(nextToken == null)
            addError("Fim de arquivo inesperado");
        else
            nextToken(); // Ver se nao tem mais nada a ser lido
    }
    
    public List<Error> getErrors() {
        errors.addAll(la.getErrors());
        return errors;
    }
    
    private void addError(String error) {
        int[] lineInformation = la.getLineInformation();
        errors.add(new Error(error, lineInformation[0], lineInformation[1]));
    }
    
    private void nextToken() {
        nextToken = la.nextToken();
    }
    
    public boolean finished() {
        return la.finished();
    }

    List<Token> getTable() {
        return la.getTokens();
    }
}