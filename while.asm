db 0
db 0
org 60h
load RF, -1
load R1, 5
store R1, [0]
load R1, 1
store R1, [1]
load R1, 0
load R2, [0]
or R1, R1, R2
load R2, [0]
or R1, R1, R2
load R2, 0
or R1, R1, R2
load R2, 0
or R1, R1, R2
load R2, 0
or R1, R1, R2
load R2, 0
or R1, R1, R2
load R2, 0
or R1, R1, R2
load R0, 0
jmpEQ R2=R0, nextInstruction0
load R1, 0
store R1, [1]
jmp 84h
nextInstruction0:
halt
