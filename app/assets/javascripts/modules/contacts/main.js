define(['angular', './Routes', './controllers/main'], function (angular, Routes) {

    return angular.module("contacts", ['contacts.controllers'])
        .config(Routes);
});