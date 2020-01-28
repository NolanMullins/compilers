JAVAC=javac
JFLEX=jflex
#JFLEX=/home/fsong/jflex/bin/jflex
CLASS=Pair.class Token.class Lexer.class Scanner.class 

all: $(CLASS)

test: $(CLASS)
	java Scanner < sampleData.txt > result.tok

tokenize: $(CLASS)
	java Scanner < newsdata.txt > result.tok

%.class: %.java
	$(JAVAC) $^

Lexer.java: token.flex
	$(JFLEX) token.flex

clean:
	rm -f Lexer.java *.class *~ 
