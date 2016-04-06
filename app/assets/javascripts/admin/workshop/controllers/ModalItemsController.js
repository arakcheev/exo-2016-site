/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['underscore'], function (_) {

    function ModalItemsController(mayBeItem, $instance, Items) {
        var view = this;

        var isEdit = this.isEdit = !(_.isNull(mayBeItem) || _.isUndefined(mayBeItem)) && !_.isUndefined(mayBeItem.getId());

        if (isEdit) {
            view.data = {
                startDate: mayBeItem.getStartDate(),
                endDate: mayBeItem.getEndDate(),
                title: mayBeItem.getTitle()
            };
        } else {
            view.data = {
                startDate: new Date(2016, 4, 30, 9, 0),
                endDate: new Date(2016, 4, 30, 9, 30)
            };
        }

        view.ok = function () {
            var item = Items.$apply(view.data.startDate, view.data.endDate, view.data.title);

            if (isEdit) {
                Items.update(mayBeItem.getId(), item).then(function (updated) {
                    $instance.close(updated);
                });
            } else {
                Items.create(item).then(function (created) {
                    $instance.close(created);
                });
            }
        };

        view.cancel = function () {
            $instance.dismiss("Close");
        };

    }

    ModalItemsController.$inject = ['mayBeItem', '$uibModalInstance', 'Items'];

    return ModalItemsController;
});