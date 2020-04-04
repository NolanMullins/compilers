import absyn.*;

import java.lang.String;
import java.util.*;

/* ASM Utils
 * General goal of this class is to abstract the tree traversal and high level generation 
 * from the direct C instruction to ASM instruction conversion
 * TODO add utility function to help aid in conversion
 */

public class ASMUtils
{
    //Constant definitions
    final static int SPACES = 4;

    //Special registers
    final static int pc = 7;
    final static int gp = 6;
    final static int fp = 5;
    final static int ac = 0;
    final static int ac1 = 1;

    //Constants
    final static int GLOBAL_SCOPE = 0;

    private static int ins = 0;
    private static int mainLoc = 0;
    //Next available loc after global frame, basically record memory space used by global var
    private static int globalOffset = 0;

    //This might work, might have to store it in the funcitonDec 
    //TODO eval above in the future
    private static int currentFrameOffset = 0;

    //Utility functions
    public void outComment(String line) {
        System.out.println("* "+line);
    }

    private void out(String line) {
        System.out.println(ins++ + ":\t"+line);
    }

    public void out(int ins, String line) {
        System.out.println(ins + ":\t"+line);
    }

    //#region[rgba(150, 10, 10, 0.3)] All instructions are either R0 or RM instructions
    //Slide 3 from TMSim slides
    public void outR0Instruction(String opCode, int r, int s, int t, String comment) {
        out(opCode+"  "+r+","+s+","+t+"\t"+comment);
    }

    //Slide 4 from TMSim slides
    public void outRMInstruction(String opCode, int r, int d, int s, String comment) {
        out(opCode+"  "+r+","+d+"("+s+")\t"+comment);
    }
    //#endregion

    //#region[rgba(10,40,120,0.3)] Helper functions
    //Output a comment and skip a line, return skipped position
    public int outSkip(String msg) {
        outComment(msg);
        return ins++;
    }

    public void outJump(int startLoc, int endLoc, String msg) {
        int offset = endLoc - startLoc - 1;
        //Set PC Reg to PC REG + offset
        /*TODO error checking
        if (newLoc + offset > ins) {
            offset = ins - newLoc;
        }*/
        out(startLoc, "LDA  "+pc+","+offset+"("+pc+")"+"\t"+msg);
    }
    
    public int newTemp() {
        int tmp = currentFrameOffset;
        currentFrameOffset -= 1;
        outRMInstruction("ST", ac, tmp, fp, "op: push tmp left");
        return tmp;
    }
    public void releaseTempVar() {
        currentFrameOffset++;
    }
    //#endregion

    //#region[rgba(50,250,10, 0.1)] High level instruction utils
    public int buildFunction(FunctionDec dec, String name) {
        int loc = outSkip("Function: "+name);
        outRMInstruction("ST", ac, -1, fp, "store return");
        if (name.equals("main"))
            mainLoc = loc;
        dec.address = loc;
        //position 0 is ofp
        //position -1 is ret-addr
        currentFrameOffset = -2;
        return loc;
    }
    
    public void finishFunction(int loc, String name) {
        outRMInstruction("LD", pc, -1, fp, "return to caller");
        outJump(loc, ins, "Jump around function: "+name);
        outComment("End function: "+name);
    }

    //todo add support for function param? might just be able to use the local var
    public void processSimpleDec(SimpleDec var, int depth) {
        if (depth == GLOBAL_SCOPE) {
            outComment("Processing global var declaration: "+var.name);
            var.offset = globalOffset;
            globalOffset--;
            var.nestLevel = 0;
        } else {
            outComment("Processing local var declaration: "+var.name);
            var.offset = currentFrameOffset;
            currentFrameOffset--;
            var.nestLevel = 1;
        }
    }

    public void loadSimpleVar(SimpleVar var, ASMDecEntry dec) {
        if (!(dec.dec instanceof VarDec)) {
            outComment("Error loading simple var: looking for VarDec, got function dec");
            return;
        }
        VarDec varDec = (VarDec)dec.dec;
        //Global
        outComment("Looking up: "+var.name);
        if (varDec.nestLevel == 0) {
          outRMInstruction("LDA", ac, varDec.offset, gp, "load id value");
        }
        else //Local
        {
          outRMInstruction("LDA", ac, varDec.offset, fp, "load id value");
        }
    }

    public void processCallExp(CallExp e, FunctionDec func, int depth) {
        //Code to compute first arg
        //ST ac, frameoffset+initFO (fp)
        //Code to compute second arg
        //ST ac, frameoffset+initFO-1 (fp)
        
        //Store current fp
        //ST fp, frameoffset+ofpFO (fp)
        //Push new frame
        //LDA fp, frameoffset (fp)
        //save return in ac
        //LDA ac, 1 (pc)
        //relative jump to function entry
        //LDA pc, ... (pc)
        //pop current frame
        //LD fp, ofpFO (FP)

        outComment("-> call of function: " + e.func);

        /*int oldFlag = flag;
        flag = CALL;*/

        int frameStart = currentFrameOffset;

        currentFrameOffset -= 2;

        //TODO handle args by pushing on to stack
        /*f(e.args != null)
            e.args.accept(this, depth);*/

        //Def func = getFuncDef(exp.func);

        if(func != null) {
            outRMInstruction("ST", fp, frameStart, fp, "push ofp");
            outRMInstruction("LDA", fp, frameStart, fp, "push frame");
            outRMInstruction("LDA", ac, 1, pc, "load ac with ret ptr");
            //Todo get function location
            outJump(ins, func.address, "Jump to function location");
            //emitRM_Abs("LDA", pc, func.lineNum,"jump to function location");
            outRMInstruction("LD", fp, 0, fp, "pop frame");
        }
        else
        {
            //report_error(exp.row, exp.col, "function: " + exp.func + " not found");
        }

        currentFrameOffset = frameStart;

        //flag = oldFlag;

        outComment("<- call");
    }


