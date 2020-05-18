db 0
db 0
org 30h
load RF, -1
load R1, 9
store R1, [0]
load R2, [0]
load R3, 8
load R5, 255
xor R4, R3, R5
load R6, 1
load R7, 0
addi R4, R4, R6
move R0, R3
addi R0, R0, RF
jmpLE R2 <= R0, 52h
addi R2, R2, R4
move R0, R2
addi R7, R7, R6
jmpLE R3<=R0, 4ah
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
load R5, 255
xor R4, R3, R5
load R6, 1
load R7, 0
addi R4, R4, R6
move R0, R3
addi R0, R0, RF
jmpLE R2 <= R0, 84h
addi R2, R2, R4
move R0, R2
addi R7, R7, R6
jmpLE R3<=R0, 7ch
move R1, R2
store R1, [1]
jmp 56h
nextInstruction0:
halt
