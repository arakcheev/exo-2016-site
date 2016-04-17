/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define([
        'underscore',
        'angular',
        './Routes',
        './controllers/MapController'
    ],
    function (_, angular) {
        var module = angular.module('map', []);

        var components = _.rest(arguments, 2);

        _.each(components, function (component) {
            if (_.isFunction(component)) {
                component.call(this, module);
            } else {
                console.error("Component if not a function");
            }
        });
    });