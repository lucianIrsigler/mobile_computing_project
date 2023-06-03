<?php
//https://github.com/PHPMailer/PHPMailer
require '/home/s2621933/public_html/php/PHPMailer/src/PHPMailer.php';
require '/home/s2621933/public_html/php/PHPMailer/src/SMTP.php';
require '/home/s2621933/public_html/php/PHPMailer/src/Exception.php';

use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;
// Create a new PHPMailer instance
$mail = new PHPMailer();

$mail->SMTPDebug = SMTP::DEBUG_SERVER; // Set debugging level
$mail->Debugoutput = function ($str, $level) { echo "$level: $str\n"; }; // Output debug messages


// SMTP configuration
$mail->isSMTP();
$mail->Host = 'smtp.gmail.com';
$mail->Port = 587;
$mail->SMTPAuth = true;
$mail->Username = 'l.irsigler64@gmail.com';
$mail->Password = 'hguwqreizfixkyzn'; // this is an application specific password.

/** 
 * 
*/

// Email content
$mail->setFrom('l.irsigler64@gmail.com', 'Test');
$mail->addAddress('lucianirsigler@gmail.com', 'Tester');
$mail->Subject = 'Email Subject';
$mail->Body = 'Email content goes here.';

error_reporting(E_ALL);
ini_set('display_errors', 1);
// Send the email
if ($mail->send()) {
    echo 'Email sent successfully!';
} else {
    echo 'Failed to send email.';
    echo 'Error: ' . $mail->ErrorInfo;
}

echo "lives here";