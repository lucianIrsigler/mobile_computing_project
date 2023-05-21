<?php
include "rating.php";

$reviewID=rand();
$userID = $_POST["userID"];
$productID = $_POST["productID"];
$stars = $_POST["stars"];
$date = $_POST["date"];

while (!(insertRating($reviewID,$userID,$productID,$stars,$date))){
    $reviewID = rand();
}