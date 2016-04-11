/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    return function (module) {

        module.config(Routes);

        function Routes($routeProvider) {

        }

        Routes.$inject = ['$routeProvider'];

        return Routes;
    };
});