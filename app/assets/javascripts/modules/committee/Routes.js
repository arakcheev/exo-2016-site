define(function () {

    function Routes($routeProvider) {
        $routeProvider
            .when('/committee', {
                templateUrl: '/assets/templates/committee/index.html',
                controller: "CommitteeController",
                controllerAs: "committee"
            });
    }

    Routes.$inject = ['$routeProvider'];

    return Routes;
});