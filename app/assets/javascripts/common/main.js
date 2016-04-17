define(['angular',
        './services/RemoveModal',
        './directives/main',
        './services/Routes'
    ],
    function (angular, RemoveModal) {

        return angular.module('common', [
            "common.directives",
            'common.playRoutes'
        ]).service('RemoveModal', RemoveModal);
    });