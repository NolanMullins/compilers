JAVA=java
JAVAC=javac
JFLEX=jflex
CLASSPATH=-classpath /usr/share/java/cup.jar:.
CUP=$(JAVA) $(CLASSPATH) java_cup.Main
#CUP=cup

all: Main.class

Main.class: absyn/*.java parser.java sym.java Lexer.java ShowTreeVisitor.java Scanner.java Main.java

%.class: %.java
	$(JAVAC) $(CLASSPATH)  $^

Lexer.java: cm.flex
	$(JFLEX) cm.flex

parser.java: cm.cup
	#$(CUP) -dump -expect 3 tiny.cup
	$(CUP) -expect 3 cm.cup

tiny:
	java $(CLASSPATH) Main fac.tiny

test:
	java $(CLASSPATH) Main data/simple.cm

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class *~
