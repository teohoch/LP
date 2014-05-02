/*
 * main.c
 *
 *  Created on: Apr 9, 2014
 *      Author: teohoch
 */
#include <stdio.h>
#include "matrix.h"
#include "display.h"


int fileExists(const char * filename)
{
	FILE * file = fopen(filename, "r");
    if (file!=NULL)
    {
        fclose(file);
        return 1;
    }
    return -1;
}

int main(int argc, char *argv[])
{

	char location[] = "instructions.lp";
	char *ruta;

	if(argc>1)
	{
		if( fileExists(argv[1]) != -1 )
		{
		    ruta = argv[1];
		} else {
			ruta = "matrices.lp";
		    printf("Archivo no valido, utilizando archivo predeterminado\n");
		}

	}else
	{
		ruta = "matrices.lp";
	}

	int nMatris;
	FILE *file = fopen( ruta, "r" );
	matrix **matrices;
	matrices = read(file, &nMatris);
	fclose(file);


	instruction instru;

	loadInstructions(&instru,location);
	executeInstructions(instru,matrices,nMatris);


	return 0;



}
