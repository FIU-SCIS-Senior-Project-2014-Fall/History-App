
<?php 

include('connectdb.php');
 
$postdata = file_get_contents("php://input");
$request = json_decode($postdata);

@$name 			= $request->name;
@$address 		= $request->address;
@$email 		= $request->email;
@$phone  		= $request->phone; 
@$website  		= $request->website; 
@$description  	= $request->description; 
@$audioPaths  	= $request->audioPaths; 
@$imagePaths  	= $request->imagePaths; 
@$documentPaths = $request->documentPaths; 
 
$sqlcode = "INSERT INTO `Places`(`Name`, `Address`, `Email`, `Phone`, `Website`, `Description`, `AudioPaths`, `ImagePaths`, `DocumentPaths`) 
			VALUES ('$name','$address','$email','$phone', '$website','$description','$audioPaths','$imagePaths','$documentPaths')";


$insert = mysql_query($sqlcode, $con);

if(! $insert)
{
	die('Could not enter data: ' . mysql_error());
}

echo "Entered data successfully\n";

mysql_close($con);

?>
