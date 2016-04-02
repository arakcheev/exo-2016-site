define(['angular', './Routes', './controllers/main'], function (angular, Routes) {

    return angular.module("registration", ['registration.controllers'])
        .config(Routes);
});