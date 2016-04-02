define(['angular',
        './ContactsController'],
    function (angular,
              ContactsController) {

        return angular.module('contacts.controllers', [])
            .controller('ContactsController', ContactsController);
    });