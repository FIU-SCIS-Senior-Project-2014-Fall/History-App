
<?php 

include('connectdb.php');
 
$postdata = file_get_contents("php://input");
$request = json_decode($postdata);

@$currentPage = $request->currentPage; 
  
$sqlcode = mysql_query("SELECT * FROM `Help` WHERE Page = '$currentPage'");

$jsonObj = array();

while($result = mysql_fetch_object($sqlcode))
{
	$jsonObj[] = $result;
}

$final_res = json_encode($jsonObj);

echo $final_res;

?>

