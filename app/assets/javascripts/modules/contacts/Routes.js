define(function () {

    function Routes($routeProvider) {
        $routeProvider
            .when('/contacts', {
                templateUrl: '/assets/templates/contacts/index.html',
                controller: "ContactsController",
                controllerAs: "contacts"
            });
    }

    Routes.$inject = ['$routeProvider'];

    return Routes;
});