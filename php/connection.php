<?php
$host="127.0.0.1";
$user="s2621933";
$password="s2621933";
$database="d2621933";

$con =mysqli_connect($host,$user,$password,$database);

if (mysqli_connect_errno()){
	echo "failed to connect";
}


