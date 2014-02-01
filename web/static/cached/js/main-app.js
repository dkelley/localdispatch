<script type="text/javascript">
'use strict';


//Declare app level module which depends on filters, and services
angular.module('myApp', [
'ngRoute',
'myApp.filters',
'myApp.services',
'myApp.directives',
'myApp.controllers',
'leaflet-directive'
]).
config(['$routeProvider', function($routeProvider) {
$routeProvider.when('/view1', {templateUrl: stack.getCachedUrl('static/cached/partials/partial1.html'), controller: 'MyCtrl1'});
$routeProvider.when('/view2', {templateUrl: stack.getCachedUrl('static/cached/partials/partial2.html'), controller: 'MyCtrl2'});
$routeProvider.otherwise({redirectTo: '/view1'});
}]);


angular.extend($scope, {
    defaults: {
        tileLayer: "http://{s}.tile.opencyclemap.org/cycle/{z}/{x}/{y}.png",
        maxZoom: 14,
        path: {
            weight: 10,
            color: '#800000',
            opacity: 1
        }
    }
});

angular.extend($scope, {
    center: {
        lat: 40.8471,
        lng: 14.0625,
        zoom: 2
    },
    legend: {
        colors: [ '#CC0066', '#006699', '#FF0000', '#00CC00', '#FFCC00' ],
        labels: [ 'Oceania', 'America', 'Europe', 'Africa', 'Asia' ]
    }
});

</script>