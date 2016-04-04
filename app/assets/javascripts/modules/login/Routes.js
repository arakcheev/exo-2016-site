/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    //Redirect user to admin page if user already was logged in.
    function AlreadyLoggedResolver(R, $location) {
        R.controllers.AdminController.isLogged().get().then(function () {
            $location.url("/admin");
        });
    }

    AlreadyLoggedResolver.$inject = ['Routes', '$location'];

    function Routes($routeProvider) {
        $routeProvider
            .when('/login', {
                templateUrl: '/assets/templates/login.html',
                controller: 'LoginController',
                controllerAs: "login",
                resolve: {
                    data: AlreadyLoggedResolver
                }
            });
    }

    Routes.$inject = ['$routeProvider'];

    return Routes;
});