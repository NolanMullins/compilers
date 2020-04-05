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
* -> call of function: input
15:     ST  5,-5(5)     push ofp
16:     LDA  5,-5(5)    push frame
17:     LDA  0,1(7)     load ac with ret ptr
18:     LDA 7,-15(7)    Jump to function location
19:     LD  5,0(5)      pop frame
* <- call
20:     LD  1,-4(5)     op: load left
21:     ST  0,0(1)      assign: store value
* <- op
* -> op
* Looking up: fac
22:     LDA  0,-3(5)    load id
23:     ST  0,-4(5)     op: push tmp left
* -> cosntant: 10
24:     LDC  0,10(0)    load constant
* <- constant
25:     LD  1,-4(5)     op: load left
26:     ST  0,0(1)      assign: store value
* <- op
* -> if
* -> test
* -> op
* Looking up: fac
27:     LD  0,-3(5)     load id
28:     ST  0,-4(5)     op: push tmp left
* -> cosntant: 11
29:     LDC  0,11(0)    load constant
* <- constant
30:     LD  1,-4(5)     op: load left
31:     SUB  0,1,0      op >
* <- op
32:     JGT  0,2,7      br if true
33:     LDC  0,0(0)     false case
34:     LDA  7,1(7)     unconditional jump
35:     LDC  0,1(0)     true case
* If jump location
* <- test
* -> op
* Looking up: fac
37:     LDA  0,-3(5)    load id
38:     ST  0,-4(5)     op: push tmp left
* -> cosntant: 99
39:     LDC  0,99(0)    load constant
* <- constant
40:     LD  1,-4(5)     op: load left
41:     ST  0,0(1)      assign: store value
* <- op
* Jump to end of if block
36:     JEQ 0,6(7)      Jump over then block
42:     LDA 7,0(7)      Leave then block
* <- if
* -> if
* -> test
* -> op
* Looking up: x
43:     LD  0,-2(5)     load id
44:     ST  0,-4(5)     op: push tmp left
* -> cosntant: 10
45:     LDC  0,10(0)    load constant
* <- constant
46:     LD  1,-4(5)     op: load left
47:     SUB  0,1,0      op >
* <- op
48:     JGT  0,2,7      br if true
49:     LDC  0,0(0)     false case
50:     LDA  7,1(7)     unconditional jump
51:     LDC  0,1(0)     true case
* If jump location
* <- test
* -> op
* Looking up: x
53:     LDA  0,-2(5)    load id
54:     ST  0,-4(5)     op: push tmp left
* -> cosntant: 2
55:     LDC  0,2(0)     load constant
* <- constant
56:     LD  1,-4(5)     op: load left
57:     ST  0,0(1)      assign: store value
* <- op
* Jump to end of if block
52:     JEQ 0,6(7)      Jump over then block
* -> op
* Looking up: x
59:     LDA  0,-2(5)    load id
60:     ST  0,-4(5)     op: push tmp left
* -> cosntant: 1
61:     LDC  0,1(0)     load constant
* <- constant
62:     LD  1,-4(5)     op: load left
63:     ST  0,0(1)      assign: store value
* <- op
58:     LDA 7,5(7)      Leave then block
* <- if
* Looking up: x
64:     LD  0,-2(5)     load id
65:     ST  0,-6(5)     store arg val
* -> call of function: output
66:     ST  5,-4(5)     push ofp
67:     LDA  5,-4(5)    push frame
68:     LDA  0,1(7)     load ac with ret ptr
69:     LDA 7,-63(7)    Jump to function location
70:     LD  5,0(5)      pop frame
* <- call
* Looking up: fac
71:     LD  0,-3(5)     load id
72:     ST  0,-6(5)     store arg val
* -> call of function: output
73:     ST  5,-4(5)     push ofp
74:     LDA  5,-4(5)    push frame
75:     LDA  0,1(7)     load ac with ret ptr
76:     LDA 7,-70(7)    Jump to function location
77:     LD  5,0(5)      pop frame
* <- call
78:     LD  7,-1(5)     return to caller
11:     LDA 7,67(7)     Jump around function: main
* End function: main
79:     ST  5,0(5)      push ofp
80:     LDA  5,0(5)     push frame
81:     LDA  0,1(7)     load ac with ret ptr
82:     LDA  7,-71(7)   jump to main location
83:     LD  5,0(5)      pop frame
84:     HALT  0,0,0     End