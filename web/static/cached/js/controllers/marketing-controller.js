'use strict';

/* Controllers */

angular.module('localDispatch.controllers', []).
  controller('SignInController', ['$scope', '$http', '$routeParams', '$route', 'marketingService', function($scope, $http, $routeParams, $route, marketingService){
	  $scope.signIn = function(){
	    marketingService.signIn({"emailAddress": $scope.emailAddress, "password": $scope.password, "rememberMe": false}, function(results){
	    	alert("signed in");
	    }, 
	    function(status, data){
	    	$scope.error = data.description;
	    });	    	    
	  }
	  
	 
  }])
  .controller('HomeController', [function() {

  }]);