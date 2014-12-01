
<?php 

include('connectdb.php');
 
$postdata = file_get_contents("php://input");
$request = json_decode($postdata);
@$author = $request->author;
@$email = $request->email;
@$title = $request->title;
@$feedback = $request->feedback; 
 
$sqlcode = "INSERT INTO `Feedback`(`Author`, `Email`, `Title`, `Feedback`) VALUES ('$author','$email','$title','$feedback')";


$insert = mysql_query($sqlcode, $con);

if(! $insert)
{
	die('Could not enter data: ' . mysql_error());
}

echo "Entered data successfully\n";

mysql_close($con);

?>
