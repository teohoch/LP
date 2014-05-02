CC			=	gcc
CFLAGS		=  -c -Wall

all: tarea1 display

tarea1:  display main.o 
	$(CC)   main.o  -L. -l display -l matrix -o tarea1

main.o: main.c display.h matrix.h
	$(CC) $(CFLAGS) main.c	

display: display.o  operations.o
	ar -rcs libdisplay.a $^
	
display.o: display.c display.h operations.h operations.o
	$(CC) $(CFLAGS) display.c

operations.o: operations.c operations.h
	$(CC) $(CFLAGS)	operations.c

clean:
	rm -rf *.o libdisplay.a tarea1

