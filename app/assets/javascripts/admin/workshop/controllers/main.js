/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular',
        './WorkShopController'],
    function (angular,
              WorkShopController) {

        return angular.module('workshop.controllers', [])
            .controller('WorkShopController', WorkShopController);
    });