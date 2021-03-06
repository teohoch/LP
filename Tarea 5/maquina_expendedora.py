__author__ = 'teohoch'

import sys

items = {"Coca-Cola":480,
         "Privilegio":250,
         "Chocman":120,
         "Doritos":320,
         "Cachantun":580,
         "Sapito":100,
         "Peta Zetas":90,
         "Morochas":150
        }
coinValues = (10,50,100,500)

class Food:
    def __init__(self, name,price):
        self.name = name
        self.price = Money(price)

class Money:
    def __init__(self,currency):
        self.currency = currency
        self.calculateValue()


    def calculateValue(self):
        #calcula el valor total de las monedas
        self.value = 0
        if (isinstance(self.currency,int)):
            self.value = self.currency
        else:
            for n in range(len(self.currency)):
                self.value += self.currency[n]*coinValues[n]
        return self.value


class Machine:
    def __init__(self, items, log):
        self.log = log
        self.forSale = []
        for key in items.keys():
            food = Food(key,items[key])
            self.forSale.append(food)

    def findItem(self,itemName):
        #Busca el item en cuestion
        for item in self.forSale:
            if (item.name == itemName):
                return item

    def calculateChange(self,inputMoney,itemName):
        #Calcula el vuelto
        item = self.findItem(itemName)
        return inputMoney - item.price.value

    def messages(self, exchange, productName):
        #Genera los mensages de acuerdo al valor del vuelto
        if (exchange<0):
            self.log.write("El dinero no alcanza para "+productName+".\n")
        elif (exchange>0):
            self.log.write("Compra de "+productName+" con vuelto igual a " + str(exchange) + ".\n")
        else:
            self.log.write("Compra exacta de "+productName+".\n")

    def transaction(self, currencyValue, productName):
        #ejecuta una transaccion, es decir calcula el vuelto y imprime el mensage correspondiente
        exchange = self.calculateChange(currencyValue,productName)
        self.messages(exchange,productName)

    def executeTransactions(self):
        #ejecuta todas las transacciones cargadas
        for productName, currency in self.transactions:
            inputMoney = Money(currency)
            self.transaction(inputMoney.calculateValue(),productName)

    def loadTransactions(self,inputPath):
        #carga las transacciones desde el archivo de ingreso
        r = open(inputPath,'r')
        self.transactions = []

        second = False
        currency = []

        for line in r:
            if (second == False):
                coins = (line.replace('\n','')).split(" ")
                for n in range(len(coins)):
                    coins[n]= int(coins[n])
                currency = coins
            else:
                self.transactions.append([line.replace('\n',''),currency[:]])
                currency = []
            second = not second


inputPath = "maquina_expendedora.input"
outputPath = "maquina_expendedora2.output"

if (len(sys.argv)>1):
    inputPath = sys.argv[1]

log = open(outputPath,'w')

machine = Machine(items,log)
machine.loadTransactions(inputPath)
machine.executeTransactions()