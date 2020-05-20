db 0
org 30h
load RF, -1
load R1, 1
store R1, [0]
jmp for0
load R2, [0]
load R3, 1
addi R1, R2, R3
store R1, [0]
for0:
load R1, [0]
load R2, 7
xor R0, R1, R2
load R1, 0
jmpEQ R1=R0, nextInstruction0
load R2, [0]
load R3, 1
addi R1, R2, R3
store R1, [0]
jmp 38h
nextInstruction0:
halt
