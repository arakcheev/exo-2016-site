define(['angular', 'modules', 'common'], function (angular) {

    var dependencies = [
        "ui.bootstrap",
        "ngCookies",
        "ngAnimate",
        "ngMessages",
        "ngRoute",
        "common",
        "modules"
    ];


    // We must already declare most dependencies here (except for common), or the submodules' routes
    // will not be resolved
    var app = angular.module('app', dependencies);

    app
        .config(['$locationProvider', function ($locationProvider) {
            $locationProvider.html5Mode(true);
        }])
        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider
                .when('/notFound', {templateUrl: '/assets/templates/notFound.html'})
                .otherwise({redirectTo: "/notFound"});
        }])
        .config(['$logProvider', function ($logProvider) {
            $logProvider.debugEnabled(true);
        }]);

    return app;

});