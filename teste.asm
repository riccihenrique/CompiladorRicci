db 0
db 0
org 60h
load RF, -1
load R1, null
load R0, 1
jmpEQ R1=R0, nextInstruction0:
load R1, 1
store R1, [0]
nextInstruction0
load R1, 2
store R1, [1]
halt
