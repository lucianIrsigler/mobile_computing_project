<?php
function mapTypeToStmtBind($var){
    $map_type_to_bind = array("double"=>"d","integer"=>"i","string"=>"s");
    return $map_type_to_bind[gettype($var)];
}
