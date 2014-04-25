/*
 * operations.c
 *
 *  Created on: Apr 9, 2014
 *      Author: teohoch
 */
#include "matrix.h"
#include "operations.h"
#include <stdio.h>

void printMatrix(matrix * a)
{
	printf("\n %c \n", a->id);
	printf("%d \n", a->dim);
	int i,j;

	for (i = 0; i < a->dim; i++)
	{
		for (j = 0; j < a->dim; j++)
		{
			printf("%f   ",(a->data[i][j]));
		}
		printf("\n");
	}
}

void createMatrix(int size, char name, matrix * novo)
{

		novo->data= malloc(sizeof(float *));
		int p, i;

		for(p=0;p<size;p++)
		{
			novo->data[p] = (float*)malloc(size*sizeof(float));
			for(i=0;i<size; i++)
			{
				novo->data[p][i]= 0.0;
			}
		}
		novo->dim = size;
		novo->id = name;

		printMatrix(novo);


}

matrix * add(matrix * a,matrix * b)
{
	 int sizeA = a->dim;
	 int sizeB = b->dim;

	 if (sizeA == sizeB)
	 {
		 matrix resultado;
		 createMatrix(sizeA,'r',&resultado);

		 int i, j;
		 for(i=0; i<sizeA; i++)
		 {
			 for(j=0; j<sizeA; j++)
			 {
				 resultado.data[i][j] = a->data[i][j] + b->data[i][j];
			 }
		 }
		 printMatrix(&resultado);
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
		 matrix resultado;
		 createMatrix(sizeA,'r',&resultado);

		 /* Comenzar calculo*/
		 int i, j, k;
		 for(i=0; i<sizeA; i++)
		 {
			 for(j=0; j<sizeA; j++)
			 {
				 float cell = 0;
				 for(k = 0; k < sizeA; k++)
				/*cambiando estos for puedo convertirlo para que no sea nesesario que las matrices sean cuadradas*/
				 {
					 cell = cell + ((a->data[i][k]) * (b->data[k][j]));
				 }
				 resultado.data[i][j] = cell;
			 }
		   }
		 printMatrix(&resultado);
		 return &resultado;
	 }else
		 return NULL;
}
