var scotchTodo = angular.module('scotchTodo', []);

function mainController($scope, $http ,$sce) {
		$http.get('/rest-api/file-content')// calling our ret api using get method and whenever rest api
		//it return success we call our result data.result
		.success(function(data) {
			var text_content = (data.result).split('.');
			var contentArea = [];
			var classes = ['myName', 'myName2', 'myName', 'myName2'];
			var n;
			for(var i=0; i< text_content.length; i++) {
				n = 0;
				if(n == (text_content.length - 1)) {
					n = 0;
				}
				contentArea.push({ text : text_content[i], value : classes[n] });
				n++;
			}
			$scope.mydata   = contentArea;	
		});
		
}
