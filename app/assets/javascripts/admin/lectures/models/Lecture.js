/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function Lecture(id, abstract, date, speaker, title, url) {

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

        this.getUrl = function () {
            return url;
        };
    }

    var fromJson = function (json) {
        if (_.isArray(json)) {
            return _.map(json, function (item) {
                return fromJson(item);
            });
        } else {
            return new Lecture(json._id, json.abstr, json.date, json.speaker, json.title, json.url);
        }
    };

    function LectureModel(Routes) {

        function lectureToJson(lecture) {
            return {
                speaker: lecture.getSpeaker(),
                date: lecture.getDate().getTime(),
                organization: lecture.getOrganization(),
                title: lecture.getTitle(),
                abst: lecture.getAbstract(),
                url: lecture.getUrl()
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

        Lecture.update = function (id, lecture) {
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

        Lecture.remove = function (lecture) {
            return Routes.controllers.AdminController.removeLecture(lecture.getId()).delete()
                .then(function (response) {
                    return response;
                });
        };

        Lecture.$apply = function (abstract, date, speaker, organization, title, url) {
            var sp = {
                fullname: speaker,
                organization: organization
            };
            return new Lecture(null, abstract, date, sp, title, url);
        };

        return Lecture;
    }

    LectureModel.$inject = ['Routes'];

    return LectureModel;

});
