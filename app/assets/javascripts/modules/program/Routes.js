define(function () {

    function Routes($routeProvider) {
        $routeProvider
            .when('/program', {
                templateUrl: '/assets/templates/program/index.html',
                controller: "ProgramController",
                controllerAs: "program"
            })
            .when('/program/workshop', {
            templateUrl: '/assets/templates/program/index.html',
            controller: "ProgramController",
            controllerAs: "program"
        });
    }

    Routes.$inject = ['$routeProvider'];

    return Routes;
});