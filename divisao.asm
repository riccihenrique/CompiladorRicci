db 0
org 30h
load RF, -1
load R2, 5
load R3, 3
xor R4, R3, RF
load R5, 1
addi R4, R4, R5
move R0, R3
addi R0, R0, RF
load R6, 0
jmpLE R2 <= R0, 4ah
addi R2, R2, R4
addi R6, R6, R5
jmp 42h
move R1, R6
store R1, [0]
halt
