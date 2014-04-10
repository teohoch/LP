/*
 * operations.c
 *
 *  Created on: Apr 9, 2014
 *      Author: teohoch
 */
#include "matrix.h"

matrix * add(matrix * a,matrix * b)
/*source http://www.phanderson.com/C/mat_add.html */
{


	 int i, j;
	 int sizeA = a->dim;
	 int sizeB = b->dim;


	 if (sizeA == sizeB)
	 {
		 /* Inicializar matriz de resultado */
		 matrix *resultado;
		 resultado = (matrix *)mallock(sizeof(matrix));
		 resultado->id = "r";
		 resultado->dim = sizeA;
		 float data[sizeA][sizeA] = {0};
		 resultado->data = &data;

		 /* Comenzar calculo*/
		 for(i=0; i<sizeA; i++)
		 {
			 for(j=0; j<sizeA; j++)
			 {
				 resultado->data[i][j] = a->data[i][j] + b->data[i][j];
			 }
		   }
		 return resultado;
	 }else
		 return NULL;
}

matrix * mult(matrix * a,matrix * b)
{


	 int i, j;
	 int sizeA = a->dim;
	 int sizeB = b->dim;


	 if (sizeA == sizeB)
	 {
		 /* Inicializar matriz de resultado */
		 matrix *resultado;
		 resultado = (matrix *)mallock(sizeof(matrix));
		 resultado->id = "r";
		 resultado->dim = sizeA;
		 float data[sizeA][sizeA] = {0};
		 resultado->data = &data;

		 /* Comenzar calculo*/
		 for(i=0; i<sizeA; i++)
		 {
			 for(j=0; j<sizeA; j++)
			 {
				 /*TODO Falta un for aqui para el calculo de multiplicacion*/
			 }
		   }
		 return resultado;
	 }else
		 return NULL;
}

