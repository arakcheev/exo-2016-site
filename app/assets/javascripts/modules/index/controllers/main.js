define(['angular',
        './IndexController'],
    function (angular,
              IndexController) {

        return angular.module('index.controllers', [])
            .controller('IndexController', IndexController);
    });