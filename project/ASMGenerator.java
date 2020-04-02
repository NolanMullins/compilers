import absyn.*;

import java.lang.String;
import java.util.*;


public class ASMGenerator implements AbsynVisitor 
{
    static int sFlag = 0;

    private int depth;
    private ASMDecEntry currFunction = null;

    private ASMUtils asm = null;
    private ASMSymbolTable table = null;

    private String fileName = "";

    public ASMGenerator(String fileName) {
        depth = 0;
        asm = new ASMUtils();
        table = new ASMSymbolTable(sFlag);
        this.fileName = fileName;
    }

    //Constant definitions
    final static int SPACES = 4;

    private void indent(int level) {
        if (sFlag == 1) {
            for (int i = 0; i < level * SPACES; i++)
                System.out.print(" ");
        }
    }

    public void visit(DecList decList, int level) {
        asm.prelude(fileName);

        while (decList != null && decList.head != null) {
            decList.head.accept(this, level);
            decList = decList.tail;
        }

        asm.end();
    }

    public void visit(VarDecList varDecList, int level) {
        while (varDecList != null && varDecList.head != null) {
            varDecList.head.accept(this, level);
            varDecList = varDecList.tail;
        }
    }


    public void visit(ExpList expList, int level) {
        while (expList != null && expList.head != null) {
            expList.head.accept(this, level);
            expList = expList.tail;
        }
    }

    public void visit(AssignExp exp, int level) {
        level++;
        exp.lhs.accept(this, level);
        exp.rhs.accept(this, level);
        int lhsType = table.getVarType(exp.lhs.name);
        if (lhsType >= 0 && exp.rhs.type >= 0 && lhsType != exp.rhs.type) {
            indent(depth);
            System.out.println("[ERROR] Type mismatch in assignment found ("+NameTy.types[exp.rhs.type]+") expected ("+NameTy.types[lhsType]+") [row: "+exp.row + " col: "+exp.col+"]");
        }
    }

    public void visit(IfExp exp, int level) {
        level++;
        exp.test.accept(this, level);

        //indent(depth);
        //System.out.println("Enter if block: ");
        exp.thenpart.accept(this, level);
        if (exp.elsepart != null)
            exp.elsepart.accept(this, level);

        //indent(depth);
        //System.out.println("Leaving if block");
    }

    public void visit(IntExp exp, int level) {
        exp.type = NameTy.INT;
    }

