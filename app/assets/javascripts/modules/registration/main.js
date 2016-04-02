define(['angular',
        './Routes',
        './controllers/main',
        './directives/main',
        './services/main'
    ],
    function (angular,
              Routes) {

        return angular.module("registration", [
                'registration.controllers', 'registration.directives', 'registration.services'])
            .config(Routes);
    });