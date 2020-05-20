db 0
db 0
db 0
db 0
org 30h
load RF, -1
load R1, 1
store R1, [2]
store R1, [2]
load R1, 1
store R1, [3]
store R1, [3]
load R1, 3
load R2, 2
load R1, 2
store R1, [1]
halt
