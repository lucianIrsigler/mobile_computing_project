<?php
include "rating.php";

$userID = $_POST["userID"];
$productID = $_POST["productID"];
$stars = $_POST["stars"];

$valid = updateRating($userID,$productID,$stars);

while (!($valid)){
    $valid = updateRating($userID,$productID,$stars);
}