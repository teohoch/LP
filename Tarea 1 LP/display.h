#ifndef DISPLAY_H
#define DISPLAY_H

#include "operations.h"

//FUNCION DISPLAY()
/* Aplica la funcion apuntada por func a las matrices apuntadas por a y b.
 * Imprime por la salida estandar la matriz resultante de dicha operacion.
 */
void display(matrix * a, matrix * b, ptr_func func);

void loadInstructions(char ** instructions, int * nInstructions, char * location);
//Funcion LoadInstructions
/* Carga las instrucciones a un arreglo arreglos de char en la forma
 * [id matriz 1][id matriz 1][operacion] con los ids de matrices tomando una sola letra cada uno,
 * y la operacion los valores 'A' para suma y 'M' para multiplicacion.
 *
 * Recibe el arreglo de arreglo de chars donde se guardaran las instruciones,
 *  el puntero a un int donde se guardan el numero de instrucciones,
 *  y un arreglo de chars donde se especifica la localizacion y nombre del archivo de matrices.
 */

#endif

