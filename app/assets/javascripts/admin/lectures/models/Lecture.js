/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function Lecture(id, abstract, date, speaker, title) {

        this.getId = function () {
            return id;
        };

        this.getDate = function () {
            return new Date(date);
        };

        this.getAbstract = function () {
            return abstract;
        };

        this.getTitle = function () {
            return title;
        };

        this.getSpeaker = function () {
            return speaker.fullname;
        };

        this.getOrganization = function () {
            return speaker.organization;
        };
    }

    var fromJson = function (json) {
        if (_.isArray(json)) {
            return _.map(json, function (item) {
                return fromJson(item);
            });
        } else {
            return new Lecture(json._id, json.abstr, json.date, json.speaker, json.title);
        }
    };

    function LectureModel(Routes) {

        function lectureToJson(lecture){
            return {
                speaker: lecture.getSpeaker(),
                date: lecture.getDate().getTime(),
                organization: lecture.getOrganization(),
                title: lecture.getTitle(),
                abst: lecture.getAbstract()
            };
        }

        Lecture.fetch = function () {
            return Routes.controllers.AdminController.listLectures().get()
                .then(function (response) {
                    var data = response.data;
                    return fromJson(data);
                })
                .catch(function (error) {
                    //error here
                    return [];
                });
        };

        Lecture.create = function (lecture) {
            return Routes.controllers.AdminController.newLecture()
                .post(lectureToJson(lecture))
                .then(function (response) {
                    var data = response.data;
                    return fromJson(data);
                })
                .catch(function () {
                    console.error("Not saved lecture");
                    return null;
                });
        };

        Lecture.update = function(id, lecture){
            return Routes.controllers.AdminController.updateLecture(id)
                .put(lectureToJson(lecture))
                .then(function (response) {
                    var data = response.data;
                    return fromJson(data);
                })
                .catch(function () {
                    console.error("Not saved lecture");
                    return null;
                });
        };

        Lecture.$apply = function (abstract, date, speaker, organization, title) {
            var sp = {
                fullname: speaker,
                organization: organization
            };
            return new Lecture(null, abstract, date, sp, title);
        };

        return Lecture;
    }

    LectureModel.$inject = ['Routes'];

    return LectureModel;

});
