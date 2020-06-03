db 0
db 0
db 0
db 0
db " "
db " "
db 0
db 0
db "          "
db "          "
org 30h
load RF, -1
load R1, """
store R1, [2.0]
load R1, "1"
store R1, [3.0]
load R1, "2"
store R1, [4.0]
load R1, "3"
store R1, [5.0]
load R1, """
store R1, [6.0]
store R1, [2]
load R2, 1
load R3, 3
load R0, 1
load R5, 1
move R6, R3
jmpLE R2<=R0, 5ah
addi R3, R3, R6
addi R0, R0, R5
jmp 52h
move R1, R3
store R1, [1]
load R2, 8
load R3, [2]
xor R4, R3, RF
load R5, 1
addi R4, R4, R5
move R0, R3
addi R0, R0, RF
load R6, 0
jmpLE R2 <= R0, 76h
addi R2, R2, R4
addi R6, R6, R5
jmp 6eh
move R1, R6
store R1, [0]
load R2, [1]
load R3, [0]
load R4, 1
xor R3, R3, RF
addi R3, R3, R4
addi R1, R2, R3
store R1, [3]
halt
