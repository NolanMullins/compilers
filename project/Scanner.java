import java.io.InputStreamReader;
import javax.swing.text.BadLocationException;
import java.util.ArrayList;
import org.omg.CORBA.BAD_CONTEXT; 

//TODO
//Output any unclosed tags
//Output any errors

public class Scanner {
    private Lexer scanner = null;
    public int badClose = 0;

    public Scanner(Lexer lexer) {
        scanner = lexer;
    }

    //Idea to filter out data, when opening a bad tag, we loop till that tag is closed
    public Token getNextToken() throws java.io.IOException {
        Token t = scanner.yylex();
        if (t == null)
            return null;
        if (t.m_type == Token.ERROR_CLOSE)
            badClose++;
        return t;
    }

    public static void main(String argv[]) {
        try {
            Scanner scanner = new Scanner(new Lexer(new InputStreamReader(System.in)));
            Token tok = null;
            while ((tok = scanner.getNextToken()) != null)
                if (tok.m_type != Token.ERROR_CLOSE)
                    System.out.println(tok);
            ArrayList<String> tags = Lexer.getRemainingTags();
            System.out.println("\n********************");
            System.out.println("Remaining open tags:");
            for (String tag : tags)
                System.out.println(tag);
            System.out.println("Number of misplaced closed tags: "+scanner.badClose);
        } catch (Exception e) {
            System.out.println("Unexpected exception:");
            e.printStackTrace();
        }
    }
}
