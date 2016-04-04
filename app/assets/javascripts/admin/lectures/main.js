/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular',
    './Routes',
    './controllers/main'], function (angular, Routes) {

    return angular.module('lectures', ['lectures.controllers'])
        .config(Routes);
});