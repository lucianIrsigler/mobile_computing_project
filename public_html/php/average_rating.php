<?php

function updateAverageRating($productID,$averageStars){
    include 'connection.php';

    $query = "INSERT INTO average_rating (productID, average_stars) 
    VALUES (?, ?) ON DUPLICATE KEY UPDATE average_stars = ?";

    $stmt = mysqli_prepare($con,$query);

    mysqli_stmt_bind_param($stmt,"idd",
        $productID,$averageStars,$averageStars);

    try {
        mysqli_stmt_execute($stmt);
    }catch (Exception $e){
        return false;
    }

    $isValid = (mysqli_stmt_affected_rows($stmt) > 0);
    mysqli_stmt_close($stmt);
    return $isValid;
}