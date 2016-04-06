/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function Routes($routeProvider) {
        $routeProvider
            .when('/admin/workshop', {
                templateUrl: '/assets/templates/workshop/index.html',
                controller: "WorkShopController",
                controllerAs: "workshop"
            });
    }

    Routes.$inject = ['$routeProvider'];

    return Routes;
});