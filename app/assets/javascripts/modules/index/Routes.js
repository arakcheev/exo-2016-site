define(function () {

    function Routes($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: '/assets/templates/index/index.html',
                controller: "IndexController",
                controllerAs: "index"
            });
    }

    Routes.$inject = ['$routeProvider'];

    return Routes;
});