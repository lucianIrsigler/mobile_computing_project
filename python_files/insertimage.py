import requests

# Specify the URL of the PHP script
url = 'https://lamp.ms.wits.ac.za/home/s2621933/php/insertimage.php'

# Specify the path to the image file
image_path = 'F:\\uni\\mobile_computing_project\\python_files\\test.jpeg'

# Create a dictionary for the files to be uploaded
files = {'image': open(image_path, 'rb')}


# Send the POST request with the image file
response = requests.post(url, files=files)

# Check the response
if response.status_code == 200:
    print("Image uploaded successfully!")
else:
    print("Error uploading image.")