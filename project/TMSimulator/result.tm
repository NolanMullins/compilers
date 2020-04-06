0:      LD  6,0(0)      load gp with maxaddress
1:      LDA  5,0(6)     copy to gp to fp
2:      ST  0,0(0)      clear location 0
4:      ST  0,-1(5)     store return
5:      IN  0,0,0       Input
6:      LD  7,-1(5)     Return to caller
7:      ST  0,-1(5)     store return
8:      LD  0,-2(5)     load output value
9:      OUT  0,0,0      output
10:     LD  7,-1(5)     return to caller
3:      LDA 7,7(7)      Jump around I/O code
12:     ST  0,-1(5)     store return
13:     LD  0,-2(5)     load id
14:     ST  0,-6(5)     store arg val
15:     ST  5,-4(5)     push ofp
16:     LDA  5,-4(5)    push frame
17:     LDA  0,1(7)     load ac with ret ptr
18:     LDA 7,-12(7)    Jump to function location
19:     LD  5,0(5)      pop frame
20:     LD  0,-3(5)     load id
21:     ST  0,-6(5)     store arg val
22:     ST  5,-4(5)     push ofp
23:     LDA  5,-4(5)    push frame
24:     LDA  0,1(7)     load ac with ret ptr
25:     LDA 7,-19(7)    Jump to function location
26:     LD  5,0(5)      pop frame
27:     LD  0,-2(5)     load id
28:     ST  0,-4(5)     op: push tmp left
29:     LD  0,-3(5)     load id
30:     LD  1,-4(5)     op: load left
31:     ADD  0,1,0      op +
32:     LD  7,-1(5)     return to caller
33:     LD  7,-1(5)     return to caller
11:     LDA 7,22(7)     Jump around function: add
35:     ST  0,-1(5)     store return
36:     LDA  0,-2(5)    load id
37:     ST  0,-3(5)     op: push tmp left
38:     ST  5,-4(5)     push ofp
39:     LDA  5,-4(5)    push frame
40:     LDA  0,1(7)     load ac with ret ptr
41:     LDA 7,-38(7)    Jump to function location
42:     LD  5,0(5)      pop frame
43:     LD  1,-3(5)     op: load left
44:     ST  0,0(1)      assign: store value
45:     LDA  0,0(6)     load id
46:     ST  0,-3(5)     op: push tmp left
47:     LDC  0,10(0)    load constant
48:     LD  1,-3(5)     op: load left
49:     ST  0,0(1)      assign: store value
50:     LD  0,-2(5)     load id
51:     ST  0,-7(5)     store arg val
52:     LD  0,0(6)      load id
53:     ST  0,-8(5)     store arg val
54:     ST  5,-5(5)     push ofp
55:     LDA  5,-5(5)    push frame
56:     LDA  0,1(7)     load ac with ret ptr
57:     LDA 7,-46(7)    Jump to function location
58:     LD  5,0(5)      pop frame
59:     ST  0,-5(5)     store arg val
60:     ST  5,-3(5)     push ofp
61:     LDA  5,-3(5)    push frame
62:     LDA  0,1(7)     load ac with ret ptr
63:     LDA 7,-57(7)    Jump to function location
64:     LD  5,0(5)      pop frame
65:     LD  7,-1(5)     return to caller
34:     LDA 7,31(7)     Jump around function: main
66:     ST  5,-1(5)     push ofp
67:     LDA  5,-1(5)    push frame
68:     LDA  0,1(7)     load ac with ret ptr
69:     LDA  7,-35(7)   jump to main location
70:     LD  5,0(5)      pop frame
71:     HALT  0,0,0     End