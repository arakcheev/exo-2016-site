define(['angular',
    './Routes',
    './controllers/main'], function (angular, Routes) {

    return angular.module('index', ['index.controllers'])
        .config(Routes);
});