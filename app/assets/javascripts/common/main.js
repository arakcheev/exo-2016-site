define(['angular',
        './directives/main',
        './services/Routes'
    ],
    function (angular) {

        return angular.module('common', [
            "common.directives",
            'common.playRoutes'
        ]);
    });