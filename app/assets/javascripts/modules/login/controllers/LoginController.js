/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function LoginController(Routes, $location) {

        var view = this;


        view.$submit = function () {
            $location.url("/admin");
        };
    }

    LoginController.$inject = ['Routes', '$location'];

    return LoginController;
});