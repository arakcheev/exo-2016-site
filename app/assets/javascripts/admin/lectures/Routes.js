/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function Routes($routeProvider) {
        $routeProvider
            .when('/admin/lectures', {
                templateUrl: '/assets/templates/lectures/index.html',
                controller: "LecturesController",
                controllerAs: "lectures"
            });
    }

    Routes.$inject = ['$routeProvider'];

    return Routes;
});