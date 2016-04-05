define(function () {

    function AddNewLectureController($scope, $instance, Routes) {

        $scope.openDP = function () {
            $scope.opened = true;
        };

        $scope.format = 'dd-MMMM-yyyy';

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.data = {
            date: new Date(2016, 4, 30)
        };

        $scope.ok = function () {
            $scope.data.date = $scope.data.date.getTime();
            $scope.data.abst = $scope.data.abstract;

            Routes.controllers.AdminController.newLecture()
                .post($scope.data)
                .then(function (response) {
                    var data = response.data;
                    $instance.close(data);
                })
                .catch(function () {
                    console.error("Not saved");
                });
        };

        $scope.cancel = function () {
            $instance.dismiss("Close");
        };

    }

    AddNewLectureController.$inject = ['$scope', '$uibModalInstance', 'Routes'];

    return AddNewLectureController;
});