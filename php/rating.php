<?php

/**
 * Gets the averages stars and updates the value in the average_rating table
 *
 * @param int $productID product to update
 */
function updateAvgStars($productID){
    $averageStars = averageRating($productID);
    updateAverageRating($productID,$averageStars);
}

/**
 * Checks if inputted category is found in the category table
 *
 * @param $productID
 * @return mixed
 */
function averageRating($productID){
    include 'connection.php';
    $query = "SELECT ROUND(AVG(stars),2) as average_stars FROM reviews where productID=?";

    $stmt = mysqli_prepare($con,$query);

    mysqli_stmt_bind_param($stmt,"i",
        $productID);

    try {
        mysqli_stmt_execute($stmt);
    }catch (Exception $e){
        echo "error occured:" . $e->getMessage();
    }

    mysqli_stmt_bind_result($stmt, $averageRating);
    mysqli_stmt_fetch($stmt);

    mysqli_stmt_close($stmt);
    mysqli_close($con);

    return $averageRating;
}

/**
 * Checks if inputted category is found in the category table
 *
 * @param $reviewID
 * @param $userID
 * @param $productID
 * @param $stars
 * @param $date
 * @return bool true if category is in the category table,else false
 */
function insertRating($reviewID,$userID,$productID,$stars,$date){
    include 'connection.php';
    include "average_rating.php";

    $query = "INSERT into reviews(reviewID,userID,productID,stars,date)
     VALUES (?,?,?,?,?)";

    $stmt = mysqli_prepare($con,$query);

    mysqli_stmt_bind_param($stmt,"iiids",
        $reviewID,$userID,$productID,$stars,$date);

    try {
        mysqli_stmt_execute($stmt);
    }catch (Exception $e){
        return false;
    }

    $isValid = (mysqli_stmt_affected_rows($stmt) > 0);

    mysqli_stmt_close($stmt);

    updateAvgStars($productID);

    return $isValid;
}

/**
 * Checks if inputted category is found in the category table
 *
 * @param $userID
 * @param $productID
 * @param $stars
 * @return bool true if category is in the category table,else false
 */
function updateRating($userID,$productID,$stars){
    include 'connection.php';
    $query = "UPDATE reviews set stars = ? where userID=? and productID=?";

    $stmt = mysqli_prepare($con,$query);

    mysqli_stmt_bind_param($stmt,"dii",
        $stars,$userID,$productID);

    try {
        mysqli_stmt_execute($stmt);
    }catch (Exception $e){
        return false;
    }

    updateAvgStars($productID);
    $valid = (mysqli_stmt_affected_rows($stmt) > 0);

    mysqli_stmt_close($stmt);
    mysqli_close($con);

    return $valid;
}

/**
 * Checks if inputted category is found in the category table
 *
 * @param $whereClauseElement
 * @param $valueToBind
 * @return bool true if category is in the category table,else false
 */
function selectRating($whereClauseElement,$valueToBind){
    include 'connection.php';
    include "misc.php";

    $query = "SELECT * FROM reviews where $whereClauseElement=?";

    $stmt = mysqli_prepare($con,$query);

    mysqli_stmt_bind_param($stmt,mapTypeToStmtBind($valueToBind),
        $valueToBind);

    try {
        mysqli_stmt_execute($stmt);
    }catch (Exception $e){
        echo "error occured:" . $e->getMessage();
    }

    $result = mysqli_stmt_get_result($stmt);
    $output = mysqli_fetch_all($result, MYSQLI_ASSOC);

    mysqli_stmt_close($stmt);
    mysqli_close($con);

    return $output;
}

/**
 * Checks if inputted category is found in the category table
 *
 * @param $whereClauseElement
 * @param $valueToBind
 * @return array true if category is in the category table,else false
 */
function selectRatingSpecific($userID,$productID){
    include 'connection.php';
    include "misc.php";

    $query = "SELECT * FROM reviews where userID=? and productID=?";

    $stmt = mysqli_prepare($con,$query);

    mysqli_stmt_bind_param($stmt,"ii",$userID,$productID);

    try {
        mysqli_stmt_execute($stmt);
    }catch (Exception $e){
        echo "error occured:" . $e->getMessage();
    }

    $result = mysqli_stmt_get_result($stmt);
    $output = mysqli_fetch_all($result, MYSQLI_ASSOC);

    mysqli_stmt_close($stmt);
    mysqli_close($con);

    return $output;
}

function selectAverageRating($productID){
    include 'connection.php';
    include "misc.php";

    $query = "SELECT * FROM average_rating where productID=?";

    $stmt = mysqli_prepare($con,$query);


    mysqli_stmt_bind_param($stmt,"i",
        $productID);
        
    try {
        mysqli_stmt_execute($stmt);
    }catch (Exception $e){
        return $data;
    }

    $result = mysqli_stmt_get_result($stmt);
    $output = mysqli_fetch_all($result, MYSQLI_ASSOC);

    mysqli_stmt_close($stmt);
    mysqli_close($con);


    if (mysqli_num_rows($result)==0){
        $data = array("error" => "invalid productID");
    }else {
        $data = array("average_rating" => $output[0]["average_stars"]);
    }
    return $data;
}