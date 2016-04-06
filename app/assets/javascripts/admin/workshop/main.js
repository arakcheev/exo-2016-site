/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular',
    './Routes',
    './controllers/main',
    './models/main'
], function (angular, Routes) {

    return angular.module('workshop', ['workshop.models', 'workshop.controllers'])
        .config(Routes);
});