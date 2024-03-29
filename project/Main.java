/*
	Created by: Fei Song
	File Name: Main.java
	To Build: 
	After the scanner, tiny.flex, and the parser, tiny.cup, have been created.
		javac Main.java
	
	To Run: 
		java -classpath /usr/share/java/cup.jar:. Main fac.tiny

	where fac.tiny is an test input file for the tiny language.
*/
	 
import java.io.*;
import absyn.*;
	 
class Main {
	static public void main(String argv[]) {  
		boolean SHOW_TREE = false;
		boolean generateASM = false;
		int sFlag = 0; // Assume the -s was not passed in
		String fileName = "";

		if (argv.length > 2) {
			System.out.println("ERROR: Too many arguments passed in");
			System.exit(1);
		} 

		for(int i=0; i< argv.length; i++)
		{
			if (argv[i].equals("-s")) {
				sFlag = 1;
			} else if (argv[i].equals("-a")) {
				SHOW_TREE = true;
			} else if (argv[i].equals("-c")) {
				generateASM = true;
			} else {
				fileName = argv[i];
			}
		}

		/* Start the parser */
		try {
			parser p = new parser(new Lexer(new FileReader(fileName)));
			Absyn result = (Absyn)(p.parse().value);      
			if (result != null) {
				SemanticAnalyzer analyzer = new SemanticAnalyzer(sFlag);
				result.accept(analyzer, 0);
			}
			if (SHOW_TREE && result != null) {
				 System.out.println("The abstract syntax tree is:");
				 ShowTreeVisitor visitor = new ShowTreeVisitor();
				 result.accept(visitor, 0); 
			}
			if (generateASM) {
                ASMGenerator asm = new ASMGenerator(fileName);
				result.accept(asm, 0);
			}
		} catch (Exception e) {
			/* do cleanup here -- possibly rethrow e */
			e.printStackTrace();
		}
	}
}


