<?php 

include('connectdb.php');

$input = $_GET['input'];
$sqlcode = mysql_query("Select count(*) as placesCreatedToday FROM Places WHERE CreatedDate >= now() - INTERVAL 1 DAY");

$jsonObj = array();

while($result = mysql_fetch_object($sqlcode))
{
	$jsonObj[] = $result;
}

$final_res = json_encode($jsonObj);

echo $final_res;

?>
