/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function Routes($routeProvider) {
        $routeProvider
            .when('/login', {
                templateUrl: '/assets/templates/login.html',
                controller: 'LoginController',
                controllerAs: "login"
            });
    }

    Routes.$inject = ['$routeProvider'];

    return Routes;
});