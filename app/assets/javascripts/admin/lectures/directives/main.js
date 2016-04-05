/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular', './LectureFormDateField'], function (angular, LectureFormDateField) {


    return angular.module('lectures.directives', [])
        .directive('lectureDateField', LectureFormDateField);
});
