/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular', './RegService'], function (angular, RegService) {

    return angular.module('registration.services', [])
        .factory("RegService", RegService);
});

