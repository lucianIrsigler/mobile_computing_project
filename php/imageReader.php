<?php
include 'connection.php';
$productID = $_POST['productID'];
/*
$userID = $_POST['userID'];
$productName = $_POST['productName'];

$query = "SELECT productID FROM products WHERE userID = ? AND productName = ?;";
$stmt = mysqli_prepare($con, $query);
mysqli_stmt_bind_param($stmt, "is", $userID, $productName);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);
$productID = mysqli_fetch_assoc($result)['productID'];
mysqli_stmt_close($stmt);
*/

$query = "SELECT imagePath FROM images WHERE productID = ?;";
$stmt = mysqli_prepare($con, $query);
mysqli_stmt_bind_param($stmt, "i", $productID);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);
$images = array();
while ($row = mysqli_fetch_assoc($result)) {
    $images[] = $row;
}
mysqli_stmt_close($stmt);
echo json_encode($images);
