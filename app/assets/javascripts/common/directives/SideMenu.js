define(function () {

    function SideMenu() {

        return {
            restrict: "E",
            replace: true,
            templateUrl: "/assets/templates/sideMenu/main.html",
            link: function (scope, element) {

            }
        };
    }

    return SideMenu;
});