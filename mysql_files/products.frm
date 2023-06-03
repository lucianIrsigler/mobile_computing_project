CREATE TABLE products(
productID int,
productName varchar(255),
productDescription (255),
price int,
category varchar(255),
dateAdded date,
PRIMARY KEY productID,
FOREIGN KEY (category) REFERENCES category(category),
);