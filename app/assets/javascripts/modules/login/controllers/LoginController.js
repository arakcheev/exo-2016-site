/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function LoginController(Routes, $log, $location) {
        var view = this;

        view.$submit = function () {
            Routes.controllers.AdminController.login().post(view.cred).then(function () {
                $location.url("/admin");
            }, function (e) {
                $log.error("Error login. ", e);
            });
        };
    }

    LoginController.$inject = ['Routes', '$log', '$location'];

    return LoginController;
});