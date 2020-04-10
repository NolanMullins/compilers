JAVA=java
JAVAC=javac
JFLEX=jflex
CLASSPATH=-classpath /usr/share/java/cup.jar:.
CUP=$(JAVA) $(CLASSPATH) java_cup.Main
#CUP=cup

all: Main.class

Main.class: absyn/*.java parser.java sym.java Lexer.java SemanticAnalyzer.java ShowTreeVisitor.java ASMDecEntry.java ASMUtils.java ASMSymbolTable.java ASMGenerator.java Scanner.java Main.java

%.class: %.java
	$(JAVAC) $(CLASSPATH)  $^

Lexer.java: cm.flex
	$(JFLEX) cm.flex

parser.java: cm.cup
	$(CUP) -expect 3 cm.cup

test:
	java $(CLASSPATH) Main data/0.cm
	java $(CLASSPATH) Main data/1.cm
	java $(CLASSPATH) Main data/2.cm
	java $(CLASSPATH) Main data/3.cm
	java $(CLASSPATH) Main data/4.cm
	java $(CLASSPATH) Main data/5.cm
	java $(CLASSPATH) Main data/6.cm
	java $(CLASSPATH) Main data/7.cm
	java $(CLASSPATH) Main data/8.cm
	java $(CLASSPATH) Main data/9.cm

run:
	java $(CLASSPATH) Main -c data/main.cm 

sort: 
	java $(CLASSPATH) Main -a TMSimulator/sort.cm 

tm: tm.c
	gcc tm.c -o tm

run-a:
	java $(CLASSPATH) Main -a data/1.cm
run-s:
	java $(CLASSPATH) Main -s data/1.cm
run-c:
	java $(CLASSPATH) Main -c data/1.cm

sample1:
	java $(CLASSPATH) Main data/sample1.cm

sample2:
	java $(CLASSPATH) Main data/sample2.cm

sample3:
	java $(CLASSPATH) Main data/sample3.cm

clean:
	rm -f parser.java Lexer.java sym.java *.class absyn/*.class *~ tm
