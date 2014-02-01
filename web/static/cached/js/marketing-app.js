'use strict';


// Declare app level module which depends on filters, and services
angular.module('localDispatch', [
  'ngRoute',
  'localDispatch.filters',
  'localDispatch.services',
  'localDispatch.directives',
  'localDispatch.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/signin', {templateUrl: stack.getCachedUrl('static/cached/partials/signin.html'), controller: 'SignInController'});
  $routeProvider.when('/home', {templateUrl: stack.getCachedUrl('static/cached/partials/home.html'), controller: 'HomeController'});
  $routeProvider.otherwise({redirectTo: '/home'});
}]);
