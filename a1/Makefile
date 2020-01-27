JAVAC=javac
JFLEX=jflex
#JFLEX=/home/fsong/jflex/bin/jflex

all: Token.class Lexer.class Scanner.class

test: Token.class Lexer.class Scanner.class
	java Scanner < sampleData.txt 

tokenize: Token.class Lexer.class Scanner.class
	java Scanner sampleData.txt > result.tok

%.class: %.java
	$(JAVAC) $^

Lexer.java: token.flex
	$(JFLEX) token.flex

clean:
	rm -f Lexer.java *.class *~ *.out
