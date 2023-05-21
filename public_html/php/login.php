<?php
include "users.php";

$username = $_POST["user"];
$password = $_POST["pass"];

if (checkIfValidUser($username,$password)){
	$data = array("status"=>1);
}else{
	$data = array("status"=>0);
}

echo json_encode($data);
