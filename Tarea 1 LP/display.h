#ifndef DISPLAY_H
#define DISPLAY_H

#include "operations.h"

//FUNCION DISPLAY()
/* Aplica la funcion apuntada por func a las matrices apuntadas por a y b.
 * Imprime por la salida estandar la matriz resultante de dicha operacion.
 */
void display(matrix * a, matrix * b, ptr_func func);

#endif

