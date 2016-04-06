/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular'], function (angular) {

    function Header($location) {

        return {
            restrict: "E",
            replace: true,
            templateUrl: "/assets/templates/admin-header.html",
            link: function (scope, elem) {

                scope.$on("$routeChangeSuccess", function (a, route) {
                    try {
                        var path = route.$$route.originalPath;
                        var menuItem = scope.menu[path];
                        if (menuItem !== undefined) {
                            document.title = menuItem + " | Exo-2016 Admin";
                        } else {
                            document.title = "Welcome to Exo-2016";
                        }
                    } catch (e) {
                        console.error("Error update page title. ", e);
                    }
                });

                scope.menu = {
                    "/admin/participants": "Participants",
                    "/admin/lectures": "Lectures",
                    "/admin/workshop": "Workshop"
                };

                scope.$active = function (url) {
                    return $location.path().indexOf(url) >= 0;
                };

                scope.$go = function (url) {
                    $location.url(url);
                    $('.in').collapse('hide');
                };
            }
        };
    }

    Header.$inject = ['$location'];

    return Header;
});