/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['underscore'], function (_) {


    function Participant(id, name, surname, patronymic, status, organization, age, position) {

        this.getId = function () {
            return id;
        };

        this.getName = function () {
            return name;
        };

        this.getSurname = function () {
            return surname;
        };

        this.getPatronymic = function () {
            return patronymic;
        };

        this.getStatus = function () {
            return status;
        };

        this.getOrganization = function () {
            return organization;
        };

        this.getAge = function () {
            return age;
        };

        this.getPosition = function () {
            return position;
        };
    }

    var fromJson = function (json) {
        if (_.isArray(json)) {
            return _.map(json, function (item) {
                return fromJson(item);
            });
        } else {
            return new Participant(json._id, json.name, json.surname, json.lastname, json.status, json.organization, json.age, json.position);
        }
    };


    function ParticipantModel(Routes) {

        function participantToJson(participant) {
            return {
                name: participant.getName(),
                surname: participant.getSurname(),
                middleName: participant.getPatronymic(),
                status: participant.getStatus(),
                organization: participant.getOrganization(),
                age: participant.getAge(),
                position: participant.getPosition()
            };
        }

        Participant.fetch = function () {
            return Routes.controllers.AdminController.listParticipants().get()
                .then(function (response) {
                    var data = response.data;
                    return fromJson(data);
                })
                .catch(function (error) {
                    //error here
                    return [];
                });
        };

        Participant.update = function (id, participant) {
            return Routes.controllers.AdminController.updateParticipant(id)
                .put(participantToJson(participant))
                .then(function (response) {
                    var data = response.data;
                    return fromJson(data);
                })
                .catch(function () {
                    console.error("Not saved participant");
                    return null;
                });
        };

        Participant.remove = function (participant) {
            return Routes.controllers.AdminController.removeParticipant(participant.getId()).delete()
                .then(function (response) {
                    return response;
                });
        };

        Participant.$apply = function (id, name, surname, middleName, organization, age, position) {

            return new Participant(id, name, surname, middleName, 0, organization, age, position);
        };

        return Participant;
    }

    ParticipantModel.$inject = ['Routes'];

    return ParticipantModel;


});
