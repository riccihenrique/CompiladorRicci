db 0
db 0
org 30h
load RF, -1
load R1, 9
store R1, [0]
load R2, [0]
load R3, 8
xor R4, R3, RF
load R5, 1
addi R4, R4, R5
move R0, R3
addi R0, R0, RF
load R6, 0
jmpLE R2 <= R0, 4eh
addi R2, R2, R4
addi R6, R6, R5
jmp 46h
move R1, R2
store R1, [1]
load R1, [1]
load R2, 0
xor R0, R1, R2
load R1, 0
jmpEQ R1=R0, nextInstruction0
load R2, [0]
load R3, 1
addi R1, R2, R3
store R1, [0]
load R2, [0]
load R3, 8
xor R4, R3, RF
load R5, 1
addi R4, R4, R5
move R0, R3
addi R0, R0, RF
load R6, 0
jmpLE R2 <= R0, 7ch
addi R2, R2, R4
addi R6, R6, R5
jmp 74h
move R1, R2
store R1, [1]
jmp 52h
nextInstruction0:
halt
