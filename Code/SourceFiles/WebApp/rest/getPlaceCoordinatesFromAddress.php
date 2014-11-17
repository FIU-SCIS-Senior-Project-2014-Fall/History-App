<?php 
 
$postdata = file_get_contents("php://input");
$request = json_decode($postdata);

@$address = $request->address;

$url = 'http://maps.google.com/maps/api/geocode/json?sensor=false&address='. urlencode($address);     

$final_res = file_get_contents($url);
 
echo $final_res;

?>





