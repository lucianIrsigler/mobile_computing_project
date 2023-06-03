CREATE TABLES users(
int userID,
Salt varchar(255),
Username varchar(255),
Password varchar(255),
Firstname varchar(255),
Lastname varchar(255),
UserDOB date,
Email varchar(255),
Phone int,
PRIMARY KEY (userID,userName)
);