define(function () {

    return function (module) {

        module.controller("MapController", MapController);

        function MapController() {

            this.map = {
                center: {
                    latitude: 55.736002,
                    longitude: 37.627309
                },
                zoom: 17
            };

        }

    };
});