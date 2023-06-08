<?php
include 'connection.php';

error_reporting(E_ALL);
ini_set('display_errors', 1);

$track = 0;

for ($i = 0; $i < 10; $i++)
{
    if (isset($_POST['image'.$i]))
    {
        $image = $_POST['image'.$i];
        $image = str_replace('data:image/png;base64,', '', $image);
        $image = str_replace(' ', '+', $image);
        $image = base64_decode($image);
        $imageName = "image_test_" . time() . ".png";
        $path = $imageName;
        $productID = 3;
        file_put_contents($path, $image);

        $query = "INSERT INTO images (productID, imagePath) VALUES (?, ?);";
        $stmt = mysqli_prepare($con, $query);
        mysqli_stmt_bind_param($stmt, "is", $productID, $path);
        mysqli_stmt_execute($stmt);
        mysqli_stmt_close($stmt);

        $track++;
    }
    else if ($i == 0) {
        echo "No Image(s) found";
        break;
    }
    else {
        break;
    }
}

if ($track > 0) {
    echo "Image(s) uploaded successfully!";
} 

mysqli_close($con);
?>
