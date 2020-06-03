db 0
db 0
db "          "
db 0
org 30h
load RF, -1
load R1, 0
store R1, [12]
store R1, [12]
load R1, 2
store R1, [1]
load R1, [1]
load R2, 2
xor R0, R1, R2
jmpEQ R1=R0, nextInstruction0
load R1, 1
store R1, [0]
nextInstruction0:
halt
