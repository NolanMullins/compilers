/*********************
 * Nolan Mullins
 * 0939720
 * Class the utilize JFlex utility
 * to parse files
 * 
 * I/O through std
 *********************/

import java.io.InputStreamReader;
import javax.swing.text.BadLocationException;
import java.util.ArrayList;
import org.omg.CORBA.BAD_CONTEXT; 

public class Scanner {
    private Lexer scanner = null;
    public int badClose = 0;

    public Scanner(Lexer lexer) {
        scanner = lexer;
    }

    public Token getNextToken() throws java.io.IOException {
        Token t = scanner.yylex();
        if (t == null)
            return null;
        //Record misplaced closed tags
        if (t.m_type == Token.ERROR_CLOSE)
            badClose++;
        return t;
    }

    public static void main(String argv[]) {
        try {
            Scanner scanner = new Scanner(new Lexer(new InputStreamReader(System.in)));

            Token tok = null;
            while ((tok = scanner.getNextToken()) != null)
                System.out.println(tok);
                    
            ArrayList<String> tags = Lexer.getRemainingTags();
            System.out.println("\n********************");

            if (tags.size() > 0)
            {
                System.out.println("Remaining open tags:");
                for (String tag : tags)
                    System.out.println(tag);
            }

            System.out.println("Number of misplaced closed tags: "+scanner.badClose);
        } catch (Exception e) {
            System.out.println("Unexpected exception:");
            e.printStackTrace();
        }
    }
}