    public void visit(OpExp exp, int level) {

        exp.left.accept(this, level);
        exp.right.accept(this, level);

        //If expression var have been defined and their types do not match
        if (exp.left.type >= 0 && exp.right.type >= 0 && exp.left.type != exp.right.type) {
            indent(depth);
            System.out.println("[ERROR] Type mismatch around operator, found ("+NameTy.types[exp.left.type]+") "+OpExp.operators[exp.op]+" ("+NameTy.types[exp.right.type]+") [row: "+exp.row + " col: "+exp.col+"]");
            exp.type = -1;
        } else if (exp.left.type >= 0 && exp.right.type >= 0) {
            //Check if boolean operator (Need int on both sides)
            if (exp.op > 3 && exp.left.type == NameTy.VOID) {
                indent(depth);
                System.out.println("[ERROR] Boolean operation must be between INT not VOID [row: "+exp.row + " col: "+exp.col+"]");
                exp.type = -1;
            } else {
                exp.type = exp.left.type;
            }
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
        exp.type = table.getVarType(exp.value.name);
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

    public void visit(ArrayDec arr, int level) {

        arr.type.accept(this, ++level);
        //Add var to table
        table.addEntryToTable(arr, arr.name, arr.type.type, depth);

        if (arr.size != null)
            arr.size.accept(this, ++level);
    }

    public void visit(SimpleDec dec, int level) {

        //Add var to table
        table.addEntryToTable(dec, dec.name, dec.type.type, depth);
        dec.type.accept(this, ++level);
    }

    public void visit(FunctionDec dec, int level) {

        dec.type.accept(this, ++level);
        //Add function to table
        if (sFlag == 1)
            System.out.println("");
        currFunction = table.addEntryToTable(dec, dec.func, dec.type.type, depth);
        currFunction.params = new ArrayList<>();

        int loc = asm.buildFunction(dec.func);

        //Add parameters to the new block depth
        indent(++depth);
        if (sFlag == 1)
            System.out.println("Params: ");

        dec.params.accept(this, ++level);
        indent(depth);
        if (sFlag == 1)
            System.out.println("");

        //Store parameter information related to function dec
        VarDecList list = dec.params;
        while (list != null && list.head != null) {
            currFunction.params.add(list.head);
            list = list.tail;
        }

        asm.finishFunction(loc, dec.func);

        //leave param depth
        depth--;
        dec.body.accept(this, ++level);
        currFunction = null;
    }

    public void visit(CompoundExp exp, int level) {
        //Enter a compound block
        if (depth>0 && sFlag == 1) {
            indent(depth);
            System.out.println("Entering a new block: ");
        }
        depth++;
        if (exp.decs != null)
            exp.decs.accept(this, ++level);
        if (exp.exps != null)
            exp.exps.accept(this, ++level);

        //leave compound block, clear variables defined in scope
        table.clearSymTable(depth);
        depth--;
        if (depth > 0 && sFlag == 1) {
            indent(depth);
            System.out.println("Leaving the block");
        }
    }

    public void visit(ReturnExp e, int level) {
        if (e.exp != null) {
            e.exp.accept(this, ++level);
            if (e.exp.type >= 0 && currFunction.type >= 0 && e.exp.type != currFunction.type) {
                indent(depth);
                System.out.println("[ERROR] Return type must be ("+NameTy.types[currFunction.type]+"), found ("+NameTy.types[e.exp.type]+") [row: "+e.exp.row + " col: "+e.exp.col+"]");
            }
        } else if (currFunction!= null && currFunction.type != NameTy.VOID) {
            indent(depth);
        }
    }

    public void visit(WhileExp exp, int level) {
        exp.test.accept(this, ++level);
        exp.body.accept(this, ++level);
    }

    public void visit(CallExp exp, int level) {
        if (!table.symtable.containsKey(exp.func) || table.symtable.get(exp.func).size() == 0) {
            if (exp.func.toString().equals("input") || exp.func.toString().equals("output"))
                return;
            System.out.println(exp.func.toString());
            System.out.println(exp.func.toString().equals("input"));
            indent(depth);
            System.out.println("[ERROR] Undefined function: " + exp.func + " [row: "+exp.row + " col: "+exp.col+"]");
            exp.type = -1;
            return;
        } else if(!(table.symtable.get(exp.func).get(table.symtable.get(exp.func).size()-1).dec instanceof FunctionDec)) {
            indent(depth);
            System.out.println("[ERROR] Function '" + exp.func + "' is a Var [row: "+exp.row + " col: "+exp.col+"]");
            exp.type = -1;
            return;
        }
        exp.type = table.getVarType(exp.func);
        exp.args.accept(this, ++level);

        //Args checking 
        ArrayList<Dec> params = table.symtable.get(exp.func).get(0).params;
        ExpList args = exp.args;

        //Check no params
        if (params.size() == 0 && args.head == null)
            return;

        ArrayList<Exp> argsArray = new ArrayList<>();
        while (args != null && args.head != null) {
            argsArray.add(args.head);
            args = args.tail;
        }

        //Check for correct number of params
        if (params.size() != argsArray.size()) {
            indent(depth);
            System.out.print("[ERROR] Arguments do not match function expected: "+exp.func+"("+table.formatParamsString(params)+") got "+exp.func+"("+table.formatArgsString(argsArray)+")");
            System.out.println(" [row: "+exp.row + " col: "+exp.col+"]");
            return;
        }

        //Check if args match param types
        for (int i = 0; i < params.size(); i++) {
            boolean error = false;
            //Check if types dont match
            if (params.get(i).type.type != argsArray.get(i).type) {
                error = true;
            }
            //Check for arrays
            else if (params.get(i) instanceof ArrayDec) {
                Exp e = argsArray.get(i);
                if (e instanceof VarExp) {
                    String varName = ((VarExp)e).value.name;
                    if (table.symtable.containsKey(varName) && table.symtable.get(varName).size() > 0) {
                        if (!(table.symtable.get(varName).get(table.symtable.get(varName).size()-1).dec instanceof ArrayDec))
                            error = true;
                    } else {
                        error = true;
                    }
                }
            }
            if (error) {
                indent(depth);
                System.out.print("[ERROR] Arguments do not match function expected: "+exp.func+"("+table.formatParamsString(params)+") got "+exp.func+"("+table.formatArgsString(argsArray)+")");
                System.out.println(" [row: "+exp.row + " col: "+exp.col+"]");
                return;
            }
        }

    }

    public void visit(NilExp exp, int level) {

    }

    public void visit(EpsilonExp exp, int level) {

    }

    public void visit(IndexVar var, int level) {
        if (!table.symtable.containsKey(var.name) || table.symtable.get(var.name).size() == 0) {
            indent(depth);
            System.out.println("[ERROR] Undefined use of: "+var.name + " [row: "+var.row + " col: "+var.col+"]");
        } else if(table.symtable.get(var.name).get(table.symtable.get(var.name).size()-1).dec instanceof FunctionDec) {
            indent(depth);
            System.out.println("[ERROR] Var '" + var.name + "' is a function [row: "+var.row + " col: "+var.col+"]");
        }
        var.index.accept(this, ++level);

        if (var.index.type >= 0 && var.index.type != NameTy.INT) {
            indent(depth);
            System.out.println("[ERROR] Index variable not type INT [row: "+var.row + " col: "+var.col+"]");
        }
    }

    public void visit(SimpleVar var, int level) {
        if (!table.symtable.containsKey(var.name) || table.symtable.get(var.name).size() == 0) {
            indent(depth);
            System.out.println("[ERROR] Undefined use of: "+var.name + " [row: "+var.row + " col: "+var.col+"]");
        } else if(table.symtable.get(var.name).get(table.symtable.get(var.name).size()-1).dec instanceof FunctionDec) {
            indent(depth);
            System.out.println("[ERROR] Var '" + var.name + "' is a function [row: "+var.row + " col: "+var.col+"]");
        }
    }
}
