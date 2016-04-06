/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {


    function LectureFormDateField() {

        return {
            restrict: "E",
            replace: true,
            templateUrl: "/assets/templates/lectures/date-form-field.html",
            scope: {
                form: "=",
                name: "@",
                label: "@",
                model: "="
            },
            link: function (scope, element) {

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
