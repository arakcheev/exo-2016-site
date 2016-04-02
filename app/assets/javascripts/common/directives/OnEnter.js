
define(function () {
    return function () {
        return {
            require: 'ngModel',
            link: function (scope, element, attrs, ctrl) {
                element.bind("keydown keypress", function (event) {
                    if(ctrl.$dirty && ctrl.$valid){
                        if (event.which === 13) {
                            scope.$apply(function () {
                                scope.$eval(attrs.onEnter, {'event': event});
                            });
                            event.preventDefault();
                        }
                    }
                });
            }
        };
    };
});