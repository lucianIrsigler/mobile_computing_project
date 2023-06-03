<?php
include "rating.php";
$productID = $_GET["productID"];
$outputJSON = selectAverageRating($productID);
echo json_encode($outputJSON);