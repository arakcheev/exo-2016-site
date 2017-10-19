/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular'], function (ng) {


    function LectureFormDateField() {

        return {
            restrict: "E",
            replace: true,
            templateUrl: "/assets/templates/lectures/date-form-field.html",
            scope: {
                form: "=",
                name: "@",
                label: "@",
                model: "=",
                rq: "="
            },
            link: function (scope, element) {

                scope.require = function () {
                    if(ng.isDefined(scope.rq)){
                        return scope.rq;
                    } else {
                        return true;
                    }
                };

                //datepicker closed by default
                scope.opened = false;

                scope.openDP = function () {
                    scope.opened = true;
                };

                scope.format = 'dd-MMMM-yyyy';

                scope.dateOptions = {
                    formatYear: 'yy',
                    startingDay: 1
                };
            }
        };
    }

    return LectureFormDateField;
});
