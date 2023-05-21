import requests
#testing if we can do a POST request to the lampserver
url = "https://lamp.ms.wits.ac.za/home/s2621933/addrating.php"

#2321
#54543
#657532

#1412442857

def createObj():
    outputFields = ["userID","productID","stars","date"]
    outputDict={}

    for i in outputFields:
        value = input(f"enter {i}:")

        if i == "stars":
            value = float(value)
        elif i in ["userID","productID"]:
            value = int(value)
        
        outputDict[i]=value
    
    return outputDict


myobj = createObj()

x =requests.post(url,data=myobj)

print(x.text)