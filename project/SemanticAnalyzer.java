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
        int type;
        public DecEntry(String name, Dec dec, int depth, int type) 
        {
            this.name = name;
            this.dec = dec;
            this.depth = depth;
            this.type = type;
        }
        @Override
        public String toString() {
            return name+", "+NameTy.types[type]+", "+dec;
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

    //TODO 
    //function calls
    //return exps
    //test conditions must be int

    final static int SPACES = 4;
    
    //May need check if theres a conflict, can you define a variable in a higher scope with the same name as a function?
    private void addEntryToTable(Dec dec, String name, int type) {
        indent(depth);

        String decType = "declaration";
        if (dec instanceof FunctionDec)
            decType = "function";

        //Check if its already defined
        if (symtable.containsKey(name) && symtable.get(name).size() > 0) {
            ArrayList<DecEntry> list = symtable.get(name);
            if (list.get(list.size()-1).depth == depth) {
                //Redefinition error
                System.out.println("[ERROR] Redefined "+decType+": " + name + " [row: "+dec.row + " col: "+dec.col+"]");
                return;
            }
        }

        //Output declaration msg
        System.out.println(decType+": "+name);

        //Add declaration to symtable
        ArrayList<DecEntry> entries;
        if (symtable.containsKey(name)) {
            entries = symtable.get(name);
        } else {
            entries = new ArrayList<>();
            symtable.put(name, entries);

        }
        DecEntry entry = new DecEntry(name, dec, depth, type);
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

    private int getVarType(String name) {
        if (symtable.containsKey(name)) {
            ArrayList<DecEntry> list = symtable.get(name);
            if (list.size() <= 0)
                return -1;
            return list.get(list.size()-1).type;
        }
        return -1;
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

    //TODO Need to check if lhs type = rhs
    public void visit(AssignExp exp, int level) {
        level++;
        exp.lhs.accept(this, level);
        exp.rhs.accept(this, level);
        int lhsType = getVarType(exp.lhs.name);
        if (lhsType >= 0 && exp.rhs.type >= 0 && lhsType != exp.rhs.type) {
            indent(depth);
            System.out.println("[ERROR] Type mismatch in assignment found ("+NameTy.types[exp.rhs.type]+") expected ("+NameTy.types[lhsType]+") [row: "+exp.row + " col: "+exp.col+"]");
        }
    }

    public void visit(IfExp exp, int level) {
        level++;
        exp.test.accept(this, level);

        indent(depth);
        System.out.println("Enter if block: ");
        exp.thenpart.accept(this, level);
        if (exp.elsepart != null)
            exp.elsepart.accept(this, level);

        indent(depth);
        System.out.println("Leaving if block");
    }

    public void visit(IntExp exp, int level) {
        exp.type = NameTy.INT;
    }

    //TODO Check left and right side of expression
    public void visit(OpExp exp, int level) {

        exp.left.accept(this, level);
        exp.right.accept(this, level);

        //If expression var have been defined and their types do not match
        if (exp.left.type >= 0 && exp.right.type >= 0 && exp.left.type != exp.right.type) {
            indent(depth);
            System.out.println("[ERROR] Type mismatch around operator, found ("+NameTy.types[exp.left.type]+") "+OpExp.operators[exp.op]+" ("+NameTy.types[exp.right.type]+") [row: "+exp.row + " col: "+exp.col+"]");
            exp.type = -1;
        } else if (exp.left.type >= 0 && exp.right.type >= 0) {
            exp.type = exp.left.type;
        } else {
            exp.type = -1;
        }

    }

    public void visit(RepeatExp exp, int level) {
        exp.exps.accept(this, level);
        exp.test.accept(this, level);
    }

    public void visit(VarExp exp, int level) {
        exp.value.accept(this, level);
        exp.type = getVarType(exp.value.name);
    }

    public void visit(WriteExp exp, int level) {
        exp.output.accept(this, ++level);
    }

    public void visit(NameTy t, int level) {
        // Get the string of the represented int
        String typeString = "";
        if (t.type == 0) {
            typeString = "int";
        } else {
            typeString = "void";
        }

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

        arr.type.accept(this, ++level);
        //Add var to table
        addEntryToTable(arr, arr.name, arr.type.type);

        if (arr.size != null)
            arr.size.accept(this, ++level);
    }

    public void visit(SimpleDec dec, int level) {

        //Add var to table
        addEntryToTable(dec, dec.name, dec.type.type);
        dec.type.accept(this, ++level);
    }

    public void visit(FunctionDec dec, int level) {

        dec.type.accept(this, ++level);
        //Add function to table
        System.out.println("");
        addEntryToTable(dec, dec.func, dec.type.type);

        //Add parameters to the new block depth
        indent(++depth);
        System.out.println("Params: ");
        dec.params.accept(this, ++level);
        depth--;
        dec.body.accept(this, ++level);
    }

    public void visit(CompoundExp exp, int level) {
        //Enter a compound block
        indent(++depth);
        System.out.println("Enter compound block: ");
        if (exp.decs != null)
            exp.decs.accept(this, ++level);
        if (exp.exps != null)
            exp.exps.accept(this, ++level);

        //leave compound block, clear variables defined in scope
        clearSymTable(depth);
        indent(depth);
        System.out.println("Leaving compound block");
        depth--;
    }

    public void visit(ReturnExp e, int level) {
        if (e.exp != null)
            e.exp.accept(this, ++level);
    }

    public void visit(WhileExp exp, int level) {
        exp.body.accept(this, ++level);
        exp.test.accept(this, ++level);
    }

    public void visit(CallExp exp, int level) {
        //TODO maybe check if its referencing a var name?
        if (!symtable.containsKey(exp.func) || symtable.get(exp.func).size() == 0) {
            indent(depth);
            System.out.println("[ERROR] Undefined function: " + exp.func + " [row: "+exp.row + " col: "+exp.col+"]");
        }
        exp.type = getVarType(exp.func);
        exp.args.accept(this, ++level);
    }

    public void visit(NilExp exp, int level) {

    }

    public void visit(EpsilonExp exp, int level) {

    }

    public void visit(IndexVar var, int level) {
        //TODO maybe check if its referencing a function name?
        if (!symtable.containsKey(var.name) || symtable.get(var.name).size() == 0) {
            indent(depth);
            System.out.println("[ERROR] Undefined use of: "+var.name + " [row: "+var.row + " col: "+var.col+"]");
        }
        var.index.accept(this, ++level);

        if (var.index.type >= 0 && var.index.type != NameTy.INT) {
            indent(depth);
            System.out.println("[ERROR] Index variable not type INT [row: "+var.row + " col: "+var.col+"]");
        }
    }

    public void visit(SimpleVar var, int level) {
        //TODO maybe check if its referencing a function name?
        if (!symtable.containsKey(var.name) || symtable.get(var.name).size() == 0) {
            indent(depth);
            System.out.println("[ERROR] Undefined use of: "+var.name + " [row: "+var.row + " col: "+var.col+"]");
        }
    }
}
