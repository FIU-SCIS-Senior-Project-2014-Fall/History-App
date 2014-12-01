<?php
    
    $hostname="localhost";
    $username="root"; //write your username
    $password="historyapp"; //write your password
    $db_name="HistoryApp"; //write your db name
    $con=mysql_connect($hostname,$username,$password);
    mysql_select_db($db_name,$con) or die ("Cannot connect the Database");
    mysql_query("SET NAMES 'utf8'",$con); 
 
?>
