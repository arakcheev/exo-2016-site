/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {
    function SpeakerAndListenerController(SpeakerModel, ListenerModel) {

        var view = this;

        SpeakerModel.fetch().then(function (speakers) {
            view.speakers = speakers;
        });

        ListenerModel.fetch().then(function (listeners) {
            view.listeners = listeners;
        });

    }

    SpeakerAndListenerController.$inject = ['SpeakerModel', 'ListenerModel'];

    return SpeakerAndListenerController;
});