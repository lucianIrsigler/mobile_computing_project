<?php
include "category.php";
include "products.php";

$category = $_GET["category"];

if (!isValidCategory($category)){
    $data = array("error"=>"invalid category");
    echo json_encode($data);
    exit();
}

$data = selectProductsCategory($category);

echo json_encode($data);

