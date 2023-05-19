<?php
include "connection.php";
$username = $_POST["user"];
$password = $_POST["pass"];

$stmt = mysqli_prepare($con,"SELECT * FROM users where username=? and password=?");
mysqli_stmt_bind_param($stmt,"ss",$username,$password);
mysqli_stmt_execute($stmt);

$result = mysqli_stmt_get_result($stmt);
$row = mysqli_fetch_array($result,MYSQLI_ASSOC);
$count = mysqli_num_rows($result);

if ($count==1){
        $data = array("status"=>1);
}else{
        $data = array("status"=>0);
}

echo json_encode($data);
?>