/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function Routes($routeProvider) {
        $routeProvider
            .when('/admin', {
                redirectTo: '/admin/participants'
            })
            .when('/admin/participants', {
                templateUrl: '/assets/templates/participants/index.html',
                controller: "ParticipantsController",
                controllerAs: "participants"
            });
    }

    Routes.$inject = ['$routeProvider'];

    return Routes;
});