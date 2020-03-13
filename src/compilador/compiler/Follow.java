package compilador.compiler;

import java.util.ArrayList;
import java.util.List;

public class Follow {
    private final List<String> follow_main;
    private List<String> follow_declaration;
    private List<String> follow_command;
    private List<String> follow_atribution;
    private List<String> follow_operation;
    private List<String> follow_decision;
    private List<String> follow_repetition;
    private List<String> follow_if;
    private List<String> follow_condition;
    private List<String> follow_while;
    private List<String> follow_for;
    private List<String> follow_logicCondition;    
    private List<String> follow_logicPlus;
    private List<String> follow_relationalCondition;
    
    public Follow() {
        follow_main = new ArrayList<>();
        follow_declaration = new ArrayList<>();
        follow_command = new ArrayList<>();
        follow_decision = new ArrayList<>();
        follow_repetition = new ArrayList<>();
        follow_if = new ArrayList<>();
        follow_condition = new ArrayList<>();
        follow_while = new ArrayList<>();
        follow_for = new ArrayList<>();
        follow_logicCondition = new ArrayList<>();
        follow_relationalCondition = new ArrayList<>();
        follow_atribution = new ArrayList<>();
        follow_logicPlus = new ArrayList<>();
        follow_operation = new ArrayList<>();
        
        follow_declaration.add("t_]");
        
        follow_command.add("t_}");
        follow_command.add("t_id");
        follow_command.add("t_if");
        follow_command.add("t_while");
        follow_command.add("t_for");
        
        follow_atribution.add("t_end");
        follow_atribution.add("t_)");
        
        
        follow_operation.add("t_)");
        follow_operation.add("t_end");
        
        follow_decision.addAll(follow_command);
        
        follow_repetition.addAll(follow_command);
        
        follow_if.addAll(follow_command);
        
        follow_while.addAll(follow_command);
        
        follow_for.addAll(follow_command);
        
        follow_condition.addAll(follow_atribution);
        
        follow_logicCondition.addAll(follow_atribution);
        
        follow_logicPlus.addAll(follow_logicCondition);
        follow_logicPlus.add("t_logic");
        
        follow_relationalCondition.addAll(follow_logicPlus);
        
        follow_atribution.addAll(follow_command);
    }

    public List<String> getFollow_main() {
        return follow_main;
    }

    public List<String> getFollow_declaration() {
        return follow_declaration;
    }

    public List<String> getFollow_command() {
        return follow_command;
    }

    public List<String> getFollow_atribution() {
        return follow_atribution;
    }

    public List<String> getFollow_operation() {
        return follow_operation;
    }

    public List<String> getFollow_decision() {
        return follow_decision;
    }

    public List<String> getFollow_repetition() {
        return follow_repetition;
    }

    public List<String> getFollow_if() {
        return follow_if;
    }

    public List<String> getFollow_condition() {
        return follow_condition;
    }

    public List<String> getFollow_while() {
        return follow_while;
    }

    public List<String> getFollow_for() {
        return follow_for;
    }

    public List<String> getFollow_logicCondition() {
        return follow_logicCondition;
    }

    public List<String> getFollow_logicPlus() {
        return follow_logicPlus;
    }

    public List<String> getFollow_relationalCondition() {
        return follow_relationalCondition;
    }
}
