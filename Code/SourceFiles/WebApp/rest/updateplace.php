
<?php 

include('connectdb.php');
 
$postdata = file_get_contents("php://input");
$request = json_decode($postdata);

@$id 			= $request->id;
@$name 			= $request->name;
@$address 		= $request->address;
@$email 		= $request->email;
@$phone  		= $request->phone; 
@$website  		= $request->website; 
@$description  	= $request->description; 
@$audioPaths  	= $request->audioPaths; 
@$imagePaths  	= $request->imagePaths; 
@$documentPaths = $request->phone; 
  
$sqlcode = "UPDATE Places 
			SET Name='$name', Address='$address', Email='$email',
			Phone='$phone', Website='$website', Description='$description',
			AudioPaths='$audioPaths', ImagePaths='$imagePaths',DocumentPaths='$documentPaths' 
			WHERE PlaceId = '$id'";


$insert = mysql_query($sqlcode, $con);

if(! $insert)
{
	die('Could not enter data: ' . mysql_error());
}

echo "Updated data successfully\n";

mysql_close($con);

?>

