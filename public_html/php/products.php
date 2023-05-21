<?php
/**
 * Inserts data into products table
 *
 * @param int $ID ID of product
 * @param string $name Name of the product
 * @param string $description Description of the product
 * @param int $price price of the product
 * @param string $category category of the product
 * @param string $dateAdded date the product is added
 * @return bool true if the insertion was successful
 */
function insertProduct($ID,$name,$description,$price,$category,$dateAdded){
    include 'connection.php';
    $query = "INSERT into products(productID,productName,productDescription,price,category,dateAdded)
     VALUES (?,?,?,?,?,?)";

    $stmt = mysqli_prepare($con,$query);

    mysqli_stmt_bind_param($stmt,"ississ",
        $ID,$name,$description,$price,$category,$dateAdded);

    try {
        mysqli_stmt_execute($stmt);
    }catch (Exception $e){
        return false;
    }

    $isValid = (mysqli_stmt_affected_rows($stmt) > 0);
    mysqli_stmt_close($stmt);
    return $isValid;
}

function selectFromProductsName($name){
    include 'connection.php';
    $query = "SELECT * FROM products WHERE productName LIKE CONCAT('%', ?, '%')";

    $stmt = mysqli_prepare($con,$query);

    mysqli_stmt_bind_param($stmt,"s",$name);

    try {
        mysqli_stmt_execute($stmt);
    }catch (Exception $e){
        echo "error occured:" . $e->getMessage();
    }

    $result = mysqli_stmt_get_result($stmt);

    return mysqli_fetch_all($result, MYSQLI_ASSOC);
}

function selectAllProducts(){
    include 'connection.php';

    $query = "SELECT * FROM products";
    $result = mysqli_query($con, $query);

    // Fetch the data into an associative array
    $data = array();
    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = $row;
    }

    // Close the connection
    mysqli_close($con);

    return $data;
}