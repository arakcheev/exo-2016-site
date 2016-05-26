/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular',
    './Routes',
    "./Speaker",
    "./Listeners",
    "./SpeakerAndListenerController"
], function (angular, Routes, SpeakerModel, ListenerModel, SpeakerAndListenerController) {

    return angular.module("speakersAndListeners", [])
        .config(Routes)
        .factory("SpeakerModel", SpeakerModel)
        .factory("ListenerModel", ListenerModel)
        .controller("SpeakerAndListenerController", SpeakerAndListenerController);
});