/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular',
        './LecturesController'],
    function (angular,
              LecturesController) {

        return angular.module('lectures.controllers', [])
            .controller('LecturesController', LecturesController);
    });