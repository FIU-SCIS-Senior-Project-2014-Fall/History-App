
<?php 

include('connectdb.php');
 
$postdata = file_get_contents("php://input");
$request = json_decode($postdata);

@$id 			= $request->id; 
 
$sqlcode = "DELETE FROM `Places` WHERE `PlaceId` = '$id' ";


$delete = mysql_query($sqlcode, $con);

if(! $delete)
{
	die('Could not delete row: ' . mysql_error());
}

echo "Delted place with id = $id successfully\n";

mysql_close($con);

?>
