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

test:
	java $(CLASSPATH) Main data/test1.cm
	java $(CLASSPATH) Main data/test2.cm
	java $(CLASSPATH) Main data/test3.cm
	java $(CLASSPATH) Main data/test4.cm

error:
	java $(CLASSPATH) Main data/error.cm

sample1:
	java $(CLASSPATH) Main data/sample1.cm

sample2:
	java $(CLASSPATH) Main data/sample2.cm

sample3:
	java $(CLASSPATH) Main data/sample3.cm

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class *~
