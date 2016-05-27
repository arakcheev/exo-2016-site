/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function EditParticipantModal($instance, participant, ParticipantModel) {
        var view = this;
        view.data = {
            name: participant.getName(),
            surname: participant.getSurname(),
            middleName: participant.getPatronymic(),
            organization: participant.getOrganization(),
            age: participant.getAge(),
            position: participant.getPosition()
        };

        view.ok = function () {
            var updatedParticipant = ParticipantModel.$apply(participant.getId(),
                view.data.name, view.data.surname, view.data.middleName, view.data.organization,
                view.data.age, view.data.position);

            ParticipantModel.update(participant.getId(), updatedParticipant)
                .then(function (created) {
                    $instance.close(created);
                });
        };

        view.cancel = function () {
            $instance.dismiss("Close");
        };

    }

    EditParticipantModal.$inject = ['$uibModalInstance', 'participant', 'ParticipantModel'];

    return EditParticipantModal;
});
