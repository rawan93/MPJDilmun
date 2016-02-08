var scotchTodo = angular.module('scotchTodo', []);

function mainController($scope, $http ,$sce) {
		$http.get('/rest-api/file-content')// calling our ret api using get method and whenever rest api
		//it return success we call our result data.result
		.success(function(data) {
		//	$scope.mydata = data.result;
			$scope.mydata   = data.result;	
			});
		
		
		
		
	
}
