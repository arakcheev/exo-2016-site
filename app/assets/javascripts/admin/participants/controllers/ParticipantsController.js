/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['underscore', './EditParticipantModal'], function (_, EditParticipantModal) {

    function ParticipantsController(ParticipantModel, $modal, RemoveModal) {

        var view = this;

        ParticipantModel.fetch().then(function (participants) {
            view.participants = participants;
        });

        function ParticipantModal(participant, callback) {
            $modal.open({
                templateUrl: '/assets/templates/participants/edit-modal.html',
                controller: EditParticipantModal,
                controllerAs: 'modal',
                keyboard: false,
                backdrop: 'static',
                resolve: {
                    participant: participant
                },
                size: "lg"
            }).result.then(function (newParticipant) {
                callback.call(this, newParticipant);
            });
        }


        view.$edit = function (participant) {
            new ParticipantModal(participant, function (updatedParticipant) {
                var index = _.indexOf(view.participants, participant);
                if (index > -1) {
                    view.participants[index] = updatedParticipant;
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
