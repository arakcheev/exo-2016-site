define(['angular',
        './Routes',
        './controllers/main',
        './directives/main'
    ],
    function (angular,
              Routes) {

        return angular.module("registration", [
                'registration.controllers', 'registration.directives'])
            .config(Routes);
    });