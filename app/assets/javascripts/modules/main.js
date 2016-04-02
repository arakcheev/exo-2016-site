define(['angular',
    './index/main',
    './committee/main',
    './contacts/main',
    './program/main',
    './registration/main'], function (angular) {

    return angular.module("modules", ['index', 'committee', 'contacts', 'program', 'registration']);
});