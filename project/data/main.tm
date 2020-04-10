* C-Minus Compilation to TM Code
* File: data/main.cm
* Standard prelude:
0: LD 6,0(0)	load gp with maxaddress
1: LDA 5,0(6)	copy to gp to fp
2: ST 0,0(0)	clear location 0
* Jump around i/o routines here
* Function input
4: ST 0,-1(5)	store return
5: IN 0,0,0	Input
6: LD 7,-1(5)	Return to caller
* code for output routine
7: ST 0,-1(5)	store return
8: LD 0,-2(5)	load output value
9: OUT 0,0,0	output
10: LD 7,-1(5)	return to caller
3: LDA 7,7(7)	Jump around I/O code
* End of standard prelude.
* Function: test
12: ST 0,-1(5)	store return
* Processing local var[] declaration: x
* Looking up: x
13: LD 0,-2(5)	load id
14: ST 0,-5(5)	op: push tmp left
* -> cosntant: 4
15: LDC 0,4(0)	load constant
* <- constant
16: JLT 0,1(7)	Halt if subscript < 0
17: LDA 7,1(7)	Jump over if not
18: HALT 0,0,0	End (rip)
19: LD 1,-5(5)	load array base addr
20: SUB 0,1,0	base is at top of array
21: LD 0,0(0)	load value at array index
22: ST 0,-5(5)	store arg val
* -> call of function: output
23: ST 5,-3(5)	push ofp
24: LDA 5,-3(5)	push frame
25: LDA 0,1(7)	load ac with ret ptr
26: LDA 7,-20(7)	Jump to function location
27: LD 5,0(5)	pop frame
* <- call
28: LD 7,-1(5)	return to caller
11: LDA 7,17(7)	Jump around function: test
* End function: test
* Function: main
30: ST 0,-1(5)	store return
* Processing local var[] declaration: x
* -> op
* Looking up: x
31: LDA 0,-2(5)	load id
32: ST 0,-12(5)	op: push tmp left
* -> cosntant: 4
33: LDC 0,4(0)	load constant
* <- constant
34: JLT 0,1(7)	Halt if subscript < 0
35: LDA 7,1(7)	Jump over if not
36: HALT 0,0,0	End (rip)
37: LD 1,-12(5)	load array base addr
38: SUB 0,1,0	base is at top of array
39: ST 0,-12(5)	op: push tmp left
* -> cosntant: 3
40: LDC 0,3(0)	load constant
* <- constant
41: LD 1,-12(5)	op: load left
42: ST 0,0(1)	assign: store value
* <- op
* Looking up: x
43: LDA 0,-2(5)	load id
44: ST 0,-14(5)	store arg val
* -> call of function: test
45: ST 5,-12(5)	push ofp
46: LDA 5,-12(5)	push frame
47: LDA 0,1(7)	load ac with ret ptr
48: LDA 7,-37(7)	Jump to function location
49: LD 5,0(5)	pop frame
* <- call
50: LD 7,-1(5)	return to caller
29: LDA 7,21(7)	Jump around function: main
* End function: main
51: ST 5,0(5)	push ofp
52: LDA 5,0(5)	push frame
53: LDA 0,1(7)	load ac with ret ptr
54: LDA 7,-25(7)	jump to main location
55: LD 5,0(5)	pop frame
56: HALT 0,0,0	End
