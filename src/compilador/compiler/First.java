package compilador.compiler;

import java.util.ArrayList;
import java.util.List;

public class First {
    private final List<String> first_main;
    private List<String> first_declaration;
    private List<String> first_command;
    private List<String> first_atribution;
    private List<String> first_operation;
    private List<String> first_decision;
    private List<String> first_repetition;
    private List<String> first_if;
    private List<String> first_condition;
    private List<String> first_while;
    private List<String> first_for;
    private List<String> first_logicCondition;   
  
    private List<String> first_logicPlus;
    private List<String> first_relationalCondition;
    
    
    public First() {
        first_main = new ArrayList<>();
        first_declaration = new ArrayList<>();
        first_command = new ArrayList<>();
        first_decision = new ArrayList<>();
        first_repetition = new ArrayList<>();
        first_if = new ArrayList<>();
        first_condition = new ArrayList<>();
        first_while = new ArrayList<>();
        first_for = new ArrayList<>();
        first_logicCondition = new ArrayList<>();
        first_relationalCondition = new ArrayList<>();
        first_atribution = new ArrayList<>();
        first_logicPlus = new ArrayList<>();
        first_operation = new ArrayList<>();
        
        first_main.add("t_main");
        
        first_declaration.add("t_type");
        
        first_if.add("t_if");
        first_for.add("t_for");
        first_while.add("t_while");
        
        first_command.add("t_id");
        first_command.addAll(first_if);
        first_command.addAll(first_while);
        first_command.addAll(first_for);
        
        first_decision.addAll(first_if);
        
        first_repetition.addAll(first_for);
        first_repetition.addAll(first_while);
        
        first_relationalCondition.add("t_num");
        first_relationalCondition.add("t_id");
        
        
        first_logicCondition.add("t_bool");      
        first_logicCondition.add("t_logic");
        first_logicCondition.addAll(first_relationalCondition);
        
        first_condition.addAll(first_logicCondition);
        
        first_atribution.add("t_id");
        
        first_logicPlus.add("t_logic");
        
        first_operation.add("t_id");
        first_operation.add("t_num");
    }

    public List<String> getFirst_main() {
        return first_main;
    }

    public List<String> getFirst_declaration() {
        return first_declaration;
    }

    public List<String> getFirst_command() {
        return first_command;
    }

    public List<String> getFirst_decision() {
        return first_decision;
    }

    public List<String> getFirst_repetition() {
        return first_repetition;
    }

    public List<String> getFirst_if() {
        return first_if;
    }

    public List<String> getFirst_condition() {
        return first_condition;
    }

    public List<String> getFirst_while() {
        return first_while;
    }

    public List<String> getFirst_for() {
        return first_for;
    }

    public List<String> getFirst_logicCondition() {
        return first_logicCondition;
    }

    public List<String> getFirst_relationalCondition() {
        return first_relationalCondition;
    }

    public List<String> getFirst_atribution() {
        return first_atribution;
    }

    public List<String> getFirst_logicPlus() {
        return first_logicPlus;
    }

    public List<String> getFirst_operation() {
        return first_operation;
    }
}