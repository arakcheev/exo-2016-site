define(function () {

    function EmailAsyncValidator(Routes, $q) {

        return {
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {

                ctrl.$asyncValidators.emailAvailable = function (modelValue, viewValue) {
                    var value = modelValue || viewValue;

                    console.log(Routes);

                    if (ctrl.$dirty) {
                        return Routes.controllers.RegistrationController.emailAvailable().post({email: value});
                    } else {
                        return $q(function (resolve, reject) {
                            resolve();
                        });
                    }
                };
            }
        };
    }

    EmailAsyncValidator.$inject = ['Routes', '$q'];

    return EmailAsyncValidator;

});