package compilador.view;

import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class FXMLContextTabController implements Initializable {   
    @FXML
    private CodeArea caCode;
    private boolean newFile;
    private char character;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        caCode.setParagraphGraphicFactory(LineNumberFactory.get(caCode));
        Subscription cleanupWhenNoLongerNeedIt = caCode             
                .multiPlainChanges()              
                .successionEnds(Duration.ofMillis(30))
                .subscribe(ignore -> caCode.setStyleSpans(0, computeHighlighting(caCode.getText())));

        caCode.setOnKeyTyped((event) -> {            
            if (event.getCharacter().equals("\t"))
                caCode.replaceText(caCode.getCaretPosition() - 1, caCode.getCaretPosition(), "    ");
            else if(event.getCharacter().equals("{")) {
                caCode.replaceText(caCode.getCaretPosition() - 1, caCode.getCaretPosition(), "{}");
                caCode.moveTo(caCode.getCaretPosition() - 1); 
            }                
            else if(event.getCharacter().equals("[")) {
                caCode.replaceText(caCode.getCaretPosition() - 1, caCode.getCaretPosition(), "[]");
                caCode.moveTo(caCode.getCaretPosition() - 1); 
            }               
            else if(event.getCharacter().equals("(")) {
                caCode.replaceText(caCode.getCaretPosition() - 1, caCode.getCaretPosition(), "()");
                caCode.moveTo(caCode.getCaretPosition() - 1);                
            }            
        });
        caCode.getStylesheets().add(getClass().getResource("codeArea.css").toExternalForm());    
    }  
    
    /*
        Implementado de:
        https://github.com/FXMisc/RichTextFX/blob/master/richtextfx/src/main/java/org/fxmisc/richtext/CodeArea.java
    */
    
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass
                    = matcher.group("KEYWORD") != null ? "keyword"
                    : matcher.group("TYPE") != null ? "type"
                    : matcher.group("COMMAND") != null ? "command"
                    : matcher.group("NUMBER") != null ? "number"
                    : matcher.group("PAREN") != null ? "paren"
                    : matcher.group("BRACE") != null ? "brace"
                    : matcher.group("BRACKET") != null ? "bracket"
                    : matcher.group("SEMICOLON") != null ? "semicolon"
                    : matcher.group("COMMENT") != null ? "comment"
                    : matcher.group("STRING") != null ? "string"
                    : matcher.group("CHAR") != null ? "char"
                    : null;

            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private static final String[] KEYWORDS = {"ricci", "var"},
            KEYTYPES = {"int", "double", "bool", "char", "string", "true", "false"},
            KEYCOMMAND = {"if", "else", "while", "for"};

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String TYPES_PATTERN = "\\b(" + String.join("|", KEYTYPES) + ")\\b";
    private static final String COMMAND_PATTERN = "\\b(" + String.join("|", KEYCOMMAND) + ")\\b";
    private static final String NUMBER_PATTERN = "\\d";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/" + "|" + "/\\*(.|\\R)*?$";
    private static final String STRING = "\\\"(.|\\R)*\\\"";
    private static final String CHAR = "\\'(.|\\R)*\\'";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ')' // #c586c0
            + "|(?<TYPE>" + TYPES_PATTERN + ')' // #569cd6
            + "|(?<COMMAND>" + COMMAND_PATTERN + ')' // #
            + "|(?<PAREN>" + PAREN_PATTERN + ')'
            + "|(?<BRACE>" + BRACE_PATTERN + ')'
            + "|(?<BRACKET>" + BRACKET_PATTERN + ')'
            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ')'
            + "|(?<COMMENT>" + COMMENT_PATTERN + ')'
            + "|(?<NUMBER>" + NUMBER_PATTERN + ')'
            + "|(?<STRING>" + STRING + ')'
            + "|(?<CHAR>" + CHAR + ')'
    );

    void newFile(boolean newFile) {
        this.newFile = newFile;
        if(newFile)
            caCode.appendText("ricci {\n    var [\n\n    ]\n}");
    }
}
