define(function () {

    function RegistrationController($scope, RegService) {

        var view = this;

        $scope.$watch(function () {
            return RegService.$getState();
        }, function (a, b) {
            view.state = a;
            if (view.state.error) {
                alert("Registration failed. Please, contact administrator.");
            }
        }, true);
    }

    RegistrationController.$inject = ['$scope', 'RegService'];

    return RegistrationController;
});