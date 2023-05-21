CREATE TABLE reviews(
reviewID int,
userID int,
productID int,
stars float,
date date,
PRIMARY KEY (userID, productID),
FOREIGN KEY (userID) REFERENCES users(userID),
FOREIGN KEY (productID) REFERENCES products(productID)
);