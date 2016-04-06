/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function WorkShopItem(id, startDate, endDate, title) {

        this.getId = function () {
            return id;
        };

        this.getStartDate = function () {
            return new Date(startDate);
        };

        this.getEndDate = function () {
            return new Date(endDate);
        };

        this.getTitle = function () {
            return title;
        };

    }

    var fromJson = function (json) {
        if (_.isArray(json)) {
            return _.map(json, function (item) {
                return fromJson(item);
            });
        } else {
            return new WorkShopItem(json._id, json.startDate, json.endDate, json.title);
        }
    };

    function WorkShopModel(Routes) {

        function itemToJson(item) {
            return {
                startDate: item.getStartDate().getTime(),
                endDate: item.getEndDate().getTime(),
                title: item.getTitle()
            };
        }

        WorkShopItem.fetch = function () {
            return Routes.controllers.AdminController.listWorkShopItems().get()
                .then(function (response) {
                    var data = response.data;
                    return fromJson(data);
                })
                .catch(function (error) {
                    //error here
                    return [];
                });
        };

        WorkShopItem.create = function (item) {
            return Routes.controllers.AdminController.newWorkShopItem()
                .post(itemToJson(item))
                .then(function (response) {
                    var data = response.data;
                    return fromJson(data);
                })
                .catch(function () {
                    console.error("Not saved item");
                    return null;
                });
        };

        WorkShopItem.update = function (id, item) {
            return Routes.controllers.AdminController.updateWorkShopItem(id)
                .put(itemToJson(item))
                .then(function (response) {
                    var data = response.data;
                    return fromJson(data);
                })
                .catch(function () {
                    console.error("Not saved item");
                    return null;
                });
        };

        WorkShopItem.remove = function (item) {
            return Routes.controllers.AdminController.removeWorkShopItem(item.getId()).delete()
                .then(function (response) {
                    return response;
                });
        };

        WorkShopItem.$apply = function (startDate, endDate, title) {
            return new WorkShopItem(null, startDate, endDate, title);
        };

        return WorkShopItem;
    }

    WorkShopModel.$inject = ['Routes'];

    return WorkShopModel;

});
