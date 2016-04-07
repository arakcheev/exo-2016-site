/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    return function (module) {

        module.config(Routes);

        function Routes($routeProvider) {
            $routeProvider
                .when('/map', {
                    templateUrl: '/assets/templates/map/index.html',
                    controller: "MapController",
                    controllerAs: "mapctrl"
                });
        }

        Routes.$inject = ['$routeProvider'];

        return Routes;
    };
});