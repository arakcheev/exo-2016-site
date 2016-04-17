/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(['underscore'], function () {

    function RemoveModalController($uibModalInstance, name) {

        this.name = name;

        this.$ok = function () {
            $uibModalInstance.close(true);
        };

        this.$close = function () {
            $uibModalInstance.dismiss(false);
        };
    }

    RemoveModalController.$inject = ['$uibModalInstance', 'name'];

    function RemoveModal($uibModal) {

        return function (name, callback) {

            var modalInstance = $uibModal.open({
                templateUrl: '/assets/templates/remove-modal.html',
                controller: RemoveModalController,
                controllerAs: "removeModal",
                resolve: {
                    name: function () {
                        return name;
                    }
                }
            });

            modalInstance.result.then(function (isRemove) {
                if (isRemove) {
                    callback.apply(this);
                }
            }, function () {
                //$log.info('Modal dismissed at: ' + new Date());
            });

        };
    }

    RemoveModal.$inject = ['$uibModal'];

    return RemoveModal;
});
