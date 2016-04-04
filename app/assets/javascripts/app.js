define(['angular', 'templates', 'modules', 'common', 'admin'], function (angular, templates) {

    var dependencies = [
        "ui.bootstrap",
        "ngCookies",
        "ngAnimate",
        "ngMessages",
        "ngRoute",
        "common",
        "modules",
        "admin"
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


    app.factory('authHttpResponseInterceptor', ['$q', '$location', function ($q, $location) {
        return {
            request: function (config) {

                var url = config.url;

                if (templates[url]) {
                    config.url = templates[url];
                }

                return config;
            },
            responseError: function (rejection) {
                if (rejection.status === 401) {
                    $location.url('/login');
                }
                return $q.reject(rejection);
            }
        };
    }]);

    app.config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push('authHttpResponseInterceptor');
    }]);

    return app;

});