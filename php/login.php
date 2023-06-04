<?php
	ini_set('display_errors', 1);
	error_reporting(E_ALL);

	include 'connection.php';

	$username = $_REQUEST["Username"];

	$query = "SELECT * FROM users WHERE Username=?;";
	$stmt = mysqli_prepare($con, $query);
        mysqli_stmt_bind_param($stmt, "s", $username);
        mysqli_stmt_execute($stmt);

	$result = mysqli_stmt_get_result($stmt);
	
	$output=array();

	while ($row = $result -> fetch_assoc()){
		$output[] = $row;
	}

	mysqli_stmt_close($stmt);
	echo json_encode($output);
?>
