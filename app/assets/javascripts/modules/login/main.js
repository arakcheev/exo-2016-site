/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular', './Routes', './controllers/main'], function (angular, Routes) {

    return angular.module("login", ['login.controllers'])
        .config(Routes);
});