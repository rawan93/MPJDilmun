var express = require('express'); //including express in our project
var app = express();//initilize express
var fs = require("fs");//fs for reading file string
var port  	 = process.env.PORT || 9000; 	

/*
set the static files location
which is now in our folder (same directory)
*/

app.use(express.static(__dirname + '/public')); 

/*
    Rest API 
    Rest API URL : /rest-api/file-content
   How you can acccess this rest api from browser :enter http://localhost:9000/rest-api/file-content ..
 */

app.get('/rest-api/file-content', function (req, res) {
var n = ["CPUinfo.txt","DilmunData.txt","test.txt"];
var str = "";
for(var k=0; k<n.length; k++) {
	var d = fs.readFileSync( __dirname + "/" + n[k], 'utf8');
	str+=d;
	if(k+1 < n.length){
		str+=".";
	}
}

res.json({result : str});

});

// where we rend html file
app.get('*', function(req, res) {
	res.sendfile('./public/index.html'); 
});

//listen (start app with node server.js) 
app.listen(port);
console.log("App listening on port " + port);