
<?php 

include('connectdb.php');

$input = $_GET['input'];
$sqlcode = mysql_query("Select * from Places WHERE PlaceId = (Select MAX(PlaceId) from Places)");

$jsonObj = array();

while($result = mysql_fetch_object($sqlcode))
{
	$jsonObj[] = $result;
}

$final_res = json_encode($jsonObj);

echo $final_res;

?>
