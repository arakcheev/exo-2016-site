/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular',
        './LoginController'],
    function (angular,
              LoginController) {

        return angular.module('login.controllers', [])
            .controller('LoginController', LoginController);
    });