/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['underscore', './ModalItemsController'], function (_, ModalItemsController) {

    function WorkShopController(Items, $modal, RemoveModal) {

        var view = this;

        view.items = [];

        view.predicate = function (item) {
            return item.getStartDate().getTime();
        };

        function ItemModal(mayBeItem, callback) {
            $modal.open({
                templateUrl: '/assets/templates/workshop/add-modal.html',
                controller: ModalItemsController,
                controllerAs: 'modal',
                keyboard: false,
                backdrop: 'static',
                resolve: {
                    mayBeItem: mayBeItem
                }
            }).result.then(function (item) {
                callback.call(this, item);
            });
        }

        view.addNew = function () {
            new ItemModal(null, function (item) {
                view.items.push(item);
            });
        };

        view.$edit = function (item) {
            new ItemModal(item, function (updatedItem) {
                var index = _.indexOf(view.items, item);
                if (index > -1) {
                    view.items[index] = updatedItem;
                }
            });
        };

        view.$remove = function (item) {
            var name = item.getTitle();
            var callback = function () {
                Items.remove(item).then(function () {
                    var index = _.indexOf(view.items, item);
                    if (index > -1) {
                        view.items.splice(index, 1);
                    }
                });
            };
            RemoveModal(name, callback);
        };

        Items.fetch().then(function (items) {
            _.each(items, function (item) {
                view.items.push(item);
            });
        });

    }

    WorkShopController.$inject = ['Items', '$uibModal', 'RemoveModal'];

    return WorkShopController;
});
