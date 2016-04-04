/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function ParticipantsController(Routes) {

        var view = this;

        Routes.controllers.AdminController.listParticipants().get().then(function (response) {
            view.participants = response.data;
            console.log(view.participants);
        }, function (e) {

        });
    }

    ParticipantsController.$inject = ['Routes'];

    return ParticipantsController;
});
