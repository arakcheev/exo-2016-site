define(function () {

    function AddNewLectureController($scope, $instance, Routes) {

        $scope.openDP = function () {
            $scope.opened = true;
        };

        $scope.format = 'dd-MMMM-yyyy';

        $scope.date = new Date(2016, 4, 30);

        $scope.dateOptions = {
            formatYear: 'yy',
            //maxDate: new Date(2016, 5, 1),
            //minDate: new Date(2016, 5, 30),
            startingDay: 1
        };

        $scope.ok = function () {

        };

        $scope.cancel = function () {
            $instance.close("");
        };

    }

    AddNewLectureController.$inject = ['$scope', '$uibModalInstance', 'Routes'];

    return AddNewLectureController;
});