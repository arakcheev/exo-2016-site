define(['./AddNewLectureController'], function (AddNewLectureController) {

    function LecturesController($modal) {
        var view = this;

        view.addNew = function () {
            $modal.open({
                templateUrl: '/assets/templates/lectures/add-modal.html',
                controller: AddNewLectureController,
                size: "lg"
            });
        };
    }

    LecturesController.$inject = ['$uibModal'];

    return LecturesController;
});