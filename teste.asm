db 0
org 30h
load RF, -1
load R1, 4
store R1, [0]
load R2, [0]
addi R1, R2, Rf
store R1, [0]
halt
