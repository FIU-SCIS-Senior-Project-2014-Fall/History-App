
<?php 

include('connectdb.php');

header('Content-type:application/json');

$input = $_GET['input'];
$sqlcode = mysql_query("SELECT * FROM `Places`");

$jsonObj = array();

while($result = mysql_fetch_object($sqlcode))
{
	$jsonObj[] = $result;
}

$final_res = json_encode($jsonObj);

echo $final_res;

?>
