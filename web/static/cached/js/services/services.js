'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('localDispatch.services', []).
  value('version', '0.1');

angular.module('localDispatch.services').provider('marketingService', function() {

	var that = this;
	this.cache = {};

	this.$get = function($http) {
		return {
			signIn: function(formData, onSuccess, onError){
				$http.post( '/api/accounts/sign-in', formData).success(function(data) {
						onSuccess(data);
			        }).
			        error(function(data, status, headers, config) {
			        	console.log(data);
			        	onError(status, data);
			        });
			}
		}
	}
});