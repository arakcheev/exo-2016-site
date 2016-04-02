define(['angular',
        './ProgramController'],
    function (angular,
              ProgramController) {

        return angular.module('program.controllers', [])
            .controller('ProgramController', ProgramController);
    });