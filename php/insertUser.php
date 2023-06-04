<?php
include 'connection.php';
include 'validateUserDetails.php';

$firstName = $_POST["firstname"];
$lastName = $_POST["lastname"];
$phone = $_POST["phone"];
$dateOfBirth = date("Y-m-d", strtotime($_POST["dateOfBirth"]));
$username = $_POST["username"];
$password = $_POST["password"];
$email = $_POST["email"];
$salt = $_POST["salt"];

if (checkUsername($username)) {
    if (searchUserSalt($salt)) {
        $query = "INSERT INTO users (Salt, Username, Password, Firstname, Lastname, UserDOB, Email, Phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        $stmt = mysqli_prepare($con, $query);
        mysqli_stmt_bind_param($stmt, "ssssssss", $salt, $username, $password, $firstName, $lastName, $dateOfBirth, $email, $phone);
        mysqli_stmt_execute($stmt);

        echo "User created successfully";
    } else {
        echo "Salt already exists";
    }
} else {
    echo "Username already exists";
}

?>
