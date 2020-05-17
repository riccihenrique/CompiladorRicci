package compilador.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Compiler {
    
    private String code = "";
    private List<Error> errors;
    private SyntaticAnalysis sa;
    private CodeGenerator codeGenerator;
    
    public Compiler(String filePath) throws IOException {
        errors = new ArrayList<>();
        String aux = "";
        File file = new File(filePath);
        FileReader arq;
        try {
            arq = new FileReader(file);
            BufferedReader reader = new BufferedReader(arq);
            
            aux = reader.readLine();
            while(aux != null) {
                code += aux + "\n";
                aux = reader.readLine();
            }            
        } 
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void Compile() {
        sa = new SyntaticAnalysis(code);
        sa.Analyse();
       
        errors.addAll(sa.getErrors());        
        
        if(errors.isEmpty()) {
            if(!sa.finished())
                errors.add(new Error("O programa nao pode ser compilado", 1, 1));
            else {
                codeGenerator = new CodeGenerator(sa.getTable());
                codeGenerator.Analyze();
                codeGenerator.generateCode();
            }
        }       
    }
    
    public List<Error> getErrors() {
        return errors;
    }
    
    public boolean finished() {
        return sa.finished();
    }

    public List<Token> getTableTokens() {
        return codeGenerator.getTokens();
    }
}