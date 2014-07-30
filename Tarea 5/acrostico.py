__author__ = 'teohoch'

import sys

def reader(archivoInput):
    #Lee el archivo de entrada
    r = open(archivoInput,'r')
    words = []
    for line in r:
        words.append(line.replace('\n',''))
    return words

def createDictionary(words):
    #crea un diccionario en la forma {letra:(palabras_que_comienzan_con_esa_letra)}
    dictionary = {}
    for word in words:
        if (word[0] in dictionary.keys()):
            wordIn = dictionary[word[0]]
            wordIn.append(word)
            dictionary[word[0]]=wordIn;
        else:
            dictionary[word[0]]=[word]

    return dictionary

def checkIfPosible(singleWord,dictionary):
    #checkea si una palabra se puede conformar con las iniciales de las demas
    posible = True
    for character in singleWord:
        if character not in dictionary.keys():
            posible = False
            break
    return posible

def checkAll(everyWord, dictionary):
    #checkea que palabras se pueden conformar con las iniciales de las demas
    canDoWords = []
    for word in everyWord:
        if checkIfPosible(word,dictionary):
            canDoWords.append(word)
    return canDoWords

def shiftLeft(n, list):
    #'gira circularmente' la lista hacia la izquierda n espacios
    return list[n:]+ list[:n]

def getSingleWordComposition(checkedWord, dictionary):
    #genera una lista de las palabras con las cuales se puede generar la palabra en cuestion.
    #a esta lista la llamaremos composicion
    composition = []
    for character in checkedWord:
        composition.append(dictionary[character][0])
        if (len(dictionary[character])>1):
            dictionary[character] = shiftLeft(1,dictionary[character])
    return composition

def getAllCompositions(checkedWords, dictionary):
    #genera una lista de composiciones de todas las palabras
    compositions = []
    for singleWord in checkedWords:
        compositions.append(getSingleWordComposition(singleWord,dictionary.copy()))
    return compositions

def writer(outputPath,compositions):
    #imprime a un archivo las composiciones
    w = open(outputPath,'w')
    for composition in compositions:
        for element in composition:
            w.write(element + '\n')
        w.write("\n")


inputPath = 'acrostico.input'
outputPath = 'acrostico.output'

if (len(sys.argv)>1):
    inputPath = sys.argv[1]



words = reader(inputPath)
dictionary = createDictionary(words)
checkedWords = checkAll(words,dictionary)
compositions = getAllCompositions(checkedWords,dictionary)
writer(outputPath, compositions)