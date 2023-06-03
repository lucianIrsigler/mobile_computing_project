<?php
include "rating.php";

function isValueEmptyOrNotNumeric($value){
    return (empty($value)||!is_numeric($value));
}


function defaultMode($mode,$value){
    $map_mode_to_element = array(0=>"userID",1=>"productID");

    if (isValueEmptyOrNotNumeric($value)){
        $data = array("error"=>"value is empty");
        return $data;
    }

    $data = selectRating($map_mode_to_element[$mode],$value);

    return $data;
}

function mergeMode(){
    $userID = $_GET["userID"];
    $productID = $_GET["productID"];

    if (isValueEmptyOrNotNumeric($userID)){
        $data = array("error"=>"userID is empty");
        echo json_encode($data);
        exit();
    }else if (isValueEmptyOrNotNumeric($productID)) {
        $data = array("error" => "productID is empty");
        echo json_encode($data);
        exit();
    }

    $data = selectRatingSpecific($userID,$productID);

    return $data;
}
/*
Depending on the 'mode' posted, script will select something different from the reviews table
Mode has to be an int
0->userID
1->productID
2- both userID and productID
*/
$mode = $_GET["mode"];
$validModes = array(0,1,2);


//check if mode is not numeric and check if mode is in valid modes
if (!is_numeric($mode) || !in_array((int)$mode, $validModes)) {
    $data = array("error" => "Invalid mode");
    echo json_encode($data);
    exit();
}

switch ((int)$mode){
    case 0:
    case 1:
       $value = $_GET["value"];
       $data = defaultMode((int)$mode,$value);
       echo json_encode($data);
       break;
    case 2:
       $data = mergeMode();
       echo json_encode($data);
       break;
}


