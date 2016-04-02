define(function () {

    function Routes($routeProvider) {
        $routeProvider
            .when('/registration', {
                templateUrl: '/assets/templates/registration/index.html',
                controller: "RegistrationController",
                controllerAs: "registration"
            });
    }

    Routes.$inject = ['$routeProvider'];

    return Routes;
});