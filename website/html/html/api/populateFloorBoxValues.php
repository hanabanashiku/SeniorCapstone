<?php
    $floorImages = array();
    $buildingIDs = array();

    session_start();
    $servername = "localhost";
    $username = "uploader";
    $password = "Senior19!";
    $dbname = "Navigation";

    // Create connection
    $conn = mysqli_connect($servername, $username, $password, $dbname);
    // Check connection
    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $currentBuilding = $_POST['building'];
    $currentUser = $_SESSION['username'];
    $sql = "SELECT * FROM buildings WHERE name = '$currentBuilding' AND owner = '$currentUser'"; 
    if ($res = mysqli_query($conn, $sql)) {
        if (mysqli_num_rows($res) > 0) {
            while ($row = mysqli_fetch_array($res)) {
                $buildingIDs = $row['id'];
            }
            // mysqli_free_res($res);  //This is breaking the .php file, not sure why...
        }
        else {
            // echo "No matching records are found.";
        }
    }
    else {  
        // echo "ERROR: Could not able to execute $sql. " .mysqli_error($conn);
    }

    $buildingID = end($buildingIDs);
    $sql = "SELECT * FROM floors WHERE buildingID = '$buildingID'";

    if ($res = mysqli_query($conn, $sql)) {
        if (mysqli_num_rows($res) > 0) {
            while ($row = mysqli_fetch_array($res)) {
                array_push($floorImages, $row['path_to_image']);
            }
            // mysqli_free_res($res);  //This is breaking the .php file, not sure why...
        }
        else {
            // echo "No matching records are found.";
        }
    }
    else {  
        // echo "ERROR: Could not able to execute $sql. " .mysqli_error($conn);
    }
    mysqli_close($conn);

    $floorImagesJSON = json_encode($floorImages);
    echo $floorImagesJSON; 
?>