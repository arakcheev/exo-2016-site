define(['underscore','./AddNewLectureController'], function (_, AddNewLectureController) {

    function LecturesController($modal, Routes) {
        var view = this;

        view.lectures = [];

        view.addNew = function () {
            $modal.open({
                templateUrl: '/assets/templates/lectures/add-modal.html',
                controller: AddNewLectureController,
                size: "lg"
            }).result.then(function (newLecture) {
                view.lectures.push(newLecture);
            });
        };

        Routes.controllers.AdminController.listLectures().get()
            .then(function (response) {
                var data = response.data;
                _.each(data, function(item){
                    view.lectures.push(item);
                });
            })
            .catch(function () {
                //error here
            });


    }

    LecturesController.$inject = ['$uibModal', 'Routes'];

    return LecturesController;
});