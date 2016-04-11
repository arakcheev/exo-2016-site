define(function () {

    function ContactsController() {

        this.map = {
            center: {
                latitude: 55.736002,
                longitude: 37.627309
            },
            zoom: 17
        };

        this.marker = {
            latitude: 55.736002,
            longitude: 37.627309
        };
    }

    return ContactsController;
});