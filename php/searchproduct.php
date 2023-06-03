<?php
//$searchRequest = $_GET["search"];
//$category = $_GET["category"];
include "products.php";
$output=array();

$searchRequest = $_GET["search"];
$outputArray = selectFromProductsName($searchRequest);

$outputJSON = array();

foreach ($outputArray as $row) {
    $data = array();
    foreach ($row as $key => $value) {
        $data[$key]=$value;
    }
    $outputJSON[] = $data;
}

echo json_encode($outputJSON);