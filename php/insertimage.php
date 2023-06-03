<?php
// Check if a file was uploaded successfully

function uploadFile()
{
    //$FILES is the files portion of $POST
    $file_name = $_FILES['image']['name'];
    $file_tmp = $_FILES['image']['tmp_name'];

    // target dir
    $target_directory = '/home/s2621933/public_html/uploads/';
    $target_file = $target_directory . $file_name;
    $move = move_uploaded_file($file_tmp, $target_file);

    // Move the uploaded file to the target directory
    if ($move) {
        $data = array("success" => 1);
    } else {
        $data = array("error" => "error uploading file");
    }
    return $data;
}

if (isset($_FILES['image']) && $_FILES['image']['error'] === UPLOAD_ERR_OK) {
    // File information
    $data = uploadFile();
    echo json_encode($data);
} else {
    $data = array("error"=>"no file uploaded or another error");
    echo json_encode($data);
}
