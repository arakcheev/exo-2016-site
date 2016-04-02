define(function () {

    function RegistrationForm() {

        return {
            restrict: "E",
            replace: true,
            templateUrl: "/assets/templates/registration/form.html",
            link: function (scope) {

            }
        };
    }

    return RegistrationForm;
});