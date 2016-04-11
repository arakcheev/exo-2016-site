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


                scope.statuses = ["Student", "Ph.D. Student", "Ph.D.", "Other"];
            }
        };
    }

    RegistrationForm.$inject = ['RegService'];

    return RegistrationForm;
});