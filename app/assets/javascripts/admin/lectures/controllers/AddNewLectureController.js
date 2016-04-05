define(function () {

    function AddNewLectureController($scope, $instance, Routes) {

        $scope.openDP = function () {
            $scope.opened = true;
        };

        $scope.format = 'dd-MMMM-yyyy';

        $scope.date = new Date(2016, 4, 30);

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.ok = function () {
            console.log("aololo");
        };

        $scope.cancel = function () {
            $instance.close("");
        };

    }

    AddNewLectureController.$inject = ['$scope', '$uibModalInstance', 'Routes'];

    return AddNewLectureController;
});