import absyn.*;

import java.lang.String;
import java.util.*;


public class SemanticAnalyzer implements AbsynVisitor 
{

    class DecEntry 
    {
        String name; 
        Dec dec; 
        int depth;
        public DecEntry(String name, Dec dec, int depth) 
        {
            this.name = name;
            this.dec = dec;
            this.depth = depth;
        }
    }

    private int depth;
    private HashMap<String, ArrayList<DecEntry>> symtable;


    public SemanticAnalyzer() {
        depth = 0;
        symtable = new HashMap<>();
    }

    //His hashtable / symtable: 
    //Basically is a map of current accessible variables
    //When entering a new block we increment our depth
    //Add any new declarations to the map (with current  depth)
    //When leave a scope remove all declarations with the current depth
    //Reduce depth by 1 

    /*
    //Checking scope ex
    String name;
    if (symtable.contains(name)) {
        ArrayList<DefEntry> entries = symtable.get(name);
        //This will get us the highest depth accessible variable 
        entries.get(enties.size()-1);
    }
    */

    //todo start by printing out blocks

    final static int SPACES = 4;
    
    //May need to differentiate functions or check if theres a conflict
    private void addEntryToTable(Dec dec, String name) {
        indent(depth);
        System.out.println("Declaration: "+name);

        ArrayList<DecEntry> entries;
        if (symtable.containsKey(name)) {
            entries = symtable.get(name);
        } else {
            entries = new ArrayList<>();
            symtable.put(name, entries);

        }
        DecEntry entry = new DecEntry(name, dec, depth);
        entries.add(entry);
    }

    private void clearSymTable(int depth) {
        for (Map.Entry<String, ArrayList<DecEntry>> var : symtable.entrySet()) {
            DecEntry index = null;
            for (DecEntry entry : var.getValue()) {
               if (entry.depth == depth) {
                   index = entry;
                   break;
               }
            }
            if (index != null) {
                //indent(depth);
                //System.out.println("Removing: "+index.name);
                var.getValue().remove(index);
            }
        }
    }

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

    //todo will need type checking here
    public void visit(AssignExp exp, int level) {
        level++;
        exp.lhs.accept(this, level);
        exp.rhs.accept(this, level);
    }

    public void visit(IfExp exp, int level) {
        level++;
        exp.test.accept(this, level);

        //todo create scope
        indent(++depth);
        System.out.println("Enter if block: ");
        exp.thenpart.accept(this, level);
        if (exp.elsepart != null)
            exp.elsepart.accept(this, level);

        indent(depth);
        System.out.println("Leaving if block");
        depth--;
    }

    public void visit(IntExp exp, int level) {
        //indent(level);
        //System.out.println("IntExp: " + exp.value);
    }

    public void visit(OpExp exp, int level) {
        /*
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
        level++;*/
        exp.left.accept(this, level);
        exp.right.accept(this, level);
    }

    public void visit(RepeatExp exp, int level) {
        /*indent(level);
        System.out.println("RepeatExp:");
        level++;*/
        exp.exps.accept(this, level);
        exp.test.accept(this, level);
    }

    public void visit(VarExp exp, int level) {
        //indent(level);
        //System.out.println("VarExp: ");
        exp.value.accept(this, level);
    }

    public void visit(WriteExp exp, int level) {
        //indent(level);
        //System.out.println("WriteExp:");
        exp.output.accept(this, ++level);
    }

    public void visit(NameTy t, int level) {
        //indent(level);

        // Get the string of the represented int
        String typeString = "";
        if (t.type == 0) {
            typeString = "int";
        } else {
            typeString = "void";
        }

        //System.out.println("NameTy: " + typeString);
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
        //indent(level);
        //System.out.println("ArrayDec: ");
        arr.type.accept(this, ++level);
        //indent(++level);
        //System.out.println("Name: " + arr.name);

        addEntryToTable(arr, arr.name);

        if (arr.size != null)
            arr.size.accept(this, ++level);
    }

    public void visit(SimpleDec dec, int level) {
        //indent(level);
        //System.out.println("SimpleDec: " + String.valueOf(dec.name));

        addEntryToTable(dec, dec.name);

        dec.type.accept(this, ++level);
    }

    public void visit(FunctionDec dec, int level) {
        //indent(level);
        //System.out.println("FunctionDec: ");

        dec.type.accept(this, ++level);
        //indent(++level);
        //System.out.println("Name: " + dec.func);

        //Add function to table
        addEntryToTable(dec, dec.func);


        //todo need to add params to compound depth
        indent(++depth);
        System.out.println("Params: ");
        dec.params.accept(this, ++level);
        depth--;
        dec.body.accept(this, ++level);
    }

    public void visit(CompoundExp exp, int level) {
        //indent(level);
        //System.out.println("CompoundExp: ");

        indent(++depth);
        System.out.println("Enter compound block: ");
        if (exp.decs != null)
            exp.decs.accept(this, ++level);
        if (exp.exps != null)
            exp.exps.accept(this, ++level);

        clearSymTable(depth);
        indent(depth);
        System.out.println("Leaving compound block");
        depth--;
    }

    public void visit(ReturnExp e, int level) {
        //indent(level);
        //System.out.println("ReturnExp: ");
        if (e.exp != null)
            e.exp.accept(this, ++level);
    }

    public void visit(WhileExp exp, int level) {
        //indent(level);
        //System.out.println("WhileExp: ");

        exp.body.accept(this, ++level);
        exp.test.accept(this, ++level);
    }

    public void visit(CallExp exp, int level) {
        //indent(level);
        //System.out.println("CallExp: " + exp.func + " ");
        exp.args.accept(this, ++level);
    }

    public void visit(NilExp exp, int level) {
        //indent(level);
        //System.out.println("NilExp: ");
    }

    public void visit(EpsilonExp exp, int level) {
        //indent(level);
    }

    public void visit(IndexVar var, int level) {
        //indent(level);
        //System.out.println("IndexVar: " + var.name + " ");
        var.index.accept(this, ++level);
    }

    public void visit(SimpleVar var, int level) {
        //indent(level);
        //System.out.println("SimpleVar: " + var.name + " ");
    }
}
