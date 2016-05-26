/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(["underscore"], function (_) {

    function Speaker(name, organization) {

        this.getName = function () {
            return name;
        };

        this.getOrganization = function () {
            return organization;
        };
    }

    var fromJson = function (json) {
        if (_.isArray(json)) {
            return _.map(json, function (item) {
                return fromJson(item);
            });
        } else {
            return new Speaker(json.name, json.organization);
        }
    };

    function SpeakerModel(Routes) {

        Speaker.fetch = function () {
            return Routes.controllers.SpeakersAndListenersController.speakers().get()
                .then(function (response) {
                    var data = response.data;
                    return fromJson(data);
                })
                .catch(function (error) {
                    //error here
                    return [];
                });
        };

        return Speaker;
    }

    SpeakerModel.$inject = ['Routes'];

    return SpeakerModel;

});
