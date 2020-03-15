package compilador.compiler;

import java.util.ArrayList;
import java.util.List;

public class Follow {
    private List<String> t_main;
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
    
    public Follow() {
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
        
        t_declaration.add("t_]");
        
        t_command.add("t_}");
        
        t_attribution.add("t_end");
        
        t_operation.add("t_end");
        
        t_decision.addAll(t_command);
        t_decision.add("t_id");
        t_decision.add("t_if");
        t_decision.add("t_while");
        t_decision.add("t_for");
        
        t_repetition.addAll(t_decision);
        
        t_if.addAll(t_decision);
        
        t_while.addAll(t_repetition);
        
        t_for.addAll(t_repetition);
        
        t_condition.add("t_)");
        t_condition.add("t_end");
        
        t_logicC.addAll(t_condition);
        
        t_logicCPlus.addAll(t_logicC);
        
        t_relationalC.addAll(t_logicCPlus);        
        t_relationalC.add("t_logic");
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
