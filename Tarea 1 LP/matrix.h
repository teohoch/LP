#ifndef MATRIX_H
#define MATRIX_H

#include <stdio.h>

//Definicion de tipo de dato de matrices.
typedef struct _matrix{
  char id;
  int dim;
  float** data;
} matrix;

//FUNCION MATRIX()
/* Lee la informacion contenida en el handle FILE * input, instanciando las
 * matrices ahi contenidas y retornando punteros a las mismas en un arreglo.
 * La cantidad de matrices instanciadas se almacena en la direccion de memoria
 * indicada en int * qty.
 * Esta funcion NO cierra el archivo indicado por input.
 */
matrix ** read(FILE * input, int * qty);

#endif

