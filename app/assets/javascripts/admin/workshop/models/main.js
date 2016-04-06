/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular', './Items'], function (angular, ItemsModel) {


    return angular.module('workshop.models', [])
        .service('Items', ItemsModel);
});
