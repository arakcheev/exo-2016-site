/*
 * Copyright (c) 2016. Arakcheev Artem (artem.arakcheev@phystech.edu). All rights reserved.
 */

define(function () {

    function RegService(Routes) {

        var state = {
            error: false,
            ok: false
        };

        return {

            $getState: function () {
                return state;
            },

            register: function (data) {
                console.log(Routes);
                return Routes.controllers.RegistrationController.register().post(data)
                    .then(function () {
                        state.ok = true;
                    }, function (e) {
                        console.error(e);
                        state.error = true;
                    });
            }
        };

    }

    RegService.$inject = ['Routes'];

    return RegService;

});
