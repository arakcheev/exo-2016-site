define(['angular',
        './RegistrationForm'],
    function (angular,
              RegistrationForm) {

        return angular.module("registration.directives", [])
            .directive("registrationForm", RegistrationForm);
    });