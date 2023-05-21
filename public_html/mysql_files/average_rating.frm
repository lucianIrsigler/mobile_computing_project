CREATE TABLE average_rating(
productID int,
average_stars float,
PRIMARY KEY (productID),
FOREIGN KEY (productID) references products(productID)
);