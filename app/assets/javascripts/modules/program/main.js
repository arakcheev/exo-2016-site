define(['angular', './Routes', './controllers/main', './directives/main'], function (angular, Routes) {

    return angular.module("program", ['program.controllers', 'program.directives'])
        .config(Routes);
});