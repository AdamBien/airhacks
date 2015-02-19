#!/usr/bin/jjs -fv

var command = "ls -al";
$EXEC(command);
var output = $OUT;

print("Here is the ${output}");
