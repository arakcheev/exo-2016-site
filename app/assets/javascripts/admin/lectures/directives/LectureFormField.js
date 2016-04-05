/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function LectureFormField() {

        return {
            restrict: "E",
            replace: true,
            templateUrl: "/assets/templates/lectures/form-filed.html",
            scope: {
                form: "=",
                name: "@",
                label: "@",
                placeholder: "@",
                model: "=",
                requiredError: "@"
            },
            link: function (scope) {

            }
        };
    }

    return LectureFormField;
});
