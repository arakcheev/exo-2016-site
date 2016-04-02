define(function () {

    function RegistrationForm(RegService) {

        return {
            restrict: "E",
            replace: true,
            templateUrl: "/assets/templates/registration/form.html",
            link: function (scope) {
                scope.$submit = function () {
                    scope.loading = true;
                    RegService.register(scope.reg);
                };
            }
        };
    }

    RegistrationForm.$inject = ['RegService'];

    return RegistrationForm;
});