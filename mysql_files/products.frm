CREATE TABLE products(
productID INTEGER NOT NULL AUTO_INCREMENT,
productName VARCHAR(50),
productDescription VARCHAR(255),
price FLOAT(2),
category VARCHAR(50),
dateAdded DATE,
PRIMARY KEY(productID),
FOREIGN KEY(category) REFERENCES category(category) ON DELETE CASCADE);
