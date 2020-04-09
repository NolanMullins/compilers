* C-Minus Compilation to TM Code
* File: TMSimulator/sort.cm
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
* Processing global var[] declaration: x
* Function: minloc
12:     ST  0,-1(5)     store return
* Processing local var[] declaration: a
* Processing local var declaration: low
* Processing local var declaration: high
* Processing local var declaration: i
* Processing local var declaration: x
* Processing local var declaration: k
* -> op
* Looking up: k
13:     LDA  0,-7(5)    load id
14:     ST  0,-8(5)     op: push tmp left
* Looking up: low
15:     LD  0,-3(5)     load id
16:     LD  1,-8(5)     op: load left
17:     ST  0,0(1)      assign: store value
* <- op
* -> op
* Looking up: x
18:     LDA  0,-6(5)    load id
19:     ST  0,-8(5)     op: push tmp left
* Looking up: a
20:     LD  0,-2(5)     load id
21:     ST  0,-9(5)     op: push tmp left
* Looking up: low
22:     LD  0,-3(5)     load id
23:     JLT  0,1(7)     Halt if subscript < 0
24:     LDA  7,1(7)     Jump over if not
25:     HALT  0,0,0     End (rip)
26:     LD  1,-9(5)     load array base addr
27:     SUB  0,1,0      base is at top of array
28:     LD  0,0(0)      load value at array index
29:     LD  1,-8(5)     op: load left
30:     ST  0,0(1)      assign: store value
* <- op
* -> op
* Looking up: i
31:     LDA  0,-5(5)    load id
32:     ST  0,-8(5)     op: push tmp left
* -> op
* Looking up: low
33:     LD  0,-3(5)     load id
34:     ST  0,-9(5)     op: push tmp left
* -> cosntant: 1
35:     LDC  0,1(0)     load constant
* <- constant
36:     LD  1,-9(5)     op: load left
37:     ADD  0,1,0      op +
* <- op
38:     LD  1,-8(5)     op: load left
39:     ST  0,0(1)      assign: store value
* <- op
* -> While
* -> op
* Looking up: i
40:     LD  0,-5(5)     load id
41:     ST  0,-8(5)     op: push tmp left
* Looking up: high
42:     LD  0,-4(5)     load id
43:     LD  1,-8(5)     op: load left
44:     SUB  0,1,0      op <
* <- op
45:     JLT  0,2(7)     br if true
46:     LDC  0,0(0)     false case
47:     LDA  7,1(7)     unconditional jump
48:     LDC  0,1(0)     true case
* If jump location
* -> if
* -> test
* -> op
* Looking up: a
50:     LD  0,-2(5)     load id
51:     ST  0,-8(5)     op: push tmp left
* Looking up: i
52:     LD  0,-5(5)     load id
53:     JLT  0,1(7)     Halt if subscript < 0
54:     LDA  7,1(7)     Jump over if not
55:     HALT  0,0,0     End (rip)
56:     LD  1,-8(5)     load array base addr
57:     SUB  0,1,0      base is at top of array
58:     LD  0,0(0)      load value at array index
59:     ST  0,-8(5)     op: push tmp left
* Looking up: x
60:     LD  0,-6(5)     load id
61:     LD  1,-8(5)     op: load left
62:     SUB  0,1,0      op <
* <- op
63:     JLT  0,2(7)     br if true
64:     LDC  0,0(0)     false case
65:     LDA  7,1(7)     unconditional jump
66:     LDC  0,1(0)     true case
* If jump location
* <- test
* -> op
* Looking up: x
68:     LDA  0,-6(5)    load id
69:     ST  0,-8(5)     op: push tmp left
* Looking up: a
70:     LD  0,-2(5)     load id
71:     ST  0,-9(5)     op: push tmp left
* Looking up: i
72:     LD  0,-5(5)     load id
73:     JLT  0,1(7)     Halt if subscript < 0
74:     LDA  7,1(7)     Jump over if not
75:     HALT  0,0,0     End (rip)
76:     LD  1,-9(5)     load array base addr
77:     SUB  0,1,0      base is at top of array
78:     LD  0,0(0)      load value at array index
79:     LD  1,-8(5)     op: load left
80:     ST  0,0(1)      assign: store value
* <- op
* -> op
* Looking up: k
81:     LDA  0,-7(5)    load id
82:     ST  0,-8(5)     op: push tmp left
* Looking up: i
83:     LD  0,-5(5)     load id
84:     LD  1,-8(5)     op: load left
85:     ST  0,0(1)      assign: store value
* <- op
* Jump to end of if block
67:     JEQ 0,19(7)     Jump over then block
86:     LDA 7,0(7)      Leave then block
* <- if
* -> op
* Looking up: i
87:     LDA  0,-5(5)    load id
88:     ST  0,-8(5)     op: push tmp left
* -> op
* Looking up: i
89:     LD  0,-5(5)     load id
90:     ST  0,-9(5)     op: push tmp left
* -> cosntant: 1
91:     LDC  0,1(0)     load constant
* <- constant
92:     LD  1,-9(5)     op: load left
93:     ADD  0,1,0      op +
* <- op
94:     LD  1,-8(5)     op: load left
95:     ST  0,0(1)      assign: store value
* <- op
96:     LDA 7,-57(7)    Jump back to test condition
49:     JEQ 0,47(7)     Jump over body
* <- While
* -> return
* Looking up: k
97:     LD  0,-7(5)     load id
98:     LD  7,-1(5)     return to caller
* <- return
99:     LD  7,-1(5)     return to caller
11:     LDA 7,88(7)     Jump around function: minloc
* End function: minloc
* Function: sort
101:    ST  0,-1(5)     store return
* Processing local var[] declaration: a
* Processing local var declaration: low
* Processing local var declaration: high
* Processing local var declaration: i
* Processing local var declaration: k
* -> op
* Looking up: i
102:    LDA  0,-5(5)    load id
103:    ST  0,-7(5)     op: push tmp left
* Looking up: low
104:    LD  0,-3(5)     load id
105:    LD  1,-7(5)     op: load left
106:    ST  0,0(1)      assign: store value
* <- op
* -> While
* -> op
* Looking up: i
107:    LD  0,-5(5)     load id
108:    ST  0,-7(5)     op: push tmp left
* -> op
* Looking up: high
109:    LD  0,-4(5)     load id
110:    ST  0,-8(5)     op: push tmp left
* -> cosntant: 1
111:    LDC  0,1(0)     load constant
* <- constant
112:    LD  1,-8(5)     op: load left
113:    SUB  0,1,0      op -
* <- op
114:    LD  1,-7(5)     op: load left
115:    SUB  0,1,0      op <
* <- op
116:    JLT  0,2(7)     br if true
117:    LDC  0,0(0)     false case
118:    LDA  7,1(7)     unconditional jump
119:    LDC  0,1(0)     true case
* If jump location
* Processing local var declaration: t
* -> op
* Looking up: k
121:    LDA  0,-6(5)    load id
122:    ST  0,-8(5)     op: push tmp left
* Looking up: a
123:    LD  0,-2(5)     load id
124:    ST  0,-11(5)    store arg val
* Looking up: i
125:    LD  0,-5(5)     load id
126:    ST  0,-12(5)    store arg val
* Looking up: high
127:    LD  0,-4(5)     load id
128:    ST  0,-13(5)    store arg val
* -> call of function: minloc
129:    ST  5,-9(5)     push ofp
130:    LDA  5,-9(5)    push frame
131:    LDA  0,1(7)     load ac with ret ptr
132:    LDA 7,-121(7)   Jump to function location
133:    LD  5,0(5)      pop frame
* <- call
134:    LD  1,-8(5)     op: load left
135:    ST  0,0(1)      assign: store value
* <- op
* -> op
* Looking up: t
136:    LDA  0,-7(5)    load id
137:    ST  0,-8(5)     op: push tmp left
* Looking up: a
138:    LD  0,-2(5)     load id
139:    ST  0,-9(5)     op: push tmp left
* Looking up: k
140:    LD  0,-6(5)     load id
141:    JLT  0,1(7)     Halt if subscript < 0
142:    LDA  7,1(7)     Jump over if not
143:    HALT  0,0,0     End (rip)
144:    LD  1,-9(5)     load array base addr
145:    SUB  0,1,0      base is at top of array
146:    LD  0,0(0)      load value at array index
147:    LD  1,-8(5)     op: load left
148:    ST  0,0(1)      assign: store value
* <- op
* -> op
* Looking up: a
149:    LD  0,-2(5)     load id
150:    ST  0,-8(5)     op: push tmp left
* Looking up: k
151:    LD  0,-6(5)     load id
152:    JLT  0,1(7)     Halt if subscript < 0
153:    LDA  7,1(7)     Jump over if not
154:    HALT  0,0,0     End (rip)
155:    LD  1,-8(5)     load array base addr
156:    SUB  0,1,0      base is at top of array
157:    ST  0,-8(5)     op: push tmp left
* Looking up: a
158:    LD  0,-2(5)     load id
159:    ST  0,-9(5)     op: push tmp left
* Looking up: i
160:    LD  0,-5(5)     load id
161:    JLT  0,1(7)     Halt if subscript < 0
162:    LDA  7,1(7)     Jump over if not
163:    HALT  0,0,0     End (rip)
164:    LD  1,-9(5)     load array base addr
165:    SUB  0,1,0      base is at top of array
166:    LD  0,0(0)      load value at array index
167:    LD  1,-8(5)     op: load left
168:    ST  0,0(1)      assign: store value
* <- op
* -> op
* Looking up: a
169:    LD  0,-2(5)     load id
170:    ST  0,-8(5)     op: push tmp left
* Looking up: i
171:    LD  0,-5(5)     load id
172:    JLT  0,1(7)     Halt if subscript < 0
173:    LDA  7,1(7)     Jump over if not
174:    HALT  0,0,0     End (rip)
175:    LD  1,-8(5)     load array base addr
176:    SUB  0,1,0      base is at top of array
177:    ST  0,-8(5)     op: push tmp left
* Looking up: t
178:    LD  0,-7(5)     load id
179:    LD  1,-8(5)     op: load left
180:    ST  0,0(1)      assign: store value
* <- op
* -> op
* Looking up: i
181:    LDA  0,-5(5)    load id
182:    ST  0,-8(5)     op: push tmp left
* -> op
* Looking up: i
183:    LD  0,-5(5)     load id
184:    ST  0,-9(5)     op: push tmp left
* -> cosntant: 1
185:    LDC  0,1(0)     load constant
* <- constant
186:    LD  1,-9(5)     op: load left
187:    ADD  0,1,0      op +
* <- op
188:    LD  1,-8(5)     op: load left
189:    ST  0,0(1)      assign: store value
* <- op
190:    LDA 7,-84(7)    Jump back to test condition
120:    JEQ 0,70(7)     Jump over body
* <- While
191:    LD  7,-1(5)     return to caller
100:    LDA 7,91(7)     Jump around function: sort
* End function: sort
* Function: main
193:    ST  0,-1(5)     store return
* Processing local var declaration: i
* -> op
* Looking up: i
194:    LDA  0,-2(5)    load id
195:    ST  0,-3(5)     op: push tmp left
* -> cosntant: 0
196:    LDC  0,0(0)     load constant
* <- constant
197:    LD  1,-3(5)     op: load left
198:    ST  0,0(1)      assign: store value
* <- op
* -> While
* -> op
* Looking up: i
199:    LD  0,-2(5)     load id
200:    ST  0,-3(5)     op: push tmp left
* -> cosntant: 10
201:    LDC  0,10(0)    load constant
* <- constant
202:    LD  1,-3(5)     op: load left
203:    SUB  0,1,0      op <
* <- op
204:    JLT  0,2(7)     br if true
205:    LDC  0,0(0)     false case
206:    LDA  7,1(7)     unconditional jump
207:    LDC  0,1(0)     true case
* If jump location
* -> op
* Looking up: x
209:    LDA  0,0(6)     load id
210:    ST  0,-3(5)     op: push tmp left
* Looking up: i
211:    LD  0,-2(5)     load id
212:    JLT  0,1(7)     Halt if subscript < 0
213:    LDA  7,1(7)     Jump over if not
214:    HALT  0,0,0     End (rip)
215:    LD  1,-3(5)     load array base addr
216:    SUB  0,1,0      base is at top of array
217:    ST  0,-3(5)     op: push tmp left
* -> call of function: input
218:    ST  5,-4(5)     push ofp
219:    LDA  5,-4(5)    push frame
220:    LDA  0,1(7)     load ac with ret ptr
221:    LDA 7,-218(7)   Jump to function location
222:    LD  5,0(5)      pop frame
* <- call
223:    LD  1,-3(5)     op: load left
224:    ST  0,0(1)      assign: store value
* <- op
* -> op
* Looking up: i
225:    LDA  0,-2(5)    load id
226:    ST  0,-3(5)     op: push tmp left
* -> op
* Looking up: i
227:    LD  0,-2(5)     load id
228:    ST  0,-4(5)     op: push tmp left
* -> cosntant: 1
229:    LDC  0,1(0)     load constant
* <- constant
230:    LD  1,-4(5)     op: load left
231:    ADD  0,1,0      op +
* <- op
232:    LD  1,-3(5)     op: load left
233:    ST  0,0(1)      assign: store value
* <- op
234:    LDA 7,-36(7)    Jump back to test condition
208:    JEQ 0,26(7)     Jump over body
* <- While
* Looking up: x
235:    LDA  0,0(6)     load id
236:    ST  0,-5(5)     store arg val
* -> cosntant: 0
237:    LDC  0,0(0)     load constant
* <- constant
238:    ST  0,-6(5)     store arg val
* -> cosntant: 10
239:    LDC  0,10(0)    load constant
* <- constant
240:    ST  0,-7(5)     store arg val
* -> call of function: sort
241:    ST  5,-3(5)     push ofp
242:    LDA  5,-3(5)    push frame
243:    LDA  0,1(7)     load ac with ret ptr
244:    LDA 7,-144(7)   Jump to function location
245:    LD  5,0(5)      pop frame
* <- call
* -> op
* Looking up: i
246:    LDA  0,-2(5)    load id
247:    ST  0,-3(5)     op: push tmp left
* -> cosntant: 0
248:    LDC  0,0(0)     load constant
* <- constant
249:    LD  1,-3(5)     op: load left
250:    ST  0,0(1)      assign: store value
* <- op
* -> While
* -> op
* Looking up: i
251:    LD  0,-2(5)     load id
252:    ST  0,-3(5)     op: push tmp left
* -> cosntant: 10
253:    LDC  0,10(0)    load constant
* <- constant
254:    LD  1,-3(5)     op: load left
255:    SUB  0,1,0      op <
* <- op
256:    JLT  0,2(7)     br if true
257:    LDC  0,0(0)     false case
258:    LDA  7,1(7)     unconditional jump
259:    LDC  0,1(0)     true case
* If jump location
* Looking up: x
261:    LDA  0,0(6)     load id
262:    ST  0,-5(5)     op: push tmp left
* Looking up: i
263:    LD  0,-2(5)     load id
264:    JLT  0,1(7)     Halt if subscript < 0
265:    LDA  7,1(7)     Jump over if not
266:    HALT  0,0,0     End (rip)
267:    LD  1,-5(5)     load array base addr
268:    SUB  0,1,0      base is at top of array
269:    LD  0,0(0)      load value at array index
270:    ST  0,-5(5)     store arg val
* -> call of function: output
271:    ST  5,-3(5)     push ofp
272:    LDA  5,-3(5)    push frame
273:    LDA  0,1(7)     load ac with ret ptr
274:    LDA 7,-268(7)   Jump to function location
275:    LD  5,0(5)      pop frame
* <- call
* -> op
* Looking up: i
276:    LDA  0,-2(5)    load id
277:    ST  0,-3(5)     op: push tmp left
* -> op
* Looking up: i
278:    LD  0,-2(5)     load id
279:    ST  0,-4(5)     op: push tmp left
* -> cosntant: 1
280:    LDC  0,1(0)     load constant
* <- constant
281:    LD  1,-4(5)     op: load left
282:    ADD  0,1,0      op +
* <- op
283:    LD  1,-3(5)     op: load left
284:    ST  0,0(1)      assign: store value
* <- op
285:    LDA 7,-35(7)    Jump back to test condition
260:    JEQ 0,25(7)     Jump over body
* <- While
286:    LD  7,-1(5)     return to caller
192:    LDA 7,94(7)     Jump around function: main
* End function: main
287:    ST  5,-10(5)    push ofp
288:    LDA  5,-10(5)   push frame
289:    LDA  0,1(7)     load ac with ret ptr
290:    LDA  7,-98(7)   jump to main location
291:    LD  5,0(5)      pop frame
292:    HALT  0,0,0     End