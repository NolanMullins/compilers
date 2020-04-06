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
3:      LDA 7,7(7)      Jump around I/O code
* End of standard prelude.
* Function: main
12:     ST  0,-1(5)     store return
* Processing local var declaration: x
* Processing local var declaration: fac
* -> op
* Looking up: x
13:     LDA  0,-2(5)    load id
14:     ST  0,-4(5)     op: push tmp left
* -> cosntant: 0
15:     LDC  0,0(0)     load constant
* <- constant
16:     LD  1,-4(5)     op: load left
17:     ST  0,0(1)      assign: store value
* <- op
* -> While
* -> op
* Looking up: x
18:     LD  0,-2(5)     load id
19:     ST  0,-4(5)     op: push tmp left
* -> cosntant: 5
20:     LDC  0,5(0)     load constant
* <- constant
21:     LD  1,-4(5)     op: load left
22:     SUB  0,1,0      op <
* <- op
23:     JLT  0,2,7      br if true
24:     LDC  0,0(0)     false case
25:     LDA  7,1(7)     unconditional jump
26:     LDC  0,1(0)     true case
* If jump location
* -> op
* Looking up: x
28:     LDA  0,-2(5)    load id
29:     ST  0,-4(5)     op: push tmp left
* -> op
* Looking up: x
30:     LD  0,-2(5)     load id
31:     ST  0,-5(5)     op: push tmp left
* -> cosntant: 1
32:     LDC  0,1(0)     load constant
* <- constant
33:     LD  1,-5(5)     op: load left
34:     ADD  0,1,0      op +
* <- op
35:     LD  1,-4(5)     op: load left
36:     ST  0,0(1)      assign: store value
* <- op
37:     LDA 7,-20(7)    JumpBack to test condition
27:     JEQ 0,10(7)     Jump over body
* <- While
* Looking up: x
38:     LD  0,-2(5)     load id
39:     ST  0,-6(5)     store arg val
* -> call of function: output
40:     ST  5,-4(5)     push ofp
41:     LDA  5,-4(5)    push frame
42:     LDA  0,1(7)     load ac with ret ptr
43:     LDA 7,-37(7)    Jump to function location
44:     LD  5,0(5)      pop frame
* <- call
45:     LD  7,-1(5)     return to caller
11:     LDA 7,34(7)     Jump around function: main
* End function: main
46:     ST  5,0(5)      push ofp
47:     LDA  5,0(5)     push frame
48:     LDA  0,1(7)     load ac with ret ptr
49:     LDA  7,-38(7)   jump to main location
50:     LD  5,0(5)      pop frame
51:     HALT  0,0,0     End