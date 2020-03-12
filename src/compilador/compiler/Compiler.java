package compilador.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Compiler {
    
    private String code = "";
    private List<Error> errors;
    private SyntaticAnalysis sa;
    
    public Compiler(String filePath) throws IOException {  
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
        } catch (IOException ex) {System.out.println(ex.getMessage());}
    }
    
    public void Compile() {
        sa = new SyntaticAnalysis(code);
        sa.Analyse();        
        errors = sa.getErrors();
    }
    
    public List<Error> getErrors() {
        return errors;
    }
    
    public boolean finished() {
        return sa.finished();
    }
}