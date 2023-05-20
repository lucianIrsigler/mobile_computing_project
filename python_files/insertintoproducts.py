import requests
#testing if we can do a POST request to the lampserver
url = "https://lamp.ms.wits.ac.za/home/s2621933/addproduct.php"

def createObj():
    outputFields = ["name","description","price","category","date_added"]
    outputDict={}

    for i in outputFields:
        value = input(f"enter {i}:")

        if i == "price":
            value = int(value)

        outputDict[i]=value
    
    return outputDict


myobj = createObj()

x =requests.post(url,data=myobj)

print(x.text)