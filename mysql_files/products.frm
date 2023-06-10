CREATE TABLE products(
productID INTEGER NOT NULL AUTO_INCREMENT,
userID INTEGER NOT NULL,
productName VARCHAR(50),
productDescription VARCHAR(255),
price FLOAT(2),
category VARCHAR(50) NOT NULL,
dateAdded DATE,
PRIMARY KEY(productID),
FOREIGN KEY(userID) REFERENCES users(userID) ON DELETE CASCADE,
FOREIGN KEY(category) REFERENCES category(category) ON DELETE CASCADE
);