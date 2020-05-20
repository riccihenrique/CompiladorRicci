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
    private final SemanticAlalysis semantic;
    
    private List<Token> tokens;
    
    public SyntaticAnalysis(String code) {
        this.code = code;
        this.first = new First();
        this.follow = new Follow();
        this.errors = new ArrayList<>();
        this.tokens = new ArrayList<>();
        this.semantic = new SemanticAlalysis();
    }
    
    private void t_var() {
        if(nextToken.getToken().equals("t_var")) {
            nextToken(true);
            if(nextToken.getToken().equals("t_[")) {
                nextToken(true);
                t_declaration();
                
                if(nextToken != null && nextToken.getToken().equals("t_]")) 
                    nextToken(true);
                else
                    addError("Esta faltando um ]");
            }
            else
                addError("É necessário um [ depois do var");
        }
    }
    
    private void t_declaration() {
        String type;
        
        if(nextToken.getToken().equals("t_type")) {
            type = nextToken.getLexema(); //get data type
            nextToken(true);
            
            t_declaration_more(type);
                
            if(nextToken.getToken().equals("t_end")) {
                nextToken(true);
                if(!first.getT_declaration().contains(nextToken.getToken()) && !follow.getT_declaration().contains(nextToken.getToken())) {
                    addError("Comando inesperado aqui. Necessario um tipo de dados ou ]");
                    nextToken(true);
                }
            }
            else 
                addError("Esta faltando um ;");
        }
        else
            addError("Tipo de dado nao conhecido");
        
 
        while(nextToken != null && !first.getT_declaration().contains(nextToken.getToken()) && !follow.getT_declaration().contains(nextToken.getToken()) && 
                    !first.getT_command().contains(nextToken.getToken()) && !follow.getT_command().contains(nextToken.getToken()))
            nextToken(true);
        
        if(nextToken != null && !follow.getT_declaration().contains(nextToken.getToken()) && first.getT_declaration().contains(nextToken.getToken()))
            t_declaration();
    }
    
    private void t_declaration_more(String type) {
        if(nextToken.getToken().equals("t_id")) {
            nextToken.setType(type);
            semantic.checkVariable(tokens, nextToken, la.getLineInformation()[0]);
            nextToken(true);
            if(nextToken.getToken().equals("t_,")) {
                nextToken(true);
                t_declaration_more(type);
            }
            else if(!follow.getT_declaration_more().contains(nextToken.getToken()) && !first.getT_declaration().contains(nextToken.getToken()) && !follow.getT_declaration().contains(nextToken.getToken()))
                addError("O simbolo separador deve ser ','");
        }
        else
            addError("Eu esperava uma variavel aqui :(");
        
        while(!follow.getT_declaration_more().contains(nextToken.getToken()) && !first.getT_declaration().contains(nextToken.getToken()) && !follow.getT_declaration().contains(nextToken.getToken()))
            nextToken(true);
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
                nextToken(true);
  
            if(nextToken != null && first.getT_command().contains(nextToken.getToken()))
                t_command();
        }
    }
    
    private void t_att() {
        Token var, var2;
        boolean check = true;
        boolean cast = false;
        if(nextToken.getToken().equals("t_id")) {
            var = semantic.variableDeclared(tokens, nextToken, la.getLineInformation()[0]);
            if(var == null)
                var = nextToken;
            attToken(var);
            nextToken(true);
            if(nextToken.getToken().equals("t_att")) {
                if(nextToken.getLexema().equals("=")) {
                    nextToken(true);
                    
                    if(nextToken.getToken().equals("t_(")) {
                        nextToken(true);
                        if(nextToken.getToken().equals("t_cast")) {
                            nextToken(true);
                            if(nextToken.getToken().equals("t_)")) {
                                nextToken(true);
                                cast = true;
                            }
                            else
                                errors.add(new Error("Falta um )", la.getLineInformation()[0], 0));                                
                        }
                        else
                            errors.add(new Error("Termo Cast não encontrado", la.getLineInformation()[0], 0));
                    }
                    
                    
                    if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_num") || nextToken.getToken().equals("t_char") || 
                            nextToken.getToken().equals("t_bool") || nextToken.getToken().equals("t_str")) {
                        
                        if(nextToken.getToken().equals("t_id")) {
                            var2 = semantic.variableDeclared(tokens, nextToken, la.getLineInformation()[0]);
                            if(var2 == null)
                                check = false;
                            else {
                                if(!semantic.checkInit(var2, la.getLineInformation()[0]))
                                    check = false;
                            }
                        }
                        else
                            var2 = nextToken;
                        
                        if(check) {
                            var = semantic.checkType(var, var2, cast, la.getLineInformation()[0]);
                            if(var != null)
                                attToken(var);
                        }
                        nextToken(true);
                        attToken(var2);
                        if(nextToken.getToken().equals("t_end"))
                            nextToken(true);
                        else if(nextToken.getToken().equals("t_op")) 
                            t_operation();
                        else
                            addError("Esta faltando um  ;");                            
                    }
                    else
                        addError("Atribuição imprópria");
                   
                }
                else if(nextToken.getLexema().equals("++") || nextToken.getLexema().equals("--")) {
                    nextToken(true);
                    if(!follow.getT_attribution().contains(nextToken.getToken())) 
                        addError("Esta faltando um  ;");
                    else
                        nextToken(true);
                }
            }
            else
                addError("Onde esta a atribuiçao?");
        }
    }
    
    private void t_operation() {
        Token var;
        boolean check;
        if(nextToken.getToken().equals("t_op")) {
            nextToken(true);
            if(nextToken.getToken().equals("t_num") || nextToken.getToken().equals("t_id")) {
                if(nextToken.getToken().equals("t_id")) {
                    var = semantic.variableDeclared(tokens, nextToken, la.getLineInformation()[0]);
                    if(var == null )
                        var = nextToken;
                    semantic.checkInit(var, la.getLineInformation()[0]);
                    
                    nextToken(true);
                    attToken(var);                           
                }
                else
                    nextToken(true);                
                
                if(nextToken.getToken().equals("t_op"))
                    t_operation();
                else if(!nextToken.getToken().equals("t_end"))
                    addError("Esta faltando um  ;");
            }                
            else
                addError("algo nessa operaçao");
        }
        else
            addError("Isso nao e um operador");
        
        while(!follow.getT_operation().contains(nextToken.getToken()))
            nextToken(true);
    }
    
    private void t_decision() {
        t_if();
    }
    
    private void t_logicC() {
        Token var;
        if(nextToken.getLexema().equals("!"))  // !
            nextToken(true);
        
        if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_bool")) {
            
            if(nextToken.getToken().equals("t_id")) {
                var = semantic.variableDeclared(tokens, nextToken, la.getLineInformation()[0]);
                if(var == null) 
                    var = nextToken;
                else 
                    semantic.checkLogic(var, la.getLineInformation()[0]);
                nextToken(true);
                attToken(var);
            }
            else            
                nextToken(true);
        }
        else if(nextToken.getToken().equals("t_$"))
            t_relationalC();
        else {
            addError("Tipo de dado nao suportado em comparaçoes logicas");
            nextToken(true);
        }
        
        if(first.getT_logicCPlus().contains(nextToken.getToken()))
            t_logicCPlus();
        else if(!follow.getT_logicC().contains(nextToken.getToken()))
            addError("Este comando nao esta no formato correto");
    }

    private void t_logicCPlus() {    
        Token var;
        if(nextToken.getLexema().equals("&&") || nextToken.getLexema().equals("||")) {
            nextToken(true);
            if(nextToken.getToken().equals("t_logic")) // !
                nextToken(true);
            
            if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_bool")) {
                if(nextToken.getToken().equals("t_id")) {
                    var = semantic.variableDeclared(tokens, nextToken, la.getLineInformation()[0]);
                    if(var == null) 
                        var = nextToken;
                    else 
                        semantic.checkLogic(var, la.getLineInformation()[0]);
                    nextToken(true);                    
                    attToken(var);
                }
                else
                    nextToken(true);
            }
            else if(nextToken.getToken().equals("t_$"))
                t_relationalC();
            else {
                addError("Isso nao parece fazer parte da condiçao"); // ARRUMAR AQUI
                
                while(!follow.getT_logicCPlus().contains(nextToken.getToken()) /*|| !nextToken.getLexema().equals("!")*/)
                    nextToken(true);
                if(nextToken.getLexema().equals("!")) {
                    nextToken(true);
                    while(!follow.getT_logicCPlus().contains(nextToken.getToken()) /*|| !nextToken.getLexema().equals("!")*/)
                        nextToken(true);
                } 
            }
        }
        
        if(first.getT_logicCPlus().contains(nextToken.getToken()) && !nextToken.getLexema().equals("!")) //Eveita que ! entre tambem
            t_logicCPlus();
        else 
            
            if(!follow.getT_logicCPlus().contains(nextToken.getToken())) {
                addError("Revise esse trecho do codigo");            
                while(!follow.getT_logicCPlus().contains(nextToken.getToken()) && new Symbol().isReserved(nextToken.getLexema()) == null)
                    nextToken(true);
            }
    }

    private void t_relationalC() {
        Token var1 = null, var2 = null;
        String op;
        
        if(nextToken.getToken().equals("t_$")) {
            nextToken(true);
            
            if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_num") 
                || nextToken.getToken().equals("t_str") || nextToken.getToken().equals("t_char")) {
                if(nextToken.getToken().equals("t_id")) 
                    var1 = semantic.variableDeclared(tokens, nextToken, la.getLineInformation()[0]);
                else
                    var1 = nextToken;
                
                nextToken(true);
                attToken(var1);
            
                if(nextToken.getToken().equals("t_relational")) {
                    op = nextToken.getLexema();
                    nextToken(true);
                
                    if(nextToken.getToken().equals("t_id") || nextToken.getToken().equals("t_num") 
                            || nextToken.getToken().equals("t_str") || nextToken.getToken().equals("t_char")) {
                        if(nextToken.getToken().equals("t_id")) 
                            var2 = semantic.variableDeclared(tokens, nextToken, la.getLineInformation()[0]);
                        else
                            var2 = nextToken;
                        
                        if(var1 != null && var2 != null) 
                            semantic.checkRelational(var1, op, var2, la.getLineInformation()[0]);                        
                        
                        nextToken(true);
                        attToken(var2);
                        if(nextToken.getToken().equals("t_$"))
                            nextToken(true);
                        else
                            addError("Esta faltando um $");
                    }
                    else
                        addError("Impossível comparar " + var1.getLexema() + " com " + nextToken.getLexema());
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
        
        while(nextToken != null && !follow.getT_condition().contains(nextToken.getToken()))
            nextToken(true);
    }

    private void t_if() {
        boolean flag = false;
        if(nextToken.getToken().equals("t_if")) {
            nextToken(true);
            if(nextToken.getToken().equals("t_(")) {
                nextToken(true);
                t_condition();
                if(nextToken.getToken().equals("t_)")) {
                    nextToken(true);
                    if(nextToken.getToken().equals("t_{")) {
                        nextToken(true);
                        t_command();
                        
                        if(!nextToken.getToken().equals("t_}"))
                            addError("Esta faltando um  }");                        
                        
                        nextToken(true);
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
            nextToken(true);

            if(nextToken.getToken().equals("t_{")) {
                nextToken(true);
                t_command();

                if(!nextToken.getToken().equals("t_}"))
                    addError("Esta faltando um  }");
                else
                    nextToken(true);
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
            nextToken(true);
        
        if(nextToken.getLexema().equals(")")) 
            nextToken(true);
        else
            addError("Falta um )");
            
        if(nextToken.getToken().equals("t_{"))
            nextToken(true);
        else
            addError("Falta um {");
        
        t_command();
        if(nextToken.getToken().equals("t_}"))
             nextToken(true);
        else
            addError("Falta um }");
    }
    
    private void t_for() {
        if(nextToken.getToken().equals("t_for")) {
            nextToken(true);
            if(nextToken.getToken().equals("t_(")) {
                nextToken(true);
                t_att();                
                
                if(first.getT_condition().contains(nextToken.getToken())) {
                    t_condition();
                    if(nextToken.getToken().equals("t_end")) {
                        nextToken(true);
                        
                        if(first.getT_attribution().contains(nextToken.getToken())) {   
                            t_att();
                            if(nextToken.getToken().equals("t_end"))
                                nextToken(true);
                            if(nextToken.getToken().equals("t_)")) {
                                nextToken(true);

                                if(nextToken.getToken().equals("t_{")) {
                                    nextToken(true);
                                    t_command();

                                    if(nextToken.getToken().equals("t_}"))
                                        nextToken(true);
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
            nextToken(true);
            if(nextToken.getToken().equals("t_(")) {
                nextToken(true);
                t_condition();
                
                if(nextToken.getToken().equals("t_)")) {
                    nextToken(true);
                    
                    if(nextToken.getToken().equals("t_{")) {
                        nextToken(true);
                        t_command();
                        
                        if(nextToken.getToken().equals("t_}"))
                            nextToken(true);                        
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
            nextToken(true);        
            if(nextToken.getToken().equals("t_{")) {
                nextToken(true);
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
        nextToken(true);      
        try {
            t_main();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
        if(nextToken == null)
            addError("Fim de arquivo inesperado");
        else
            nextToken(true); // Ver se nao tem mais nada a ser lido
    }
    
    public List<Error> getErrors() {
        errors.addAll(la.getErrors());
        errors.addAll(semantic.getErrors());
        return errors;
    }
    
    private void addError(String error) {
        int[] lineInformation = la.getLineInformation();
        errors.add(new Error(error, lineInformation[0], lineInformation[1]));
    }
    
    private void nextToken(boolean flag) {
        if(nextToken != null && flag)          
            tokens.add(nextToken);
        nextToken = la.nextToken();
    }
    
    private void attToken(Token t) {
        List<Token> l = new ArrayList<>();
        for(Token tk : this.tokens) {
            if(!tk.getLexema().equals(t.getLexema()))
                l.add(tk);
            else
                l.add(t);
        }
        this.tokens = l;
    }
    
    public boolean finished() {
        return la.finished();
    }

    List<Token> getTable() {
        return tokens;
    }
}