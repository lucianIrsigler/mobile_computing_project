<?php
    include 'connection.php';
    $image = $_POST['image'];

    if(!empty($image)) {
        $image = base64_decode($image);
        $image_name = "image_test_".time().".png";
        $path = $image_name;
        file_put_contents($path, $image);

        $query = "INSERT INTO images (productID, imagePath) VALUES (?, ?);";
        $stmt = mysqli_prepare($con, $query);
        mysqli_stmt_bind_param($stmt, "is", 1, $path);
        mysqli_stmt_execute($stmt);

        echo "success";
    } else {
        echo "Please Select An Image";
    }
