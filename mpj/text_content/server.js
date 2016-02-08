var express = require('express'); //including express in our project
var app = express();//initilize express
var fs = require("fs");//fs for reading file string
var port  	 = process.env.PORT || 9000; 	

/*
set the static files location
Public folder for users. we can put HTML/CSS/Javascript 
*/

app.use(express.static(__dirname + '/public')); 

/*
 *  Rest API Block
 *  Rest API URL : /rest-api/file-content
 *  How you can acccess this rest api from browser : Just enter http://localhost:9000/rest-api/file-content end enter..
 */


app.get('/rest-api/file-content', function (req, res) {
	var path = require('path');
	//var filePath = path.resolve(__dirname, "..", "..", "..","Desktop","DilmunData.txt");
	var filePath = path.resolve(__dirname, "DilmunData.txt");
	console.log("filepaht", filePath);
   fs.readFile(filePath,'utf8', function (err, data) {
	   res.json({result : data});//we read a static text file and return it as json format
   });
});


// where we rend html file
app.get('*', function(req, res) {
	res.sendfile('./public/index.html'); // load the single view file (angular will handle the page changes on the front-end)
});

//listen (start app with node server.js) ======================================
app.listen(port);
console.log("App listening on port " + port);
