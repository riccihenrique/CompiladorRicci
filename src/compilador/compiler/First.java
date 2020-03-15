package compilador.compiler;

import java.util.ArrayList;
import java.util.List;

public class First {
    private final List<String> t_main;
    private List<String> t_declaration;
    private List<String> t_command;
    private List<String> t_attribution;
    private List<String> t_operation;
    private List<String> t_decision;
    private List<String> t_repetition;
    private List<String> t_if;
    private List<String> t_condition;
    private List<String> t_while;
    private List<String> t_for;
    private List<String> t_logicC;   
  
    private List<String> t_logicCPlus;
    private List<String> t_relationalC;
    
    
    public First() {
        t_main = new ArrayList<>();
        t_declaration = new ArrayList<>();
        t_command = new ArrayList<>();
        t_decision = new ArrayList<>();
        t_repetition = new ArrayList<>();
        t_if = new ArrayList<>();
        t_condition = new ArrayList<>();
        t_while = new ArrayList<>();
        t_for = new ArrayList<>();
        t_logicC = new ArrayList<>();
        t_relationalC = new ArrayList<>();
        t_attribution = new ArrayList<>();
        t_logicCPlus = new ArrayList<>();
        t_operation = new ArrayList<>();
        
        t_main.add("t_main");
        
        t_declaration.add("t_type");
        
        t_if.add("t_if");
        t_for.add("t_for");
        t_while.add("t_while");
        
        t_command.add("t_id");
        t_command.addAll(t_if);
        t_command.addAll(t_while);
        t_command.addAll(t_for);
        
        t_decision.addAll(t_if);
        
        t_repetition.addAll(t_for);
        t_repetition.addAll(t_while);
        
        t_relationalC.add("t_$");        
        
        t_logicC.add("t_bool");
        t_logicC.add("t_id");
        t_logicC.add("t_logic");
        t_logicC.addAll(t_relationalC);
        
        
        t_condition.addAll(t_logicC);
        
        t_attribution.add("t_id");
        
        t_logicCPlus.add("t_logic");
        
        t_operation.add("t_id");
        t_operation.add("t_num");
    }

    public List<String> getT_main() {
        return t_main;
    }

    public List<String> getT_declaration() {
        return t_declaration;
    }

    public List<String> getT_command() {
        return t_command;
    }

    public List<String> getT_attribution() {
        return t_attribution;
    }

    public List<String> getT_operation() {
        return t_operation;
    }

    public List<String> getT_decision() {
        return t_decision;
    }

    public List<String> getT_repetition() {
        return t_repetition;
    }

    public List<String> getT_if() {
        return t_if;
    }

    public List<String> getT_condition() {
        return t_condition;
    }

    public List<String> getT_while() {
        return t_while;
    }

    public List<String> getT_for() {
        return t_for;
    }

    public List<String> getT_logicC() {
        return t_logicC;
    }

    public List<String> getT_logicCPlus() {
        return t_logicCPlus;
    }

    public List<String> getT_relationalC() {
        return t_relationalC;
    }
}