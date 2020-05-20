db 0
db 0
org 30h
load RF, -1
load R2, 5
load R3, 5
load R0, 1
load R5, 1
move R6, R3
jmpLE R2<=R0, 44h
addi R3, R3, R6
addi R0, R0, R5
jmp 60
move R1, R3
store R1, [0]
load R2, [0]
load R3, 10
load R4, 1
xor R3, R3, RF
addi R3, R3, R4
addi R1, R2, R3
store R1, [1]
halt
