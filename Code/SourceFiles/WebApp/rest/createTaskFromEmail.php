
<?php 

include('connectdb.php');

$author = $_POST['author'];
$email = $_POST['email'];
$title = $_POST['title'];
$feedback = $_POST['feedback'];

$sqlcode = "INSERT INTO `Feedback`(`Author`, `Email`, `Title`, `Feedback`) VALUES ('$author','$email','$title','$feedback')";


//$insert = mysql_query($sqlcode, $con);

//if(! $insert)
//{
//	die('Could not enter data: ' . mysql_error());
//}

//echo "Entered data successfully\n";

mysql_close($con);

?>
