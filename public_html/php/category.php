<?php
/**
 * Checks if inputted category is found in the category table
 *
 * @param string $categoryInput category to check
 * @return bool true if category is in the category table,else false
 */
function isValidCategory($categoryInput){
    include 'connection.php';
    $query = "SELECT * FROM category where category=?";

    $stmt = mysqli_prepare($con,$query);
    mysqli_stmt_bind_param($stmt,"s",$categoryInput);
    mysqli_stmt_execute($stmt);

    $result = mysqli_stmt_get_result($stmt);
    $count = mysqli_num_rows($result);
    mysqli_stmt_close($stmt);

    return ($count == 1);
}