* C-Minus Compilation to TM Code
* File: data/main.cm
* Standard prelude:
0:      LD  6,0(0)      load gp with maxaddress
1:      LDA  5,0(6)     copy to gp to fp
2:      ST  0,0(0)      clear location 0
* Jump around i/o routines here
* Function input
4:      ST  0,-1(5)     store return
5:      IN  0,0,0       Input
6:      LD  7,-1(5)     Return to caller
* code for output routine
7:      ST  0,-1(5)     store return
8:      LD  0,-2(5)     load output value
9:      OUT  0,0,0      output
10:     LD  7,-1(5)     return to caller
3:      LDA  7,7(7)     Jump around I/O code
* End of standard prelude.
* Function: main
12:     ST  0,-1(5)     store return
* Processing local var declaration: x
* Processing local var declaration: fac
* -> op
* Looking up: x
13:     LD  0,-2(5)     load id value
14:     ST  0,-4(5)     op: push tmp left
* -> call of function: input
15:     ST  5,-5(5)     push ofp
16:     LDA  5,-5(5)    push frame
17:     LDA  0,1(7)     load ac with ret ptr
18:     LDA  7,-15(7)   Jump to function location
19:     LD  5,0(5)      pop frame
* <- call
20:     LD  1,-4(5)     op: load left
21:     ST  0,0(1)      assign: store value
* <- op
* -> op
* Looking up: fac
22:     LD  0,-3(5)     load id value
23:     ST  0,-4(5)     op: push tmp left
* -> cosntant: 1
24:     LDC  0,1(0)     load constant
* <- constant
25:     LD  1,-4(5)     op: load left
26:     ST  0,0(1)      assign: store value
* <- op
* -> while
* while: jump after body comes back here
27:     LD  0,-2(5)     load id value
28:     ST  0,-4(5)     op: push left
29:     LDC  0,1(0)     load const
30:     LD  1,-4(5)     op: load left
31:     SUB  0,1,0      op >
32:     JGT  0,2(7)     br if true
33:     LDC  0,0(0)     false case
34:     LDA  7,1(7)     unconditional jmp
35:     LDC  0,1(0)     true case
* While: jump to end belongs here
*  -> compound statement
*  -> op
* looking up id: fac
* Space for jump
37:     LDA  0,-3(5)    load id address
38:     ST  0,-4(5)     op: push left
39:     LD  0,-3(5)     load id value
40:     ST  0,-5(5)     op: push left
41:     LD  0,-2(5)     load id value
42:     LD  1,-5(5)     op: load left
43:     MUL  0,1,0      op *
44:     LD  1,-4(5)     op: load left
45:     ST  0,0(1)      assign: store value
46:     LDA  0,-2(5)    load id address
47:     ST  0,-4(5)     op: push left
48:     LD  0,-2(5)     load id value
49:     ST  0,-5(5)     op: push left
50:     LDC  0,1(0)     load const
51:     LD  1,-5(5)     op: load left
52:     SUB  0,1,0      op -
53:     LD  1,-4(5)     op: load left
54:     ST  0,0(1)      assign: store value
55:     LDA  7,-29(7)   while: absolute jmp to test
36:     JEQ  0,19(7)    while: jmp to end
* <- while
* Looking up: fac
56:     LD  0,-3(5)     load id value
57:     ST  0,-6(5)     store arg val
* -> call of function: output
58:     ST  5,-4(5)     push ofp
59:     LDA  5,-4(5)    push frame
60:     LDA  0,1(7)     load ac with ret ptr
61:     LDA  7,-55(7)   Jump to function location
62:     LD  5,0(5)      pop frame
* <- call
63:     LD  7,-1(5)     return to caller
11:     LDA  7,52(7)    Jump around function: main
* End function: main
64:     ST  5,0(5)      push ofp
65:     LDA  5,0(5)     push frame
66:     LDA  0,1(7)     load ac with ret ptr
67:     LDA  7,-56(7)   jump to main location
68:     LD  5,0(5)      pop frame
69:     HALT  0,0,0     End