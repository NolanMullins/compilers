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

    //Utility functions
    private void outComment(String line) {
        System.out.println("* "+line);
    }

    private void out(String line) {
        System.out.println(line);
    }



    //TODO copy all his prelude code here
    public void prelude() {
        outComment("C-Minus Compilation to TM Code");
        outComment("File: gcd.tm");
        outComment("Standard prelude:");
        out("0:     LD  6,0(0) 	load gp with maxaddress");
        out("1:    LDA  5,0(6) 	copy to gp to fp");
        out("2:     ST  0,0(0) 	clear location 0");
        outComment("Jump around i/o routines here");
        outComment("code for input routine");
        out("4:     ST  0,-1(5) 	store return");
        out("5:     IN  0,0,0 	input");
        out("6:     LD  7,-1(5) 	return to caller");
        outComment("code for output routine");
        out("7:     ST  0,-1(5) 	store return");
        out("8:     LD  0,-2(5) 	load output value");
        out("9:    OUT  0,0,0 	output");
        out("10:     LD  7,-1(5) 	return to caller");
        out("3:    LDA  7,7(7) 	jump around i/o code");
        outComment("End of standard prelude.");
    }


}