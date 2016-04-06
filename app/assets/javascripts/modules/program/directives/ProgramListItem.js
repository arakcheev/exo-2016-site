/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {


    function ProgramListItem() {

        return {
            restrict: "E",
            replace: true,
            templateUrl: "/assets/templates/program/list-item.html",
            scope: {
                days: "="
            }
        };
    }

    return ProgramListItem;
});
