/*
  File Name: tiny.flex
  JFlex specification for the TINY language
*/
   
import java.util.ArrayList;
import java.util.Map.Entry;

%%
   
%class Lexer
%type Token
%line
%column
    
%eofval{
  //System.out.println("*** end of file reached");
  return null;
%eofval};

%{

    private static ArrayList<Pair> tagStack = new ArrayList<>();
    private static String[] relevant = {"doc", "text", "date", "docno", "headline", "length", "p"};

    public static String getTagName(String line) 
    {
        return line.replaceAll("([<][/])|[<>]", "").split(" ")[0];
    }

    public static boolean includeElement() 
    {
        //Loop through tagStack looking for filtered tags
        for (int a = 0; a < tagStack.size(); a++)
        {
            if (tagStack.get(a).relevant==false)
            {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<String> getRemainingTags() 
    {
        ArrayList<String> tags = new ArrayList<>();
        for (Pair tag : tagStack)
            tags.add(tag.tag);
        return tags;
    }

    public static void pushTag(String tag)
    {
        for(String check : relevant)
        {
            if (check.equals(tag.toLowerCase()))
            {
                tagStack.add(new Pair(tag, true));
                return;
            }
        }
        tagStack.add(new Pair(tag, false));
    }
%};

/* A line terminator is a \r (carriage return), \n (line feed), or
   \r\n. */
LineTerminator = \r|\n|\r\n
   
/* White space is a line terminator, space, tab, or form feed. */
WhiteSpace     = {LineTerminator} | [ \t\f]
   
%%
   
/*
   This section contains regular expressions and actions, i.e. Java
   code, that will be executed when the scanner matches the associated
   regular expression. */
   

//Open tag
[<]([a-zA-Z=\"0-9 ])+[>]    {   
                                String openTag = yytext();
                                String tag = getTagName(openTag);
                                pushTag(tag);
                                if (includeElement())
                                    return new Token(Token.OPEN, tag, yyline, yycolumn);
                            }
//Close tag
[<][\/][a-zA-Z ]*[>] {   
                        String closeTag = yytext();
                        int i = tagStack.size()-1;
                        String tag = getTagName(closeTag);
                        if (tag.equals(tagStack.get(i).tag)) {
                            if (includeElement())
                            {
                                tagStack.remove(i);
                                return new Token(Token.CLOSE, tag, yyline, yycolumn);
                            }
                            tagStack.remove(i);
                        } else {
                            return new Token(Token.ERROR_CLOSE, tag, yyline, yycolumn);
                        }
                    }

//Number
[-+]?[0-9]+([.][0-9]+)? { 
                            if (includeElement())
                                return new Token(Token.NUMBER, yytext(), yyline, yycolumn);
                        }

//APOSTROPHIZED
([a-zA-Z0-9]+['][a-zA-Z0-9]+)+  { 
                                    if (includeElement())
                                        return new Token(Token.APOSTROPHIZED, yytext(), yyline, yycolumn); 
                                }

//HYPHENATED
([a-zA-Z0-9]+[-][a-zA-Z0-9]+)+  { 
                                    if (includeElement())
                                        return new Token(Token.HYPHENATED, yytext(), yyline, yycolumn); 
                                }

//Word
([a-zA-Z]+[0-9]*)+      { 
                            if (includeElement())
                                return new Token(Token.WORD, yytext(), yyline, yycolumn); 
                        }

//punctuation
[.!?,;:'\(\)\[\]\"-@#$%\^&\*]     { 
                                    if (includeElement())
                                        return new Token(Token.PUNCTUATION, yytext(), yyline, yycolumn); 
                                }

//New lines
{LineTerminator}+       { /*return new Token(Token.NL, yytext(), yyline, yycolumn);*/ }

{WhiteSpace}+           { /* skip whitespace */ }   

.                       { return new Token(Token.ERROR, yytext(), yyline, yycolumn); }
