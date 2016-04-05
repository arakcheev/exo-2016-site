/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular',
    './Routes',
    './controllers/main',
    './directives/main',
    './models/main'], function (angular, Routes) {

    return angular.module('lectures', ['lectures.controllers', 'lectures.directives', 'lectures.models'])
        .config(Routes);
});