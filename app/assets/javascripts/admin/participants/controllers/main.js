/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['angular',
        './ParticipantsController'],
    function (angular,
              ParticipantsController) {

        return angular.module('participants.controllers', [])
            .controller('ParticipantsController', ParticipantsController);
    });