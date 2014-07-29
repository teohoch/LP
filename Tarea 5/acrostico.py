__author__ = 'teohoch'


def reader(archivoInput):
    r = open(archivoInput,'r')
    words = []
    for line in r:
        words.append(line.replace('\n',''))
    return words

def createDictionary(words):
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
    posible = True
    for character in singleWord:
        if character not in dictionary.keys():
            posible = False
            break
    return posible

def checkAll(everyWord, dictionary):
    canDoWords = []
    for word in everyWord:
        if checkIfPosible(word,dictionary):
            canDoWords.append(word)
    return canDoWords

def shiftLeft(n, list):
    return list[n:]+ list[:n]

def getSingleWordComposition(checkedWord, dictionary):
    composition = []
    for character in checkedWord:
        composition.append(dictionary[character][0])
        if (len(dictionary[character])>1):
            dictionary[character] = shiftLeft(1,dictionary[character])
    return composition

def getAllCompositions(checkedWords, dictionary):
    compositions = []
    for singleWord in checkedWords:
        compositions.append(getSingleWordComposition(singleWord,dictionary.copy()))
    return compositions

def writer(outputPath,compositions):
    w = open(outputPath,'w')
    for composition in compositions:
        for element in composition:
            w.write(element + '\n')
        w.write("\n")


inputPath = 'acrostico.input'
outputPath = 'acrostico.output'

words = reader(inputPath)
dictionary = createDictionary(words)
checkedWords = checkAll(words,dictionary)
compositions = getAllCompositions(checkedWords,dictionary)
writer(outputPath, compositions)