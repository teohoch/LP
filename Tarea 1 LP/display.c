/*
 * display.c
 *
 *  Created on: May 1, 2014
 *      Author: teohoch
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include "display.h"
#include "operations.h"


void display(matrix * a, matrix * b, ptr_func func)
{
	matrix * result =func(a,b);
	printMatrix(result);

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

