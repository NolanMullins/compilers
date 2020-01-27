/*
  File Name: tiny.flex
  JFlex specification for the TINY language
*/
   
import java.util.ArrayList;

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
    private static ArrayList<String> tagStack = new ArrayList<String>();
    // static methods such as getTagName can be defined here as well
    //System.out.println(getTagName());
%};

/* A line terminator is a \r (carriage return), \n (line feed), or
   \r\n. */
LineTerminator = \r|\n|\r\n
   
/* White space is a line terminator, space, tab, or form feed. */
WhiteSpace     = {LineTerminator} | [ \t\f]
   
/* A literal integer is is a number beginning with a number between
   one and nine followed by zero or more numbers between zero and nine
   or just a zero.  */
//digit = [0-9]
//number = {digit}+
   
/* A identifier integer is a word beginning a letter between A and
   Z, a and z, or an underscore followed by zero or more letters
   between A and Z, a and z, zero and nine, or an underscore. */
//letter = [a-zA-Z]
//identifier = {letter}+
   
%%
   
/*
   This section contains regular expressions and actions, i.e. Java
   code, that will be executed when the scanner matches the associated
   regular expression. */
   

//Open tag
[<]([a-zA-Z=\"0-9 ])+[>]    {   
                                String openTag = yytext();
                                String tag = openTag.replaceAll("[<>]", "").split(" ")[0];
                                tagStack.add(tag);
                                return new Token(Token.OPEN, yytext(), yyline, yycolumn);
                            }
//Close tag
[<][\/][a-zA-Z]*[>] {   
                        String closeTag = yytext();
                        int i = tagStack.size()-1;
                        String tag = closeTag.replaceAll("[</> ]", "").split(" ")[0];
                        if (tag.equals(tagStack.get(i))) {
                            tagStack.remove(i);
                            return new Token(Token.CLOSE, yytext(), yyline, yycolumn);
                        } else {
                            //System.out.println("ERROR with tags");
                            //System.out.println(tagStack.get(i) + " != " + tag);
                        }
                        return new Token(Token.ERROR, yytext(), yyline, yycolumn);
                    }

//Number
[-+]?[0-9]+([.][0-9]+)? { return new Token(Token.NUMBER, yytext(), yyline, yycolumn);}

//Word
([a-zA-Z]+[0-9]*)+      {return new Token(Token.WORD, yytext(), yyline, yycolumn);}

//APOSTROPHIZED
([a-zA-Z0-9]+['][a-zA-Z0-9]+)+    {return new Token(Token.APOSTROPHIZED, yytext(), yyline, yycolumn);}

//HYPHENATED
([a-zA-Z0-9]+[-][a-zA-Z0-9]+)+    {return new Token(Token.HYPHENATED, yytext(), yyline, yycolumn);}

//punctuation
[.!?,;:'\(\)\[\]\"]     { return new Token(Token.PUNCTUATION, yytext(), yyline, yycolumn); }

//New lines
{LineTerminator}+       { /*return new Token(Token.NL, yytext(), yyline, yycolumn);*/ }

{WhiteSpace}+           { /* skip whitespace */ }   

.                       { return new Token(Token.ERROR, yytext(), yyline, yycolumn); }
