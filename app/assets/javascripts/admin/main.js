/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular',
    './participants/main',
    './lectures/main'
], function (angular) {

    return angular.module("admin", ['participants', 'lectures']);
});