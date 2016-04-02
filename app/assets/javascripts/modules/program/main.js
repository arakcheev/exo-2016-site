define(['angular', './Routes', './controllers/main'], function (angular, Routes) {

    return angular.module("program", ['program.controllers'])
        .config(Routes);
});