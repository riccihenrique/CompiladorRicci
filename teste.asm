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
org 60h
load RF, -1
load R1, 1
store R1, [2]
load R2, 1
load R3, 3
load R0, 2
load R5, 1
move R6, R3
addi R0, R0, RF
jmpLE R2<=R0, 78h
addi R3, R3, R6
addi R4, R4, R5
move R1, R3
store R1, [1]
load R2, 8
load R3, [2]
load R5, 255
xor R4, R3, R5
load R6, 1
load R7, 0
addi R4, R4, R6
move R0, R3
addi R0, R0, RF
jmpLE R2 <= R0, 98h
addi R2, R2, R4
move R0, R2
addi R7, R7, R6
jmpLE R3<=R0, 90h
move R1, R7
store R1, [0]
load R2, [1]
load R3, [0]
load R4, 1
xor R3, R3, RF
addi R3, R3, R4
addi R1, R2, R3
store R1, [3]
halt
