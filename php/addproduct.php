<?php
include "category.php";
include "products.php";

$ID=rand();
$name = $_POST["name"];
$description = $_POST["description"];
$price = $_POST["price"];
$category = $_POST["category"];
$dateAdded = $_POST["date_added"];

if (!isValidCategory($category)){
    $data = array("error"=>"invalid category");
    echo json_encode($data);
    exit();
}

while (!(insertProduct($ID,$name,$description,$price,$category,$dateAdded))){
    $ID = rand();
}

