/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['underscore'], function (_) {

    function ParticipantsController(ParticipantModel, $modal, RemoveModal) {

        var view = this;

        ParticipantModel.fetch().then(function (participants) {
            view.participants = participants;
        });

        function ParticipantModal(participant, callback) {
            $modal.open({
                templateUrl: '/assets/templates/participants/edit-modal.html',
                controller: ModalLectureController,
                controllerAs: 'modal',
                resolve: {
                    participant: participant
                },
                size: "lg"
            }).result.then(function (participant) {
                callback.call(this, participant);
            });
        }


        view.$edit = function (lecture) {
            new ParticipantModal(lecture, function (updatedLecture) {
                var index = _.indexOf(view.lectures, lecture);
                if (index > -1) {
                    view.lectures[index] = updatedLecture;
                }
            });
        };

        view.$remove = function (participant) {
            var name = participant.getName() + " " + participant.getSurname();
            var callback = function () {
                ParticipantModel.remove(participant).then(function () {
                    var index = _.indexOf(view.participants, participant);
                    if (index > -1) {
                        view.participants.splice(index, 1);
                    }
                });
            };
            RemoveModal(name, callback);
        };
    }

    ParticipantsController.$inject = ['ParticipantModel', '$uibModal', 'RemoveModal'];

    return ParticipantsController;
});
