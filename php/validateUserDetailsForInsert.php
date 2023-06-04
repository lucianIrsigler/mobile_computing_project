<?php
    function searchUserSalt($salt)
    {
        include 'connection.php';
        $query = "SELECT * FROM users WHERE Salt = ?;";

        $stmt = mysqli_prepare($con, $query);
        mysqli_stmt_bind_param($stmt, "s", $salt);
        mysqli_stmt_execute($stmt);
        $result = mysqli_stmt_get_result($stmt);

        return mysqli_num_rows($result) > 0;
    }

    function checkUsername($username)
    {
        include 'connection.php';
        $query = "SELECT * FROM users WHERE Username = ?;";

        $stmt = mysqli_prepare($con, $query);
        mysqli_stmt_bind_param($stmt, "s", $username);
        mysqli_stmt_execute($stmt);
        $result = mysqli_stmt_get_result($stmt);

        return mysqli_num_rows($result) > 0;
    }
?>
