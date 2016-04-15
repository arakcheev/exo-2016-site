define(['underscore'], function (_) {

    function ProgramController(Routes) {

        var view = this;

        //Lol ) Three days of school.
        view.day1 = 30;
        view.day2 = 31;
        view.day3 = 1;

        view.days = {};

        Routes.controllers.AdminController.getProgram().get().then(function (response) {
            view.days = {};
            for (var dateStr in response.data) {
                var dateParts = dateStr.split(' ');
                var date = new Date(dateParts[2], (dateParts[1] - 1), dateParts[0]);
                var day = date.getDate();
                view.days[day] = response.data[dateStr];
            }
            console.log(view.days);
        });

    }

    ProgramController.$inject = ['Routes'];

    return ProgramController;
});