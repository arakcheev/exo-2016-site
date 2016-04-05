/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular',
    './LectureFormDateField',
    './LectureFormField'],
    function (angular, LectureFormDateField, LectureFormField) {


    return angular.module('lectures.directives', [])
        .directive('lectureDateField', LectureFormDateField)
        .directive('lectureFormField', LectureFormField);
});
