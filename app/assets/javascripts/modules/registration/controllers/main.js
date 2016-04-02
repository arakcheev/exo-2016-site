define(['angular',
        './RegistrationController'],
    function (angular,
              RegistrationController) {

        return angular.module('registration.controllers', [])
            .controller('RegistrationController', RegistrationController);
    });