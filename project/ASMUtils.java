import absyn.*;

import java.lang.String;
import java.util.*;


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

    //Output a comment and skip a line, return skipped position
    public int outSkip(String msg) {
        outComment(msg);
        return ins++;
    }

    public void outJump(int loc, int offset, String msg) {
        out(loc, "LDA  7,"+offset+"(7)"+"\t"+msg);
    }

    //TODO copy all his prelude code here
    public void prelude() {
        outComment("C-Minus Compilation to TM Code");
        outComment("File: gcd.tm");
        outComment("Standard prelude:");
        out("LD  6,0(0) 	load gp with maxaddress");
        out("LDA  5,0(6) 	copy to gp to fp");
        out("ST  0,0(0) 	clear location 0");
        outComment("Jump around i/o routines here");
        int loc = outSkip("Function input");
        out("ST  0,-1(5) 	store return");
        out("IN  0,0,0 	input");
        out("LD  7,-1(5) 	return to caller");
        outComment("code for output routine");
        out("ST  0,-1(5) 	store return");
        out("LD  0,-2(5) 	load output value");
        out("OUT  0,0,0 	output");
        out("LD  7,-1(5) 	return to caller");
        outJump(loc, ins-loc-1, "Jump around I/O code");
        //out("3:    LDA  7,7(7) 	jump around i/o code");
        outComment("End of standard prelude.");
    }


}