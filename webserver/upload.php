<?php
    $file_path = "resources/";
    $file_path = $file_path . basename( $_FILES['uploaded_file']['name']);

    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
        http_response_code(200);
	$result = file_get_contents('http://www.alin-inayeh.ro/playground/callerRecord/transcribe.php?filename='. basename($_FILES['uploaded_file']['name']));
	echo $result;
	// todo: delete the file!!!
    } else{
        http_response_code(404);
    }
 ?>
