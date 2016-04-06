define(['underscore', './ModalLectureController'], function (_, ModalLectureController) {

    function LecturesController($modal, Lectures) {
        var view = this;

        view.items = [];

        view.predicate = function (lecture) {
            return lecture.getDate().getTime();
        };

        function LectureModal(mayBeLecture, callback) {
            $modal.open({
                templateUrl: '/assets/templates/lectures/add-modal.html',
                controller: ModalLectureController,
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
                view.items.push(lecture);
            });
        };

        view.$edit = function (lecture) {
            new LectureModal(lecture, function (updatedLecture) {
                var index = _.indexOf(view.items, lecture);
                if (index > -1) {
                    view.items[index] = updatedLecture;
                }
            });
        };

        view.$remove = function (lecture) {
            Lectures.remove(lecture).then(function () {
                var index = _.indexOf(view.items, lecture);
                if (index > -1) {
                    view.items.splice(index, 1);
                }
            });
        };

        Lectures.fetch().then(function (lectures) {
            _.each(items, function (item) {
                view.items.push(item);
            });
        });


    }

    LecturesController.$inject = ['$uibModal', 'Lectures'];

    return LecturesController;
});