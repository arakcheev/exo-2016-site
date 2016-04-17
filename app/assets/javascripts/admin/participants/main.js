define(['angular',
    './Routes',
    './Participant',
    './controllers/main'
], function (angular, Routes, ParticipantModel) {

    return angular.module('participants', ['participants.controllers'])
        .config(Routes).factory('ParticipantModel', ParticipantModel);
});