define(['underscore'], function (_) {

    function ProgramController(Routes) {

        var view = this;

        //Lol ) Three days of school.
        view.day1 = 30;
        view.day2 = 31;
        view.day3 = 1;

        view.days = {};

        Routes.controllers.AdminController.getProgram().get().then(function (response) {
            view.days = response.data;
        });

    }

    ProgramController.$inject = ['Routes'];

    return ProgramController;
});