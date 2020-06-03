db 0
org 30h
load RF, -1
load R2, 1
load R3, 5
load R0, 1
load R5, 1
move R6, R3
jmpLE R2<=R0, 44h
addi R3, R3, R6
addi R0, R0, R5
jmp 3ch
move R1, R3
store R1, [0]
halt
