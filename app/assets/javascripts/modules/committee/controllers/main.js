define(['angular',
        './CommitteeController'],
    function (angular,
              CommitteeController) {

        return angular.module('committee.controllers', [])
            .controller('CommitteeController', CommitteeController);
    });