/*
 * main.c
 *
 *  Created on: Apr 9, 2014
 *      Author: teohoch
 */
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include "matrix.h"
#include "display.h"


typedef struct _instruction{
  int n;
  char** data;
} instruction;

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

void createInstruction(instruction *novo,int size)
{
	novo->data= malloc(sizeof(char*));
	int p;

	for(p=0;p<size;p++)
	{
		novo->data[p] = (char*)malloc(10*sizeof(char));
	}
	novo->n = size;
}


void loadInstructions(instruction *instru, char * location)
{
	int n;
	FILE * file;
	file = fopen(location,"r");
	char buffer[10];
	fgets ( buffer, sizeof buffer, file );
	n = atoi(buffer);
	int i;

	createInstruction(instru,n);

	for(i=0;i<n;i++)
	{
		fgets ( buffer, sizeof buffer, file );
		strcpy(instru->data[i],buffer);
	}
	fclose(file);
}

int findMatrix(matrix **matrices, int nMatrix ,char letra)
{
	int lugar = -1;
	int i;
	for (i=0;i<nMatrix;i++)
	{
		char fromMatrix = tolower(matrices[i]->id);
		char fromLetra = tolower(letra);
		if(fromMatrix == fromLetra)
		{
			lugar = i;
			break;
		}
	}
	return lugar;
}

void executeInstructions(instruction instru, matrix ** arrayMatrix, int nMatrix)
{
	int i;
	for (i = 0; i < instru.n; ++i)
	{
		if (i!=0)
			printf("\n");
		char command[10];
		strcpy(command,instru.data[i]); 	//Copiar el comanco

		char firstId, secondId,operation;
		firstId = command[0];
		secondId = command[2];
		operation = tolower(command[4]);				//Separar comando en ids de matrices y operaciones


		int first, second;
		first = findMatrix(arrayMatrix,nMatrix,firstId);
		second = findMatrix(arrayMatrix,nMatrix,secondId);	//encontrar la posicion de las matrices dentro del arreglo


		if(operation=='a')
		{
			printf("%c + %c\n",firstId,secondId);
			display(arrayMatrix[first],arrayMatrix[second],add);
		}
		else if(operation == 'm')
		{
			printf("%c * %c\n",firstId,secondId);
			display(arrayMatrix[first],arrayMatrix[second],mult);
		}


	}
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
