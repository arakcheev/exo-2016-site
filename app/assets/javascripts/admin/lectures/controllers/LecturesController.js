define(['underscore', './AddNewLectureController'], function (_, AddNewLectureController) {

    function LecturesController($modal, Lectures) {
        var view = this;

        view.lectures = [];

        view.predicate = function (lecture) {
            return lecture.getDate().getTime();
        };

        function LectureModal(mayBeLecture, callback) {
            $modal.open({
                templateUrl: '/assets/templates/lectures/add-modal.html',
                controller: AddNewLectureController,
                controllerAs: 'modal',
                resolve: {
                    mayBeLecture: mayBeLecture
                },
                size: "lg"
            }).result.then(function (newLecture) {
                callback.call(this, newLecture);
            });
        }

        view.addNew = function () {
            new LectureModal(null, function (lecture) {
                view.lectures.push(lecture);
            });
        };

        view.$edit = function (lecture) {
            new LectureModal(lecture, function (updatedLecture) {
                var index = _.indexOf(view.lectures, lecture);
                if (index > -1) {
                    view.lectures[index] = updatedLecture;
                }
            });
        };

        view.$remove = function (lecture) {
            Lectures.remove(lecture).then(function () {
                var index = _.indexOf(view.lectures, lecture);
                if (index > -1) {
                    view.lectures.splice(index, 1);
                }
            });
        };

        Lectures.fetch().then(function (lectures) {
            _.each(lectures, function (item) {
                view.lectures.push(item);
            });
        });


    }

    LecturesController.$inject = ['$uibModal', 'Lectures'];

    return LecturesController;
});