import absyn.*;

import java.lang.String;
import java.util.ArrayList;

public class SemanticAnalyzer implements AbsynVisitor {

    private int depth;
    private HashMap<String, ArrayList<DecEntry>> symtable;

    class DefEntry {String name; Dec def; int depth;}

    public SemanticAnalyzer() {
        depth = 0;
        symtable = new HashMap<>();
    }

    /*
    //Add new scope
    DefEntry entry = new DefEntry(name, def, depth);
    ArrayList<DefEntry> entries = new ArrayList<>()l
    entries.add(entry);
    symtable.put(name, entries);



    //His hashtable / symtable: 
    //Basically is a map of current accessible variables
    //When entering a new block we increment our depth
    //Add any new declarations to the map (with current  depth)
    //When leave a scope remove all declarations with the current depth
    //Reduce depth by 1 

    //Checking scope ex
    String name;
    if (symtable.contains(name)) {
        ArrayList<DefEntry> entries = symtable.get(name);
        //This will get us the highest depth accessible variable 
        entries.get(enties.size()-1);
    }
    */


    final static int SPACES = 4;

    private void indent(int level) {
        for (int i = 0; i < level * SPACES; i++)
            System.out.print(" ");
    }

    public void visit(ExpList expList, int level) {
        while (expList != null && expList.head != null) {
            expList.head.accept(this, level);
            expList = expList.tail;
        }
    }

    public void visit(AssignExp exp, int level) {
        indent(level);
        System.out.println("AssignExp:");
        level++;
        exp.lhs.accept(this, level);
        exp.rhs.accept(this, level);
    }

    public void visit(IfExp exp, int level) {
        indent(level);
        System.out.println("IfExp:");
        level++;
        exp.test.accept(this, level);
        exp.thenpart.accept(this, level);
        if (exp.elsepart != null)
            exp.elsepart.accept(this, level);
    }

    public void visit(IntExp exp, int level) {
        indent(level);
        System.out.println("IntExp: " + exp.value);
    }

    public void visit(OpExp exp, int level) {
        indent(level);
        System.out.print("OpExp:");
        switch (exp.op) {
        case OpExp.PLUS:
            System.out.println(" + ");
            break;
        case OpExp.MINUS:
            System.out.println(" - ");
            break;
        case OpExp.MUL:
            System.out.println(" * ");
            break;
        case OpExp.DIV:
            System.out.println(" / ");
            break;
        case OpExp.EQ:
            System.out.println(" = ");
            break;
        case OpExp.LT:
            System.out.println(" < ");
            break;
        case OpExp.GT:
            System.out.println(" > ");
            break;
        default:
            System.out.println("Unrecognized operator at line " + exp.row + " and column " + exp.col);
        }
        level++;
        exp.left.accept(this, level);
        exp.right.accept(this, level);
    }

    public void visit(ReadExp exp, int level) {
        indent(level);
        System.out.println("ReadExp:");
        exp.input.accept(this, ++level);
    }

    public void visit(RepeatExp exp, int level) {
        indent(level);
        System.out.println("RepeatExp:");
        level++;
        exp.exps.accept(this, level);
        exp.test.accept(this, level);
    }

    public void visit(VarExp exp, int level) {
        indent(level);
        System.out.println("VarExp: ");
        exp.value.accept(this, level);
    }

    public void visit(WriteExp exp, int level) {
        indent(level);
        System.out.println("WriteExp:");
        exp.output.accept(this, ++level);
    }

    public void visit(NameTy t, int level) {
        indent(level);

        // Get the string of the represented int
        String typeString = "";
        if (t.type == 0) {
            typeString = "int";
        } else {
            typeString = "void";
        }

        System.out.println("NameTy: " + typeString);
    }

    public void visit(VarDecList varDecList, int level) {
        while (varDecList != null && varDecList.head != null) {
            varDecList.head.accept(this, level);
            varDecList = varDecList.tail;
        }
    }

    public void visit(DecList decList, int level) {
        while (decList != null && decList.head != null) {
            decList.head.accept(this, level);
            decList = decList.tail;
        }
    }

    public void visit(ArrayDec arr, int level) {
        indent(level);
        System.out.println("ArrayDec: ");
        arr.type.accept(this, ++level);
        indent(++level);
        System.out.println("Name: " + arr.name);
        if (arr.size != null)
            arr.size.accept(this, ++level);
    }

    public void visit(SimpleDec dec, int level) {
        indent(level);
        System.out.println("SimpleDec: " + String.valueOf(dec.name));
        dec.type.accept(this, ++level);
    }

    public void visit(FunctionDec dec, int level) {
        indent(level);
        System.out.println("FunctionDec: ");

        dec.type.accept(this, ++level);
        indent(++level);
        System.out.println("Name: " + dec.func);

        dec.params.accept(this, ++level);
        dec.body.accept(this, ++level);
    }

    public void visit(CompoundExp exp, int level) {
        indent(level);
        System.out.println("CompoundExp: ");
        if (exp.decs != null)
            exp.decs.accept(this, ++level);
        if (exp.exps != null)
            exp.exps.accept(this, ++level);
    }

    public void visit(ReturnExp e, int level) {
        indent(level);
        System.out.println("ReturnExp: ");
        if (e.exp != null)
            e.exp.accept(this, ++level);
    }

    public void visit(WhileExp exp, int level) {
        indent(level);
        System.out.println("WhileExp: ");

        exp.body.accept(this, ++level);
        exp.test.accept(this, ++level);
    }

    public void visit(CallExp exp, int level) {
        indent(level);
        System.out.println("CallExp: " + exp.func + " ");
        exp.args.accept(this, ++level);
    }

    public void visit(NilExp exp, int level) {
        indent(level);
        System.out.println("NilExp: ");
    }

    public void visit(EpsilonExp exp, int level) {
        indent(level);
    }

    public void visit(IndexVar var, int level) {
        indent(level);
        System.out.println("IndexVar: " + var.name + " ");
        var.index.accept(this, ++level);
    }

    public void visit(SimpleVar var, int level) {
        indent(level);
        System.out.println("SimpleVar: " + var.name + " ");
    }
}
