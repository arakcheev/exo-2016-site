define(['angular',
    './Routes',
    './controllers/main'], function (angular, Routes) {

    return angular.module('participants', ['participants.controllers'])
        .config(Routes);
});