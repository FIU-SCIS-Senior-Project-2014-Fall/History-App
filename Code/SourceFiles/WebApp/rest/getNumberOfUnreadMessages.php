<?php 

include('connectdb.php');

$input = $_GET['input'];
$sqlcode = mysql_query("Select count(*) as feedbackNo from Feedback");

$jsonObj = array();

while($result = mysql_fetch_object($sqlcode))
{
	$jsonObj[] = $result;
}

$final_res = json_encode($jsonObj);

echo $final_res;

?>
