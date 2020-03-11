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
	$(CUP) -expect 3 cm.cup

test:
	java $(CLASSPATH) Main data/1.cm
	java $(CLASSPATH) Main data/2.cm
	java $(CLASSPATH) Main data/3.cm
	java $(CLASSPATH) Main data/4.cm
	java $(CLASSPATH) Main data/5.cm

run:
	java $(CLASSPATH) Main data/1.cm

sample1:
	java $(CLASSPATH) Main data/sample1.cm

sample2:
	java $(CLASSPATH) Main data/sample2.cm

sample3:
	java $(CLASSPATH) Main data/sample3.cm

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class *~
