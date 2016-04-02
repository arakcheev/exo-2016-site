define(function () {

    function PhoneAsyncValidator(Routes, $q) {

        return {
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {

                ctrl.$asyncValidators.phoneAvailable = function (modelValue, viewValue) {
                    var value = modelValue || viewValue;

                    if (ctrl.$dirty) {
                        return Routes.controllers.RegistrationController.phoneAvailable().post({phone: value});
                    } else {
                        return $q(function (resolve, reject) {
                            resolve();
                        });
                    }
                };
            }
        };
    }

    PhoneAsyncValidator.$inject = ['Routes', '$q'];

    return PhoneAsyncValidator;

});