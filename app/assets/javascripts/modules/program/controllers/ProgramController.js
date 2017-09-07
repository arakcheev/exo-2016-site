define(['underscore'], function (_) {

    function ProgramController(Routes, $location) {

        var view = this;

        //Lol ) Three days of school.
        view.day1 = 15;
        view.day2 = 16;
        view.day3 = 17;

        view.days = {};

        var downloadUrl = (function () {
            var origin = location.protocol + "//" + location.host;
            return origin + Routes.controllers.HomeController.downloadProgram("exo2016-program.pdf").url;
        })();

        view.download = function () {
            window.location.href = downloadUrl;
        };

        Routes.controllers.AdminController.getProgram().get().then(function (response) {
            view.days = {};
            for (var dateStr in response.data) {
                var dateParts = dateStr.split(' ');
                var date = new Date(dateParts[2], (dateParts[1] - 1), dateParts[0]);
                var day = date.getDate();
                console.log(dateStr, response.data[dateStr]);
                view.days[day] = response.data[dateStr];
            }
        });

    }

    ProgramController.$inject = ['Routes', '$location'];

    return ProgramController;
});