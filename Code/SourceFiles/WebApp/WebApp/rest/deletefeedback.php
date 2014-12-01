
<?php 

include('connectdb.php');

$sqlcode = "DELETE FROM `Feedback`";

$insert = mysql_query($sqlcode, $con);

if(! $insert)
{
	die('Could not enter data: ' . mysql_error());
}

echo "Delted data successfully\n";

mysql_close($con);

?>
