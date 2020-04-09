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
            //genCode(expList.head);
            expList = expList.tail;
        }
    }

    public void visit(AssignExp exp, int level) {
        level++;
        asm.outComment("-> op");
        exp.lhs.isAddr = true;
        exp.lhs.isAssign = true;
        exp.lhs.accept(this, level);

        int tmpVar = asm.newTemp();

        exp.rhs.isAddr = false;
        exp.rhs.accept(this, level);

        //get result expression into temp
        asm.processResultAssignExp(tmpVar);

        asm.releaseTempVar();

        asm.outComment("<- op");
    }

    public void visit(IfExp exp, int level) {
        level++;

        asm.outComment("-> if");
        asm.outComment("-> test");
        //This will build the asm expression but we'll still have to check the result
        exp.test.accept(this, level);
        int testLoc = 0;
        if (exp.test instanceof OpExp)
            testLoc = asm.processIfJump((OpExp)exp.test);
        else
            asm.outComment("Error with if test case, not OpExp");
        asm.outComment("<- test");

        exp.thenpart.accept(this, level);
        int finishThenLoc = asm.outSkip("Jump to end of if block");

        //If the statement resulted in false we need to jump to the else block
        asm.outJumpToCurrentINS("JEQ", 0, testLoc, "Jump over then block");

        if (exp.elsepart != null)
            exp.elsepart.accept(this, level);
        
        asm.outJumpToCurrentINS(finishThenLoc, "Leave then block");
        asm.outComment("<- if");

        //indent(depth);
        //System.out.println("Leaving if block");
    }

    public void visit(IntExp exp, int level) {
        exp.type = NameTy.INT;
        asm.processConstant(exp);
    }

    public void visit(OpExp exp, int level) {

        asm.outComment("-> op");
        exp.left.isAddr = false;
        exp.left.accept(this, level);

        int tmpVar = asm.newTemp();

        exp.right.isAddr = false;
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
            IndexVar var = (IndexVar)exp.value;
            ASMDecEntry dec = table.getVar(var.name);
            boolean loadAddr = true;
            if (dec.dec instanceof ArrayDec)
                if (((ArrayDec)dec.dec).isParam)
                    loadAddr = false;
            asm.loadVar(exp.value, dec, loadAddr);
            //Store addr in tmp
            int tmp = asm.newTemp();
            var.index.accept(this, ++level);
            asm.verifyArrayAccess(0);
            asm.loadArray(var, dec, exp.isAssign, tmp);
            //need to do work
        } else if (exp.value instanceof SimpleVar) {
            boolean isAddr = exp.isAddr;
            ASMDecEntry dec = table.getVar(exp.value.name);
            if (dec.dec instanceof ArrayDec && !((ArrayDec)dec.dec).isParam)
                isAddr = true;
            asm.loadVar(exp.value, dec, isAddr);
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
        if (arr.size == null) {
            arr.isParam = true;
            asm.processArrayDec(arr, 1, depth);
        } else {
            arr.isParam = false;
            asm.processArrayDec(arr, arr.size.value, depth);
        }

        //if (arr.size != null)
            //arr.size.accept(this, ++level);
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
        dec.address = loc+1;

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
        asm.outComment("-> return");
        e.exp.accept(this, level);
        asm.returnToCaller();
        asm.outComment("<- return");
    }

    public void visit(WhileExp exp, int level) {
        level++;

        asm.outComment("-> While");
        //Record the starting position of the while loop, we'll need to jump back here each iteration
        int topOfWhile = asm.getCurrINS();
        //Build the looping expression
        exp.test.accept(this, level);

        //Check the result of the expression
        int testLoc = 0;
        if (exp.test instanceof OpExp)
            testLoc = asm.processIfJump((OpExp)exp.test);
        else
            asm.outComment("Error with if test case, not OpExp");

        exp.body.accept(this, level);

        //At the end of the loop body jump back
        asm.outJump(asm.getCurrINS(), topOfWhile, "Jump back to test condition");

        //Backpatch a jump over the loop body if the expression resulted in false
        asm.outJumpToCurrentINS("JEQ", 0, testLoc, "Jump over body");

        asm.outComment("<- While");;
    }

    public void visit(CallExp exp, int level) {
        //Assume no errors
        ASMDecEntry asmDec = table.getVar(exp.func);
        if (!(asmDec.dec instanceof FunctionDec)) {
            System.out.println("Error needed function, got something else");
            return;
        }
        int frameStart = asm.startCallExp();
        //exp.args.accept(this, ++level);
        ExpList args = exp.args;
        while (args != null && args.head != null) {
            //args.head.isAddr = false;
            //Passing array value in to function
            /*if (args.head instanceof VarExp)
                if (((VarExp)args.head).value instanceof IndexVar)
                    args.head.isAddr = true;*/
            args.head.accept(this, level);
            //Push arg on stack
            asm.pushArgOnStack(1);
            args = args.tail;
        }
        exp.type = table.getVarType(exp.func);
        asm.processCallExp(exp, (FunctionDec)asmDec.dec, depth, frameStart);

    }

    public void visit(NilExp exp, int level) {

    }

    public void visit(EpsilonExp exp, int level) {

    }

    public void visit(IndexVar var, int level) {
        //lda flag = false;
        //var.index.accept(this, ++level);
    }

    public void visit(SimpleVar var, int level) {
    }
}
