/*
 * display.c
 *
 *  Created on: May 1, 2014
 *      Author: teohoch
 */
#include <stdio.h>
#include <stdlib.h>

#include <ctype.h>
#include "display.h"
#include "operations.h"

void printMatrix(matrix * a)
{
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

void display(matrix * a, matrix * b, ptr_func func)
{
	matrix * result =func(a,b);
	printMatrix(result);
}



