/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular', './Lecture'], function (angular, LectureModel) {


    return angular.module('lectures.models', [])
        .service('Lectures', LectureModel);
});