    public void processReturnExp(ReturnExp e) {
        //store return address
        //ST ac, retFO (fp)
        //...
        //return to caller
        //LD pc, retFO (fp)
    }

    public void processConstant(IntExp e) {
        outComment("-> cosntant: "+e.value);
        outRMInstruction("LDC", ac, e.value, ac, "load constant");
        outComment("<- constant");
    }

    public void processResultAssignExp(int tmpOffset) {
        outRMInstruction("LD", ac1, tmpOffset, fp, "op: load left");
        outRMInstruction("ST", ac, 0, ac1, "assign: store value");
    }

    public void processResultTmpOpExp(OpExp e, int tmpOffset) {
        outRMInstruction("LD", ac1, tmpOffset, fp, "op: load left");

        outR0Instruction(getASMExpCode(e.op), ac, ac1, ac, "op " + getOpString(e.op));

        //Need to check if this exp is in an if stmt or while stmt
        if (1==2) {
            /*
            asm.outR0Instruction(getRO(exp.op), ac, 2, pc, "br if true");
            asm.outRMInstruction("LDC", ac, 0, 0, "false case");
            asm.outRMInstruction("LDA", pc, 1, pc, "unconditional jump");
            asm.outRMInstruction("LDC", ac, 1, 0, "true case"); 
            */
        }

    }
    //#endregion

    public void prelude(String fileName) {
        outComment("C-Minus Compilation to TM Code");
        outComment("File: "+fileName);
        outComment("Standard prelude:");
        
        outRMInstruction("LD", gp, 0, ac,"load gp with maxaddress");
        outRMInstruction("LDA", fp, 0, gp,"copy to gp to fp");
        outRMInstruction("ST", 0, 0, ac,"clear location 0");

        outComment("Jump around i/o routines here");
        int loc = outSkip("Function input");

        outRMInstruction("ST", ac, -1, fp, "store return");
        outR0Instruction("IN", 0, 0, 0, "Input");
        outRMInstruction("LD", pc, -1, fp, "Return to caller");

        outComment("code for output routine");

        outRMInstruction("ST", ac, -1, fp, "store return");
        outRMInstruction("LD", ac, -2, fp, "load output value");
        outR0Instruction("OUT", 0, 0, 0, "output");
        outR0Instruction("LD", pc, -1, fp, "return to caller");

        outJump(loc, ins, "Jump around I/O code");

        outComment("End of standard prelude.");
    }

    public void end() {
        //Will need to get the global offset
        outRMInstruction("ST", fp, globalOffset, fp, "push ofp");
        outRMInstruction("LDA", fp, globalOffset, fp, "push frame");
        outRMInstruction("LDA", ac, 1, pc, "load ac with ret ptr");
        outRMInstruction("LDA", pc, -(ins-mainLoc-1), pc, "jump to main location");
        outRMInstruction("LD", fp, 0, fp, "pop frame");
        outR0Instruction("HALT", 0, 0, 0, "End");
    }

    public String getASMOpCode(int op) {
        switch(op) {
        case OpExp.PLUS:
            return "ADD";
        case OpExp.MINUS:
            return "SUB";
        case OpExp.MUL:
            return "MUL";
        case OpExp.DIV:
            return "DIV";
        case OpExp.EQ:
            return "JEQ";
        case OpExp.NE:
            return "JNE";
        case OpExp.LT:
            return "JLT";
        case OpExp.LE:
            return "JLE";
        case OpExp.GT:
            return "JGT";
        case OpExp.GE:
            return "JGE";
        default:
            return "Error getting operator asm code";
        }
    }

    //If the code is a boolean expression well do a subtraction in prep for a jump
    public String getASMExpCode(int op) {
        switch(op)
        {
        case OpExp.PLUS:
            return "ADD";
        case OpExp.MINUS:
            return "SUB";
        case OpExp.MUL:
            return "MUL";
        case OpExp.DIV:
            return "DIV";
        default:
            return "SUB";
        }
    }
    
    public String getOpString(int op) {
        switch(op) {
        case OpExp.PLUS:
            return "+";
        case OpExp.MINUS:
            return "-";
        case OpExp.MUL:
            return "*";
        case OpExp.DIV:
            return "/";
        case OpExp.EQ:
            return "==";
        case OpExp.NE:
            return "!=";
        case OpExp.LT:
            return "<";
        case OpExp.LE:
            return "<=";
        case OpExp.GT:
            return ">";
        case OpExp.GE:
            return ">=";
        default:
            return "??? error with operator code: "+op;
        }
    }


}