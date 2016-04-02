define(function () {
    return function () {
        return {
            require: 'ngModel',
            link: function (scope, element, attrs, ctrl) {
                element.bind("keydown keypress", function (event) {
                    if (event.which === 27) {
                        scope.$apply(function () {
                            scope.$eval(attrs.onEsc, {'event': event});
                        });
                        event.preventDefault();
                    }
                });
            }
        };
    };
});