define(['underscore'], function (_) {

    function ModalLectureController(mayBeLecture, $instance, Lectures) {
        var view = this;

        var isEdit = this.isEdit = !(_.isNull(mayBeLecture) || _.isUndefined(mayBeLecture)) && !_.isUndefined(mayBeLecture.getId());

        if (isEdit) {
            view.data = {
                date: mayBeLecture.getDate(),
                abstract: mayBeLecture.getAbstract(),
                title: mayBeLecture.getTitle(),
                organization: mayBeLecture.getOrganization(),
                speaker: mayBeLecture.getSpeaker()
            };
        } else {
            view.data = {
                date: new Date(2017, 10, 15)
            };
        }

        view.ok = function () {
            var lecture = Lectures.$apply(view.data.abstract, view.data.date, view.data.speaker, view.data.organization, view.data.title);

            if (isEdit) {
                Lectures.update(mayBeLecture.getId(), lecture).then(function (created) {
                    $instance.close(created);
                });
            } else {
                Lectures.create(lecture).then(function (created) {
                    $instance.close(created);
                });
            }
        };

        view.cancel = function () {
            $instance.dismiss("Close");
        };

    }

    ModalLectureController.$inject = ['mayBeLecture', '$uibModalInstance', 'Lectures'];

    return ModalLectureController;
});