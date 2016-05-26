define(['angular',
    './index/main',
    './committee/main',
    './contacts/main',
    './program/main',
    './registration/main',
    './login/main',
    './map/main',
    './speakersAndListeners/main'], function (angular) {

    return angular.module("modules", ['index', 'committee', 'contacts', 'program', 'registration', 'login', 'map', 'speakersAndListeners']);
});