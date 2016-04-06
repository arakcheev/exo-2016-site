/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular', './ProgramListItem'], function (angular, ProgramListItem) {

    return angular.module('program.directives', [])
        .directive("programListItem", ProgramListItem);
});
