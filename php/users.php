<?php
function checkIfValidUser($username,$password){
    include "connection.php";
    $query = "SELECT * FROM users where username=? and password=?";
    $stmt = mysqli_prepare($con,$query);
    mysqli_stmt_bind_param($stmt,"ss",$username,$password);
    mysqli_stmt_execute($stmt);

    $result = mysqli_stmt_get_result($stmt);
    //$row = mysqli_fetch_array($result,MYSQLI_ASSOC);
    $count = mysqli_num_rows($result);
    return ($count==1);
}

