<?php
	$text = file_get_contents('http://alin-inayeh.ro:8888/?filename='.$_GET['filename']);
	mail('alinoch7@gmail.com', 'Voice', $text);
	echo $text;
?>
