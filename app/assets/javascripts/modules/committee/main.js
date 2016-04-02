define(['angular', './Routes', './controllers/main'], function (angular, Routes) {

    return angular.module("committee", ['committee.controllers'])
        .config(Routes);
});