#ifndef OPERATIONS_H
#define OPERATIONS_H

#include "matrix.h"

//FUNCION MULT()
/* Retorna el resultado de la multiplicacion de las matrices indicadas por
 * parametro, en el orden en que son ingresadas (de izquierda a derecha).
 */
matrix * mult(matrix * a,matrix * b);

//FUNCION ADD()
/* Retorna el resultado de la suma de las matrices indicadas por parametro.
 */
matrix * add(matrix * a,matrix * b);

typedef matrix * (*ptr_func)(matrix *, matrix *);

void printMatrix(matrix * a);

void createMatrix(int size, char name,matrix * novo);

#endif


