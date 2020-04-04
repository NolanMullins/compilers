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

    //TODO
    //Call gen code for exp in the list
    //Maybe not, makes more sense to use existing tree traversal
    public void visit(ExpList expList, int level) {
        while (expList != null && expList.head != null) {
            expList.head.accept(this, level);
            //genCode(expList.head);
            expList = expList.tail;
        }
    }

    //TODO Delete This
    //TODO slides 10-CodeGeneration, slide 6-12, 10-12 has a little more info on future work needed
    //TODO implement each section here
    void genCode(Exp tree ) {        // newtemp() returns a new name such as t1, t2, etc.
        String codestr = "";
        if( tree != null ) {
            if( tree instanceof OpExp) {
                Exp left = ((OpExp)tree).left;
                Exp right = ((OpExp)tree).right;
                genCode(left);
                genCode(right);
                tree.tempAddr = asm.newTemp();      // each node is added with a “temp” string
                codestr+= tree.tempAddr+ " = " + left.tempAddr+ "+" + right.tempAddr;
                //emitCode( codestr);
            } else if( tree instanceof AssignExp) {
                VarExp left = ((AssignExp)tree).lhs;
                Exp right = ((AssignExp)tree).rhs;
                genCode(right);
                tree.tempAddr = left.tempAddr;
                codestr += left.tempAddr + "=" + right.tempAddr;
                //emitCode( codestr);
            } else if(tree instanceof VarExp) {
                //Moved into visit var exp
                VarExp e = (VarExp)tree;
                //TODO
                if (e.value instanceof IndexVar) {
                    //need to do work
                } else if (e.value instanceof SimpleVar) {
                    asm.loadSimpleVar((SimpleVar)e.value, table.getVar(e.value.name));
                }
            } else if(tree instanceof IntExp) {
                // do nothing 
            } else 
                asm.outComment("Error");
        }
    }

    public void visit(AssignExp exp, int level) {
        level++;
        asm.outComment("-> op");
        exp.lhs.accept(this, level);

        int tmpVar = asm.newTemp();

        exp.rhs.accept(this, level);

        //get result expression into temp
        asm.processResultAssignExp(tmpVar);

        asm.releaseTempVar();

        asm.outComment("<- op");
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

        asm.outComment("-> op");
        exp.left.accept(this, level);

        int tmpVar = asm.newTemp();

        exp.right.accept(this, level);

        //get result expression into temp
        asm.processResultTmpOpExp(exp, tmpVar);

        asm.releaseTempVar();

        asm.outComment("<- op");
    }

    public void visit(RepeatExp exp, int level) {
        exp.exps.accept(this, level);
        exp.test.accept(this, level);
    }

    public void visit(VarExp exp, int level) {
        exp.value.accept(this, level);
        exp.type = table.getVarType(exp.value.name);
        //TODO
        if (exp.value instanceof IndexVar) {
            //need to do work
        } else if (exp.value instanceof SimpleVar) {
            asm.loadSimpleVar((SimpleVar)exp.value, table.getVar(exp.value.name));
        }
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
        asm.processSimpleDec(dec, depth);
        dec.type.accept(this, ++level);
    }

    public void visit(FunctionDec dec, int level) {

        dec.type.accept(this, ++level);
        //Add function to table
        if (sFlag == 1)
            System.out.println("");
        currFunction = table.addEntryToTable(dec, dec.func, dec.type.type, depth);
        currFunction.params = new ArrayList<>();

        int loc = asm.buildFunction(dec, dec.func);

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


        //leave param depth
        depth--;
        dec.body.accept(this, ++level);
        currFunction = null;
        asm.finishFunction(loc, dec.func);
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
        //Assume no errors
        ASMDecEntry asmDec = table.getVar(exp.func);
        if (!(asmDec.dec instanceof FunctionDec)) {
            System.out.println("Error needed function, got something else");
            return;
        }
        exp.type = table.getVarType(exp.func);
        asm.processCallExp(exp, (FunctionDec)asmDec.dec, depth);
        exp.args.accept(this, ++level);

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
