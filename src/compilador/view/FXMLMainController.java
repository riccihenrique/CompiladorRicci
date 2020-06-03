package compilador.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import compilador.compiler.Compiler;
import compilador.compiler.Error;
import compilador.compiler.Token;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fxmisc.richtext.CodeArea;


public class FXMLMainController implements Initializable {
    @FXML
    private JFXButton btNovo;
    @FXML
    private JFXButton btAbir;
    @FXML
    private JFXButton btSalvar;
    @FXML
    private JFXButton btCompilar;  
    @FXML
private TabPane tbpFiles;
    
    private List<String> paths;
    @FXML
    private JFXTextArea taErrors;
    @FXML
    private TabPane tbpSaida;
    
    
    private boolean flag = false;
    @FXML
    private JFXTextArea taTabela;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbpFiles.getStyleClass().add("floating");
        tbpSaida.getStyleClass().add("floating");
        
        new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.isControlDown())
                    flag = true;
                else if(e.getKeyChar() == 's' && flag) {
                    flag = false;
                    SalvarArquivo(null);
                }
                else if(e.getKeyChar() == 'o' && flag) {
                    flag = false;
                    AbiriArquivo(null);
                }
                else if(e.getKeyChar() == 'n' && flag) {
                    flag = false;
                    createTab("undefined *", true);
                }
                else if(e.isAltDown())
                    try {
                        Compilar(null);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
                }
                   
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.isControlDown())
                    flag = true;
                else if(e.getKeyChar() == 's' && flag) {
                    flag = false;
                    SalvarArquivo(null);
                }
                else if(e.getKeyChar() == 'o' && flag) {
                    flag = false;
                    AbiriArquivo(null);
                }
                else if(e.getKeyChar() == 'n' && flag) {
                    flag = false;
                    createTab("undefined *", true);
                }
                else if(e.isAltDown())
                    try {
                        Compilar(null);
                } catch (IOException ex) {
                    Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        paths = new ArrayList<>();
        
        /*path = "./teste.hrr";
        AbiriArquivo(null);*/
    }    
        
    private void createTab(String title, boolean newFile) {
        paths.add("");
        Tab t = new Tab(title);
        t.setStyle("-fx-text-base-color:#fdfdfd;-fx-background-color:#353538");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLContextTab.fxml"));       
        Parent root;
        try {
            root = (Parent) loader.load();  
            FXMLContextTabController c = loader.getController();
            c.newFile(newFile);
            t.setContent(root);
            tbpFiles.getTabs().add(t);
        } catch (IOException ex) {}
    }
             
    @FXML
    private void NovoArquivo(ActionEvent event) throws IOException {
        createTab("untitled *", true);
    }

    @FXML
    private void AbiriArquivo(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HRR File (*.hrr)", "*.hrr"));
        File file;
        //if(paths.get(getIndex()) == "")
            file = chooser.showOpenDialog(null);
        //else
            //file = new File(paths.get(getIndex()));
        
        if(file != null) {
            try {
                FileReader arq = new FileReader(file);
                BufferedReader reader = new BufferedReader(arq);
                
                createTab(file.getName(), false);
                paths.set(tbpFiles.getTabs().size() - 1, file.getAbsolutePath());
                
                tbpFiles.getSelectionModel().select(tbpFiles.getTabs().size() - 1);
                for(Node n : ((AnchorPane)tbpFiles.getTabs().get(tbpFiles.getTabs().size() - 1).getContent()).getChildren()) {
                    if(n instanceof CodeArea) {
                        CodeArea caAux = ((CodeArea) n);
                        if(caAux.getId().equals("caCode")) {
                            String line;
                            String text = "";
                            line = reader.readLine();
                            while(line != null) {
                                text += line + "\n";
                                line = reader.readLine();
                            }
                            caAux.replaceText(text);
                            break;
                        }
                    }                    
                }
            } 
            catch (FileNotFoundException ex) {} catch (IOException ex) {}
        }
    }

    @FXML
    private void SalvarArquivo(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HRR File (*.hrr)", "*.hrr"));
        File file;
        if(paths.get(getIndex()).equals("")) {
            file = chooser.showSaveDialog(null);
            paths.set(getIndex(), file.getAbsolutePath());
        }
        else
           file = new File(paths.get(getIndex()));        

        if(file != null){
            try {
                FileWriter writer = new FileWriter(file, false);
                for(Node n : ((AnchorPane)tbpFiles.getTabs().get(tbpFiles.getSelectionModel().getSelectedIndex()).getContent()).getChildren()) {
                    if(n instanceof CodeArea) {
                        
                        CodeArea caAux = ((CodeArea) n);                        
                        if(caAux.getId().equals("caCode")) {                            
                            writer.write(caAux.getText());
                            break;
                        }
                    }                    
                }
                writer.flush();
                writer.close();
                tbpFiles.getTabs().get(tbpFiles.getSelectionModel().getSelectedIndex()).setText(file.getName());
            } catch (IOException ex){}       
        }
    }

    @FXML
    private void Compilar(ActionEvent event) throws IOException {
        SalvarArquivo(event);
        Compiler compiler = new Compiler(paths.get(getIndex()));
        compiler.Compile();
        
        String msg = "";
        List<Error> err = compiler.getErrors();
        List<Token> tk = compiler.getTableTokens();
        
        for(Token t : tk)
            msg += t + "\n";
        
        taTabela.clear();
        taTabela.setText(msg);
        
        msg = "";
        if(err.isEmpty())
            msg = "Compilaçao executada com sucesso!!!!!!";
        else
            for(Error e : err)
                msg += e + "\n";
        taErrors.clear();
        taErrors.setText(msg);
    }
    
    private int getIndex() {
        return tbpFiles.getSelectionModel().getSelectedIndex();
    }
}