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

    private static int ins = 0;
    private static int mainLoc = 0;






    //#region[rgba(150, 10, 10, 0.3)] All instructions are either R0 or RM instructions
    //Utility functions
    private void outComment(String line) {
        System.out.println("* "+line);
    }

    private void out(String line) {
        System.out.println(ins++ + ":\t"+line);
    }

    public void out(int ins, String line) {
        System.out.println(ins + ":\t"+line);
    }

    //Slide 3 from TMSim slides
    public void outR0Instructions(String opCode, int r, int s, int t, String comment) {
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

    public void outJump(int loc, int offset, String msg) {
        //Set PC Reg to PC REG + offset
        out(loc, "LDA  "+pc+","+offset+"("+pc+")"+"\t"+msg);
    }
    //#endregion

    ////#region[rgba(50,250,10, 0.1)] High level instruction utils
    public int buildFunction(String name) {
        int loc = outSkip("Function: "+name);
        outRMInstruction("ST", ac, -1, fp, "store return");
        if (name.equals("main"))
            mainLoc = loc;
        return loc;
    }
    
    public void finishFunction(int loc, String name) {
        outJump(loc, ins-loc-1, "Jump around function: "+name);
        outComment("End function: "+name);
    }
    ////#endregion

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
        outR0Instructions("IN", 0, 0, 0, "Input");
        outRMInstruction("LD", pc, -1, fp, "Return to caller");

        outComment("code for output routine");

        outRMInstruction("ST", ac, -1, fp, "store return");
        outRMInstruction("LD", ac, -2, fp, "load output value");
        outR0Instructions("OUT", 0, 0, 0, "output");
        outR0Instructions("LD", pc, -1, fp, "return to caller");

        outJump(loc, ins-loc-1, "Jump around I/O code");

        outComment("End of standard prelude.");
    }

    public void end() {
        //Will need to get the global offset
        int offset = 0; //TODO
        //Will need to record the offset of the main function
        int mainLoc = 11; //TODO
        outRMInstruction("ST", fp, -offset, fp, "push ofp");
        outRMInstruction("LDA", fp, -offset, fp, "push frame");
        outRMInstruction("LDA", ac, 1, pc, "load ac with ret ptr");
        outRMInstruction("LDA", pc, -(ins-mainLoc-1), pc, "jump to main location");
        outRMInstruction("LD", fp, 0, fp, "pop frame");
        outR0Instructions("HALT", 0, 0, 0, "End");
    }

}