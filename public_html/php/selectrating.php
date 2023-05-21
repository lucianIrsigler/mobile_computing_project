<?php
include "rating.php";

/*
Depending on the 'mode' posted, script will select something different from the reviews table
Mode has to be an int
0->userID
1->productID
*/
$mode = $_POST["mode"];
$value = $_POST["value"];

$validModes = array(0,1);

if (in_array($validModes,$mode) or gettype($mode)!="string"){
    $data = array("error"=>"invalid mode");
    echo json_encode($data);
    exit();
}else if (empty($value)){
    $data = array("error"=>"value is empty");
    echo json_encode($data);
    exit();
}

$map_mode_to_element = array(0=>"userID",1=>"productID");

$data = selectRating($map_mode_to_element[$mode],$value);

echo json_encode($data);