/*
 * main.c
 *
 *  Created on: Apr 9, 2014
 *      Author: teohoch
 */
#include <stdio.h>
#include "matrix.h"
#include "operations.h"

main()
{
	int nMatris;
	char *ruta = "matrices.lp"; /*TODO reemplazar despues para permitir pasar este argumento por consola*/
	FILE *file = fopen( ruta, "r" );
	matrix **matrices;
	matrices = read(file, &nMatris);
	mult(matrices[0],matrices[1]);



}
