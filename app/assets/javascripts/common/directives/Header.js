define(['angular'], function (angular) {

    function Header($location) {

        return {
            restrict: "E",
            replace: true,
            templateUrl: "/assets/templates/header.html",
            link: function (scope, elem) {

                scope.menu = {
                    "/program": "Program",
                    "/committee": "Committee",
                    "/registration": "Registration",
                    "/participants": "Participants",
                    "/contacts": "Contacts"
                };

                try {
                    var path = $location.path();
                    var menuItem = scope.menu[path];
                    if (menuItem !== undefined) {
                        document.title = menuItem + " | Exo-2017";
                    } else {
                        document.title = "Welcome to Exo-2017";
                    }
                } catch (e) {
                    console.error("Error update page title. ", e);
                }

                scope.$active = function (url) {
                    return $location.path().indexOf(url) >= 0;
                };

                scope.$go = function (url) {
                    $location.url(url);
                    $('.in').collapse('hide');
                    $('body').scrollTop(0);
                };
            }
        };
    }

    Header.$inject = ['$location'];

    return Header;
});