/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function Routes($routeProvider) {
        $routeProvider
            .when('/participants', {
                templateUrl: '/assets/templates/speakersAndListeners/index.html',
                controller: "SpeakerAndListenerController",
                controllerAs: "SaL"
            });
    }

    Routes.$inject = ['$routeProvider'];

    return Routes;
});